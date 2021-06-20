package micro.apps.service

import com.alibaba.csp.sentinel.adapter.grpc.SentinelGrpcClientInterceptor
import io.grpc.ChannelCredentials
import io.grpc.Grpc
import io.grpc.InsecureChannelCredentials
import io.grpc.ManagedChannel

fun channelForTarget(target: String, creds: ChannelCredentials = InsecureChannelCredentials.create()): ManagedChannel {
    return Grpc.newChannelBuilder(target, creds)
        /* Only for using provided test certs. */
        .overrideAuthority("www.sumo.com")
        // .defaultLoadBalancingPolicy("round_robin")
        // .executor(Dispatchers.Default.asExecutor())
        .build()
}

fun sentinelChannelForTarget(target: String, creds: ChannelCredentials = InsecureChannelCredentials.create()): ManagedChannel {
    return Grpc.newChannelBuilder(target, creds)
        .defaultLoadBalancingPolicy("round_robin")
        .intercept(SentinelGrpcClientInterceptor())
        .build()
}
