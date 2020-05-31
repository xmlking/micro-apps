package micro.apps.μservice

import com.alibaba.csp.sentinel.adapter.grpc.SentinelGrpcClientInterceptor
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.internal.DnsNameResolverProvider

fun channelForTarget(target: String): ManagedChannel {
    return ManagedChannelBuilder
        .forTarget(target)
        .nameResolverFactory(DnsNameResolverProvider())
        // .defaultLoadBalancingPolicy("round_robin")
        // .executor(Dispatchers.Default.asExecutor())
        .usePlaintext()
        .build()
}

fun sentinelChannelForTarget(target: String): ManagedChannel {
    return ManagedChannelBuilder
        .forTarget(target)
        .nameResolverFactory(DnsNameResolverProvider())
        // .defaultLoadBalancingPolicy("round_robin")
        .intercept(SentinelGrpcClientInterceptor())
        .usePlaintext()
        .build()
}
