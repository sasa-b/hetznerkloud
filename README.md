# Getting Started

## Installing the package

Add to `build.gradle.kt` dependencies block.

```Kotlin
dependencies {
    implementation("tech.s-co.hetznerkloud:0.5.+")
    // or if you always want the latest version
    implementation("tech.s-co.hetznerkloud:latest.release")
}
```

## Usage

### [Hetzner Cloud API](https://docs.hetzner.cloud/reference/cloud)
```kotlin
import tech.sco.hetznerkloud.CloudApiClient
import tech.sco.hetznerkloud.ApiToken
import tech.sco.hetznerkloud.request.FilterFields
import tech.sco.hetznerkloud.request.SortingFields
import tech.sco.hetznerkloud.request.SortingDirection

fun main() {
    
    runBlocking {
        val apiToken = ApiToken.load(Path("/some/path/token.txt"))

        val client = CloudApiClient(apiToken)
        
        client.servers.all(
            filter = setOf(Pair(FilterFields.Server.STATUS, "running")), 
            sorting = setOf(Pair(SortingFields.Server.CREATED, SortingDirection.DESC))
        ).map {
            println("Id: ${it.id}")
            println("Name: ${it.name}")
        }
    }
}
```

### [Hetzner Storage Box API](https://docs.hetzner.cloud/reference/hetzner) 
```kotlin
import tech.sco.hetznerkloud.ApiClient
import tech.sco.hetznerkloud.ApiToken

fun main() {

    runBlocking {
        val apiToken = ApiToken.env("HETZNER_API_TOKEN") ?: ApiToken.file(Path("/some/path/token.txt"))

        val client = ApiClient(apiToken)

        client.storageBoxes.all().map {
            println("Id: ${it.id}")
            println("Name: ${it.name}")
        }
    }
}
```

## Error handling
You can find all the various errors in the `src/kotlin/tech/sco/hetznerkloud/model/Error.kt` file.

All errors are wrapped in the `Failure` throwable object which will also hold the request object that caused the error, that should help you in debugging potential issues.

```Kotlin
import tech.sco.hetznerkloud.CloudApiClient
import tech.sco.hetznerkloud.ApiToken
import tech.sco.hetznerkloud.request.CreateSSHKey

fun main() {
    
    runBlocking {
        val apiToken = ApiToken.env("HETZNER_API_TOKEN") ?: ApiToken.file(Path("/some/path/token.txt"))

        val client = CloudApiClient(apiToken)

        try {
            client.sshKeys.create(CreateSSHKey(name = "login key", labels = emptyMap()))
        } catch (f: Failure) {
            when {
                f.error is UnauthorizedError -> println(f.request!!.headers)
            }
        }
        
    }
}
```

## Ktor client plugins
You can extend the behaviour of the Ktor client used under the hood by adding official or custom plugins.

By default, the following ones are installed and used by this library:
* Auth
* Content Negotiation
* Kotlinx Serialization

For example let's add retry behaviour with a plugin:
```kotlin
val client = CloudApiClient(ApiToken("xxx-xxx-xxx")) {
    install(HttpRequestRetry) {
        retryOnServerErrors(maxRetries = 5)
    }
}
```

# Contributing

## Running integration tests

You need to provide a Hetzner API token either in an env variable `API_TEST_TOKEN=` or in a file `src/test/resources/token.txt`