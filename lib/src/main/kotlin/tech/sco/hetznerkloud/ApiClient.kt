package tech.sco.hetznerkloud

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import tech.sco.hetznerkloud.api.StorageBoxTypes
import tech.sco.hetznerkloud.api.StorageBoxes

@OptIn(InternalAPI::class)
class ApiClient private constructor(val storageBoxes: StorageBoxes, val storageBoxTypes: StorageBoxTypes) {
    companion object {
        fun of(token: ApiToken, httpEngine: HttpClientEngine = CIO.create(), block: HttpClientConfig<*>.() -> Unit = {}): ApiClient =
            configureHttpClient(token, httpEngine, block).let { httpClient ->
                ApiClient(
                    storageBoxes = StorageBoxes(httpClient),
                    storageBoxTypes = StorageBoxTypes(httpClient),
                )
            }
    }
}
