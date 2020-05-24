package micro.apps.proto.account.fixtures

import micro.apps.proto.account.v1.CreateRequest
import micro.apps.proto.common.fixtures.mockPerson

fun mockAccountCreateRequest(mockId: Int): CreateRequest {
    return CreateRequest.newBuilder().setAccount(mockPerson(mockId)).build()
}
