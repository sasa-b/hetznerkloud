/*
 * This source file was generated by the Gradle 'init' task
 */
package tech.sco.hetznerkloud

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import tech.sco.hetznerkloud.repository.Actions
import tech.sco.hetznerkloud.repository.Datacenters
import tech.sco.hetznerkloud.repository.Images
import tech.sco.hetznerkloud.repository.Isos
import tech.sco.hetznerkloud.repository.ServerTypes
import tech.sco.hetznerkloud.repository.Servers
import tech.sco.hetznerkloud.response.ErrorResponse

internal const val BASE_URL = "https://api.hetzner.cloud/v1"

class CloudApiClient private constructor(
    val actions: Actions,
    val servers: Servers,
    val serverTypes: ServerTypes,
    val images: Images,
    val isos: Isos,
    val datacenters: Datacenters,
) {
    companion object {
        fun of(token: ApiToken, httpEngine: HttpClientEngine = CIO.create()): CloudApiClient {
            val httpClient = HttpClient(httpEngine) {
                expectSuccess = true

                install(ContentNegotiation) {
                    json(
                        Json {
//                        classDiscriminator = "type"
                            ignoreUnknownKeys = true
//                        isLenient = true
//                        serializersModule = SerializersModule {
//                            polymorphic(Error::class) {
//                                subclass(UnauthorizedError::class)
//                                subclass(InvalidInputError::class)
//                            }
//                        }
                        },
                    )
                }

                install(Auth) {
                    bearer {
                        loadTokens {
                            BearerTokens(accessToken = token.value, refreshToken = null)
                        }
                    }
                }

                HttpResponseValidator {
                    handleResponseExceptionWithRequest { exception, request ->
                        when (exception) {
                            is ClientRequestException -> {
                                val response: ErrorResponse = exception.response.body()
                                throw response.error
                            }
                        }
                    }
                }
            }

            return CloudApiClient(
                actions = Actions(httpClient),
                servers = Servers(httpClient),
                serverTypes = ServerTypes(httpClient),
                images = Images(httpClient),
                isos = Isos(httpClient),
                datacenters = Datacenters(httpClient),
            )
        }
    }
}
