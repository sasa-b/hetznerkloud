package tech.sco.hetznerkloud

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.request
import io.ktor.http.appendPathSegments
import tech.sco.hetznerkloud.api.Actions
import tech.sco.hetznerkloud.api.StorageBoxTypes
import tech.sco.hetznerkloud.api.StorageBoxes

@OptIn(InternalAPI::class)
class ApiClient private constructor(val actions: Actions, val storageBoxes: StorageBoxes, val storageBoxTypes: StorageBoxTypes) {
    companion object {
        const val BASE_URL = "https://api.hetzner.com"

        fun of(token: ApiToken, httpEngine: HttpClientEngine = CIO.create(), block: HttpClientConfig<*>.() -> Unit = {}): ApiClient = configureHttpClient(token, httpEngine, {
            block()
            defaultRequest {
                // Only sets scheme and host, disregards path
                url(BASE_URL)
            }
        }).let { httpClient ->
            ApiClient(
                actions = Actions(httpClient),
                storageBoxes = StorageBoxes(httpClient),
                storageBoxTypes = StorageBoxTypes(httpClient),
            )
        }
    }
}
