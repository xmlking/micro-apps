package micro.apps.service.interceptors

import io.grpc.ForwardingServerCall
import io.grpc.Metadata
import io.grpc.ServerCall
import io.grpc.ServerCallHandler
import io.grpc.ServerInterceptor
import io.grpc.Status

class UnknownStatusInterceptor : ServerInterceptor {
    override fun <ReqT, RespT> interceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> {
        val wrappedCall = object : ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(call) {
            override fun close(st: Status, trailers: Metadata) {
                var status = st
                if (status.code == Status.Code.UNKNOWN && status.description == null &&
                    status.cause != null
                ) {
                    val t = status.cause
                    status = Status.INTERNAL.withDescription(t!!.message)
                        .augmentDescription(t.stackTrace.joinToString("\n"))
                }
                super.close(status, trailers)
            }
        }
        return next.startCall(wrappedCall, headers)
    }
}
