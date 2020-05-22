package micro.apps.account

import com.alibaba.csp.sentinel.EntryType
import com.alibaba.csp.sentinel.adapter.grpc.SentinelGrpcClientInterceptor
import com.alibaba.csp.sentinel.slots.block.RuleConstant
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager
import com.alibaba.csp.sentinel.slots.clusterbuilder.ClusterBuilderSlot
import io.grpc.ManagedChannelBuilder
import io.grpc.StatusException
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

fun configureFlowRule(count: Int, resourceName: String) {
    val rule = FlowRule()
        .setCount(count.toDouble())
        .setGrade(RuleConstant.FLOW_GRADE_QPS)
        .setResource(resourceName)
        .setLimitApp("default")
        .`as`(FlowRule::class.java)
    FlowRuleManager.loadRules(listOf(rule))
}

class AccountServerTest : FunSpec({
    val resourceName = "micro.apps.proto.account.v1.AccountService/Get"
    val port = 8080
    lateinit var server: AccountServer

    beforeSpec() {
        server = AccountServer(port)
        server.start()
    }

    afterSpec() {
        server.server.shutdown()
        FlowRuleManager.loadRules(null)
        ClusterBuilderSlot.getClusterNodeMap().clear()
    }

    test("rate-limit should block second request") {
        configureFlowRule(2, resourceName)

        val channel =
            ManagedChannelBuilder.forAddress("localhost", port)
                .usePlaintext()
                .intercept(SentinelGrpcClientInterceptor())
                .build()
        val client = AccountClient(channel)

        shouldNotThrowAny {
            client.get("sumo")
        }
        val clusterNode = ClusterBuilderSlot.getClusterNode(resourceName, EntryType.OUT)
        clusterNode.totalRequest() - clusterNode.blockRequest() shouldBe 1

        // Not allowed to pass.
        configureFlowRule(0, resourceName)
        // The second request will be blocked.
        val e = shouldThrow<StatusException> {
            client.get("sumo")
        }

        e.message shouldBe "UNAVAILABLE: Flow control limit exceeded (client side)"
        clusterNode.blockRequest() shouldBe 1

        client.close()
    }
})
