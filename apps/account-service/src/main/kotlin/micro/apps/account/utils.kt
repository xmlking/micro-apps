package micro.apps.account

import com.alibaba.csp.sentinel.adapter.grpc.SentinelGrpcClientInterceptor
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.internal.DnsNameResolverProvider
import micro.apps.proto.account.v1.GetRequest
import micro.apps.proto.account.v1.GetResponse
import micro.apps.proto.account.v1.SearchRequest
import micro.apps.proto.account.v1.SearchResponse

fun channelForTarget(target: String): ManagedChannel {
    return ManagedChannelBuilder
        .forTarget(target)
        .nameResolverFactory(DnsNameResolverProvider())
        // .defaultLoadBalancingPolicy()
        // .executor(Dispatchers.Default.asExecutor())
        .usePlaintext()
        .build()
}

fun sentinelChannelForTarget(target: String): ManagedChannel {
    return ManagedChannelBuilder
        .forTarget(target)
        .nameResolverFactory(DnsNameResolverProvider())
        .intercept(SentinelGrpcClientInterceptor())
        .usePlaintext()
        .build()
}

fun GetRequest(init: GetRequest.Builder.() -> Unit) =
    GetRequest.newBuilder()
        .apply(init)
        .build()

fun SearchRequest(init: SearchRequest.Builder.() -> Unit) =
    SearchRequest.newBuilder()
        .apply(init)
        .build()

fun GetResponse(init: GetResponse.Builder.() -> Unit) =
    GetResponse.newBuilder()
        .apply(init)
        .build()

fun SearchResponse(init: SearchResponse.Builder.() -> Unit) =
    SearchResponse.newBuilder()
        .apply(init)
        .build()
