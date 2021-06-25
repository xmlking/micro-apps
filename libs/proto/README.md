# Proto

Generated code from ProtoBuf files.

### Prerequisites

```bash
# buf: proto tool https://buf.build/docs/tour-1
brew tap bufbuild/buf
brew install buf
```

### Buf

```bash
# To list all files Buf is configured to use:
buf ls-files
# lint
buf lint
buf breaking --against-input '.git#branch=master'
```

### Test

```bash
gradle :libs:proto:test

# To use Buf-produced FileDescriptorSets with grpcurl on the fly:
grpcurl -protoset <(buf image build -o -) ...

# To use Buf-produced FileDescriptorSets with ghz on the fly:
ghz --protoset <(buf image build -o -) ...
```

### Build

```bash
gradle :libs:proto:generateProto
gradle :libs:proto:build
```

### Example

```kotlin
package com.baegoon.api.controller

import com.baegoon.proto.greeting.GreetingGrpc
import com.baegoon.proto.greeting.GreetingRequest
import io.grpc.ManagedChannelBuilder
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/greeting")
class GreetingController {

    private val channel = ManagedChannelBuilder
        .forAddress("localhost", 1234)
        .usePlaintext()
        .build()

    @GetMapping
    fun greeting(@RequestParam name: String): ResponseEntity<*> {
        val request = GreetingRequest.newBuilder()
            .setName(name)
            .build()

        val response = GreetingGrpc
            .newBlockingStub(this.channel)
            .greeting(request)

        return ResponseEntity.ok(response.hello)
    }
}
```

### Reference

1. https://grpc.io/docs/tutorials/basic/java/
2. https://developers.google.com/protocol-buffers/docs/javatutorial
3. https://developers.google.com/protocol-buffers/docs/reference/java-generated
4. https://planet.jboss.org/post/generic_marshalling_with_google_protocol_buffers
