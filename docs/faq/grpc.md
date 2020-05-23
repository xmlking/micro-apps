# gRPC

- How to implement gRPC Authentication? 
    
    Credentials can be of two types:
    
    **Channel credentials**, which are attached to a Channel, such as Client SSL credentials.
    **Call credentials**, which are attached to a call (such as JWT token).

    ```java
    pool, _ := x509.SystemCertPool()
    // error handling omitted
    creds := credentials.NewClientTLSFromCert(pool, "")
    perRPC, _ := oauth.NewServiceAccountFromFile("service-account.json", scope)
    conn, _ := grpc.Dial(
        "greeter.googleapis.com",
        grpc.WithTransportCredentials(creds),
        grpc.WithPerRPCCredentials(perRPC),
    )
    // error handling omitted
    client := pb.NewGreeterClient(conn)
    // ...
    ```
    
    Reference : [gRPC Authentication](https://grpc.io/docs/guides/auth/)

### flow-control middleware 

- How to implement gRPC client/server side resilience?

    Found following two liberties that could be used as gRPC intercepts
    1. [concurrency-limits-grpc](https://github.com/Netflix/concurrency-limits)
    2. [Sentinel](https://sentinelguard.io/en-us/)
        1. [Sentinel-gRPC](https://sentinelguard.io/en-us/docs/open-source-framework-integrations.html)
