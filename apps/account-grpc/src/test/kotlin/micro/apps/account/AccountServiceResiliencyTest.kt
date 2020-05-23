package micro.apps.account

import com.alibaba.csp.sentinel.EntryType
import com.alibaba.csp.sentinel.adapter.grpc.SentinelGrpcClientInterceptor
import com.alibaba.csp.sentinel.slots.block.RuleConstant
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager
import com.alibaba.csp.sentinel.slots.clusterbuilder.ClusterBuilderSlot
import com.google.protobuf.StringValue
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.StatusException
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.async
import micro.apps.proto.account.v1.AccountServiceGrpcKt
import micro.apps.proto.account.v1.GetRequest

const val resourceName = "micro.apps.proto.account.v1.AccountService/Get"

fun configureFlowRule(count: Int) {
    val rule = FlowRule()
        .setCount(count.toDouble())
        .setGrade(RuleConstant.FLOW_GRADE_QPS)
        .setResource(resourceName)
        .setLimitApp("default")
        .`as`(FlowRule::class.java)
    FlowRuleManager.loadRules(listOf(rule))
}

fun configureBlockingFlowRule(count: Int) {
    val rule = FlowRule()
        .setCount(count.toDouble())
        .setGrade(RuleConstant.FLOW_GRADE_QPS)
        .setResource(resourceName)
        .setLimitApp("default")
        .`as`(FlowRule::class.java)
        .setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER)
        .setMaxQueueingTimeMs(20 * 1000)
    FlowRuleManager.loadRules(listOf(rule))
}

class AccountServiceResiliencyTest : FunSpec({

    val port = 8080
    lateinit var server: AccountServer
    lateinit var channel: ManagedChannel

    beforeSpec {
        server = AccountServer(port)
        server.start()
    }

    afterSpec {
        server.server.shutdown()
    }

    beforeTest {
        channel =
            ManagedChannelBuilder.forAddress("localhost", port)
                .usePlaintext()
                .intercept(SentinelGrpcClientInterceptor())
                .build()
    }

    afterTest {
        println("clearing the rules after test")
        channel.shutdownNow()
        FlowRuleManager.loadRules(null)
        ClusterBuilderSlot.getClusterNodeMap().clear()
    }

    test("rate-limit should block second request") {
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

    test("rate-limit should slowdown busted requests") {
        configureBlockingFlowRule(2)
        val accountStub: AccountServiceGrpcKt.AccountServiceCoroutineStub =
            AccountServiceGrpcKt.AccountServiceCoroutineStub(channel)

        val request = GetRequest.newBuilder().setId(StringValue.of("sumo")).build()
        val response = accountStub.get(request)
        response.account.firstName shouldBe "sumo"

        shouldNotThrowAny {
            repeat(10) {
                val res = async {
                    accountStub.get(request)
                }
                println(res.await().account.firstName)
            }
        }
    }
})
