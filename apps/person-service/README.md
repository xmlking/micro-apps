# Person GraphQL

Person GraphQL service

This project uses Quarkus, the Supersonic Subatomic Java Framework.

* https://quarkus.io/guides/microprofile-graphql
* https://quarkus.io/blog/supersonic-subatomic-graphql/
* https://github.com/phillip-kruger/graphql-example

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
gradle :apps:person-service:quarkusDev
open http://localhost:8080/q/graphql-ui/
open http://localhost:8080/graphql/schema.graphql
```

```
{
  hello
}

{
    products
}

{
  hello,
  products
}

{
  person(id: 1){
    names
    surname
    scores{
      name
      value
    }
  }
}
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:

```shell script
./gradlew build
```

It produces the `quarkus-run.jar` file in the `build/quarkus-app/` directory. Be aware that it’s not an _über-jar_ as
the dependencies are copied into the `build/quarkus-app/lib/` directory.

If you want to build an _über-jar_, execute the following command:

```shell script
./gradlew build -Dquarkus.package.type=uber-jar
```

The application is now runnable using `java -jar build/quarkus-app/quarkus-run.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./gradlew build -Dquarkus.package.type=native
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./gradlew build -Dquarkus.package.type=native -Dquarkus.native.container-build=true
```


