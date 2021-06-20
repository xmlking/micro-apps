package micro.apps.proto.utils

import micro.apps.proto.account.v1.GetRequest
import micro.apps.proto.account.v1.GetResponse
import micro.apps.proto.account.v1.SearchRequest
import micro.apps.proto.account.v1.SearchResponse
import micro.apps.proto.echo.v1.EchoRequest
import micro.apps.proto.echo.v1.EchoResponse
import micro.apps.proto.echo.v1.EchoStreamRequest
import micro.apps.proto.echo.v1.EchoStreamResponse

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

/**
 * Echo Service
 */
fun EchoRequest(init: EchoRequest.Builder.() -> Unit) =
    EchoRequest.newBuilder()
        .apply(init)
        .build()

fun EchoStreamRequest(init: EchoStreamRequest.Builder.() -> Unit) =
    EchoStreamRequest.newBuilder()
        .apply(init)
        .build()

fun EchoResponse(init: EchoResponse.Builder.() -> Unit) =
    EchoResponse.newBuilder()
        .apply(init)
        .build()

fun EchoStreamResponse(init: EchoStreamResponse.Builder.() -> Unit) =
    EchoStreamResponse.newBuilder()
        .apply(init)
        .build()
