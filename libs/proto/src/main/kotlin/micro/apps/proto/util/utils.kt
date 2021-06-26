package micro.apps.proto.util

import micro.apps.proto.echo.v1.EchoRequest
import micro.apps.proto.echo.v1.EchoResponse
import micro.apps.proto.echo.v1.EchoStreamRequest
import micro.apps.proto.echo.v1.EchoStreamResponse
import micro.apps.proto.keying.v1.KeyRequest
import micro.apps.proto.keying.v1.KeyResponse
import micro.apps.proto.linking.v1.LinkRequest
import micro.apps.proto.linking.v1.LinkResponse
import micro.apps.proto.account.v1.GetRequest as GetAccountRequest
import micro.apps.proto.account.v1.GetResponse as GetAccountResponse
import micro.apps.proto.account.v1.SearchRequest as SearchAccountRequest
import micro.apps.proto.account.v1.SearchResponse as SearchAccountResponse
import micro.apps.proto.address.v1.GetRequest as GetAddressRequest
import micro.apps.proto.address.v1.GetResponse as GetAddressResponse
import micro.apps.proto.address.v1.SearchRequest as SearchAddressRequest
import micro.apps.proto.address.v1.SearchResponse as SearchAddressResponse
import micro.apps.proto.order.v1.GetRequest as GetProductRequest
import micro.apps.proto.order.v1.GetResponse as GetProductResponse
import micro.apps.proto.order.v1.SearchRequest as SearchProductRequest
import micro.apps.proto.order.v1.SearchResponse as SearchProductResponse

/**
 * Temp kotlinized builders.
 * should be replaced when official `protoc-gen-grpc-kotlin` implements them
 */
fun GetAccountRequest(init: GetAccountRequest.Builder.() -> Unit) =
    GetAccountRequest.newBuilder()
        .apply(init)
        .build()

fun SearchAccountRequest(init: SearchAccountRequest.Builder.() -> Unit) =
    SearchAccountRequest.newBuilder()
        .apply(init)
        .build()

fun GetAccountResponse(init: GetAccountResponse.Builder.() -> Unit) =
    GetAccountResponse.newBuilder()
        .apply(init)
        .build()

fun SearchAccountResponse(init: SearchAccountResponse.Builder.() -> Unit) =
    SearchAccountResponse.newBuilder()
        .apply(init)
        .build()

/**
 * Address Service
 */
fun GetAddressRequest(init: GetAddressRequest.Builder.() -> Unit) =
    GetAddressRequest.newBuilder()
        .apply(init)
        .build()

fun SearchAddressRequest(init: SearchAddressRequest.Builder.() -> Unit) =
    SearchAddressRequest.newBuilder()
        .apply(init)
        .build()

fun GetAddressResponse(init: GetAddressResponse.Builder.() -> Unit) =
    GetAddressResponse.newBuilder()
        .apply(init)
        .build()

fun SearchAddressResponse(init: SearchAddressResponse.Builder.() -> Unit) =
    SearchAddressResponse.newBuilder()
        .apply(init)
        .build()

/**
 * Product Service
 */

fun GetProductRequest(init: GetProductRequest.Builder.() -> Unit) =
    GetProductRequest.newBuilder()
        .apply(init)
        .build()

fun SearchProductRequest(init: SearchProductRequest.Builder.() -> Unit) =
    SearchProductRequest.newBuilder()
        .apply(init)
        .build()

fun GetProductResponse(init: GetProductResponse.Builder.() -> Unit) =
    GetProductResponse.newBuilder()
        .apply(init)
        .build()

fun SearchProductResponse(init: SearchProductResponse.Builder.() -> Unit) =
    SearchProductResponse.newBuilder()
        .apply(init)
        .build()

/**
 * Keying Service
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

/**
 * Keying Service
 */
fun KeyRequest(init: KeyRequest.Builder.() -> Unit) =
    KeyRequest.newBuilder()
        .apply(init)
        .build()

fun KeyResponse(init: KeyResponse.Builder.() -> Unit) =
    KeyResponse.newBuilder()
        .apply(init)
        .build()

/**
 * Linking Service
 */
fun LinkRequest(init: LinkRequest.Builder.() -> Unit) =
    LinkRequest.newBuilder()
        .apply(init)
        .build()

fun LinkResponse(init: LinkResponse.Builder.() -> Unit) =
    LinkResponse.newBuilder()
        .apply(init)
        .build()
