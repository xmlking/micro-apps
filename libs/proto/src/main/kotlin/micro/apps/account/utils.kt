package micro.apps.account

import micro.apps.proto.account.v1.GetRequest
import micro.apps.proto.account.v1.GetResponse
import micro.apps.proto.account.v1.SearchRequest
import micro.apps.proto.account.v1.SearchResponse

/**
 * Temp kotlinized builders.
 * should be replaced when official `protoc-gen-grpc-kotlin` implements them
 */
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
