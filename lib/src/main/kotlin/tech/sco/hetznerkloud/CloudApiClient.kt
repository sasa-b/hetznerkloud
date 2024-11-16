package tech.sco.hetznerkloud

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.InternalAPI
import kotlinx.serialization.json.Json
import tech.sco.hetznerkloud.api.Actions
import tech.sco.hetznerkloud.api.Certificates
import tech.sco.hetznerkloud.api.Datacenters
import tech.sco.hetznerkloud.api.Firewalls
import tech.sco.hetznerkloud.api.FloatingIps
import tech.sco.hetznerkloud.api.Images
import tech.sco.hetznerkloud.api.Isos
import tech.sco.hetznerkloud.api.LoadBalancerTypes
import tech.sco.hetznerkloud.api.LoadBalancers
import tech.sco.hetznerkloud.api.Networks
import tech.sco.hetznerkloud.api.PlacementGroups
import tech.sco.hetznerkloud.api.PrimaryIps
import tech.sco.hetznerkloud.api.SSHKeys
import tech.sco.hetznerkloud.api.ServerTypes
import tech.sco.hetznerkloud.api.Servers
import tech.sco.hetznerkloud.api.Volumes
import tech.sco.hetznerkloud.model.RateLimitExceededError
import tech.sco.hetznerkloud.response.Error

internal const val BASE_URL = "https://api.hetzner.cloud/v1"

@Suppress("LongParameterList")
@OptIn(InternalAPI::class)
class CloudApiClient private constructor(
    val actions: Actions,
    val servers: Servers,
    val serverTypes: ServerTypes,
    val images: Images,
    val isos: Isos,
    val datacenters: Datacenters,
    val placementGroups: PlacementGroups,
    val networks: Networks,
    val loadBalancers: LoadBalancers,
    val loadBalancerTypes: LoadBalancerTypes,
    val sshKeys: SSHKeys,
    val volumes: Volumes,
    val certificates: Certificates,
    val firewalls: Firewalls,
    val primaryIps: PrimaryIps,
    val floatingIps: FloatingIps,
) {
    companion object {
        fun of(token: ApiToken, httpEngine: HttpClientEngine = CIO.create(), block: HttpClientConfig<*>.() -> Unit = {}): CloudApiClient = HttpClient(httpEngine) {
            // throws RedirectResponseException, ClientRequestException, ServerResponseException
            // on 3xx, 4xx and 5xx status codes
            expectSuccess = true

            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
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
                        is ResponseException -> {
                            val errorResponse: Error = exception.response.body()

                            val error = when (errorResponse.error) {
                                is RateLimitExceededError -> {
                                    val headers = exception.response.headers

                                    errorResponse.error.copy(
                                        hourlyRateLimit = headers.get("RateLimit-Limit")?.toIntOrNull() ?: 3600,
                                        hourlyRateLimitRemaining = headers.get("RateLimit-Remaining")?.toIntOrNull(),
                                        hourlyRateLimitResetTimestamp = headers.get("RateLimit-Reset")?.toLongOrNull(),
                                    )
                                }
                                else -> errorResponse.error
                            }

                            throw Failure(error, request)
                        }
                    }
                }
            }

            block()
        }.let { httpClient ->
            CloudApiClient(
                actions = Actions(httpClient),
                servers = Servers(httpClient),
                serverTypes = ServerTypes(httpClient),
                images = Images(httpClient),
                isos = Isos(httpClient),
                datacenters = Datacenters(httpClient),
                placementGroups = PlacementGroups(httpClient),
                networks = Networks(httpClient),
                loadBalancers = LoadBalancers(httpClient),
                loadBalancerTypes = LoadBalancerTypes(httpClient),
                sshKeys = SSHKeys(httpClient),
                volumes = Volumes(httpClient),
                certificates = Certificates(httpClient),
                firewalls = Firewalls(httpClient),
                primaryIps = PrimaryIps(httpClient),
                floatingIps = FloatingIps(httpClient),
            )
        }
    }
}
