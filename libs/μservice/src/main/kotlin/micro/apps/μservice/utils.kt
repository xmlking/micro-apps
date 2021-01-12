package micro.apps.μservice

import com.alibaba.csp.sentinel.adapter.grpc.SentinelGrpcClientInterceptor
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder

fun channelForTarget(target: String): ManagedChannel {
    return ManagedChannelBuilder
        .forTarget(target)
        .defaultLoadBalancingPolicy("round_robin")
        // .executor(Dispatchers.Default.asExecutor())
        .usePlaintext()
        .build()
}

fun sentinelChannelForTarget(target: String): ManagedChannel {
    return ManagedChannelBuilder
        .forTarget(target)
        .defaultLoadBalancingPolicy("round_robin")
        .intercept(SentinelGrpcClientInterceptor())
        .usePlaintext()
        .build()
}
