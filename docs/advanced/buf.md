# Buf

[Buf](https://buf.build/) is a tool for __Protobuf__ files:

- [Linter](https://buf.build/docs/lint-usage) that enforces good API design choices and structure.
- [Breaking change detector](https://buf.build/docs/breaking-usage) that enforces compatibility at the source code or
  wire level
- Configurable file [builder](https://buf.build/docs/build-overview) that
  produces [Images](https://buf.build/docs/build-images) our extension
  of [FileDescriptorSets](https://github.com/protocolbuffers/protobuf/blob/master/src/google/protobuf/descriptor.proto)

## Prerequisites

```bash
# buf: proto tool https://buf.build/docs/tour-1
brew tap bufbuild/buf
brew install buf
# or use `go install` to install Buf
go install github.com/bufbuild/buf/cmd/buf@latest
```

## Developer Workflow

### Info

```bash
# To list all files Buf is configured to use:
buf ls-files
# To see your currently configured lint or breaking checkers:
buf config ls-breaking-rules
# To see all available lint checkers independent of configuration/defaults:
buf config ls-lint-rules
```

### Build

```bash
# check
buf build -o /dev/null
buf build -o image.bin
```

### Lint

```bash
buf lint
# We can also output errors in a format you can then copy into your buf.yaml file
buf lint --error-format=config-ignore-yaml
# Run breaking change detection
# for dev local
buf breaking --against image.bin
buf breaking --against '.git#branch=main'
# for CI
export HTTPS_GIT=https://github.com/xmlking/yeti.git
buf check breaking --against-input "$(HTTPS_GIT)#branch=main"
```

### Format

```bash
prototool format -w proto;
```

### Generate

```bash
make proto
```

## Tools

### grpcurl

```bash
# To use Buf-produced FileDescriptorSets with grpcurl on the fly:
grpcurl -protoset <(buf image build -o -) ...
```

### ghz

```bash
# To use Buf-produced FileDescriptorSets with ghz on the fly:
ghz --protoset <(buf image build -o -) ...
```

## Reference

1. [Style Guide](https://buf.build/docs/style-guide)
1. [Buf docs](https://buf.build/docs/introduction)
1. [Buf Example](https://github.com/bufbuild/buf-example/blob/master/Makefile)
1. [Buf Schema Registry](https://buf.build/docs/roadmap)
