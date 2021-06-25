package micro.apps.service.domain.account

import com.alibaba.csp.sentinel.EntryType
import com.alibaba.csp.sentinel.adapter.grpc.SentinelGrpcClientInterceptor
import com.alibaba.csp.sentinel.slots.block.RuleConstant
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager
import com.alibaba.csp.sentinel.slots.clusterbuilder.ClusterBuilderSlot
import com.google.protobuf.StringValue
import io.grpc.ManagedChannel
import io.grpc.Server
import io.grpc.StatusException
import io.grpc.inprocess.InProcessChannelBuilder
import io.grpc.inprocess.InProcessServerBuilder
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.TestCaseConfig
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import micro.apps.proto.account.v1.AccountServiceGrpcKt
import micro.apps.proto.account.v1.GetRequest
import micro.apps.proto.account.v1.GetResponse
import micro.apps.test.E2E
import micro.apps.test.Slow
import micro.apps.test.gRPC
import mu.KotlinLogging
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security
import kotlin.system.measureTimeMillis
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.minutes

/**
 * Showcase backpressure handling techniques:
 * - Dropping the messages.
 * - Sensible buffering strategies (time vs count).
 * - Blocking the execution and processing the current set of events.
 * - Throttling and debouncing strategies.
 */
const val resourceName = "micro.apps.proto.account.v1.AccountService/Get"

fun configureFlowRule(qps: Int) {
    val rule = FlowRule()
        .setCount(qps.toDouble())
        .setGrade(RuleConstant.FLOW_GRADE_QPS)
        .setResource(resourceName)
        .setLimitApp("default")
        .`as`(FlowRule::class.java)
    FlowRuleManager.loadRules(listOf(rule))
}

fun configureBlockingFlowRule(qps: Int) {
    val rule = FlowRule()
        .setCount(qps.toDouble())
        .setGrade(RuleConstant.FLOW_GRADE_QPS)
        .setResource(resourceName)
        .setLimitApp("default")
        .`as`(FlowRule::class.java)
        .setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER)
        .setMaxQueueingTimeMs(20 * 1000)
    FlowRuleManager.loadRules(listOf(rule))
}

private val logger = KotlinLogging.logger {}

@ExperimentalTime
class AccountServiceResiliencyTest : FunSpec({
    // Add BCP to avoid `algid parse error, not a sequence` eror
    Security.addProvider(BouncyCastleProvider())

    // defaultTestConfig
    TestCaseConfig(timeout = Duration.minutes(3), tags = setOf(gRPC))

    lateinit var uniqueName: String
    lateinit var server: Server
    lateinit var channel: ManagedChannel

    beforeSpec {
        uniqueName = InProcessServerBuilder.generateName()
        server = InProcessServerBuilder.forName(uniqueName).directExecutor().addService(AccountService()).build()
        server.start()
    }

    afterSpec {
        server.shutdown()
    }

    beforeTest {
        channel = InProcessChannelBuilder.forName(uniqueName).intercept(SentinelGrpcClientInterceptor()).build()
    }

    afterTest {
        println("clearing the rules after test")
        channel.shutdownNow()
        FlowRuleManager.loadRules(null)
        ClusterBuilderSlot.getClusterNodeMap().clear()
    }

    test("rate-limit should block second request").config(enabled = true, tags = setOf(E2E)) {
        configureFlowRule(2)

        val client = AccountClient(channel)

        shouldNotThrowAny {
            client.get("sumo")
        }
        val clusterNode = ClusterBuilderSlot.getClusterNode(resourceName, EntryType.OUT)
        clusterNode.totalRequest() - clusterNode.blockRequest() shouldBe 1

        // Not allowed to pass.
        configureFlowRule(0)
        // The second request will be blocked.
        val e = shouldThrow<StatusException> {
            client.get("sumo")
        }

        e.message shouldBe "UNAVAILABLE: Flow control limit exceeded (client side)"
        clusterNode.blockRequest() shouldBe 1
    }

    test("flow-control should slowdown busted requests").config(enabled = true, tags = setOf(Slow, E2E)) {
        configureBlockingFlowRule(3)
        val accountStub: AccountServiceGrpcKt.AccountServiceCoroutineStub =
            AccountServiceGrpcKt.AccountServiceCoroutineStub(channel)

        lateinit var results: List<GetResponse>
        shouldNotThrowAny {
            val elapsed = measureTimeMillis {
                results = (1..10).map {
                    async {
                        logger.debug { "firing request #$it" }
                        val request = GetRequest.newBuilder().setId(StringValue.of("sumo$it")).build()
                        val res = accountStub.get(request)
                        logger.debug { "Received: ${res.account.firstName}" }
                        res
                    }
                }.awaitAll()
            }
            logger.info { "elapsed time $elapsed" }
        }
        results.size shouldBe 10
    }
})
