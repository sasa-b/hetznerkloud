/*
 * This source file was generated by the Gradle 'init' task
 */
package tech.sco.hetznerkloud

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import io.ktor.http.parameters
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import tech.sco.hetznerkloud.request.CreateServer
import tech.sco.hetznerkloud.request.HttpBody
import tech.sco.hetznerkloud.request.UpdateServer
import tech.sco.hetznerkloud.response.ActionItem
import tech.sco.hetznerkloud.response.ActionList
import tech.sco.hetznerkloud.response.DatacenterItem
import tech.sco.hetznerkloud.response.DatacenterList
import tech.sco.hetznerkloud.response.ImageItem
import tech.sco.hetznerkloud.response.ImageList
import tech.sco.hetznerkloud.response.IsoItem
import tech.sco.hetznerkloud.response.IsoList
import tech.sco.hetznerkloud.response.ServerCreated
import tech.sco.hetznerkloud.response.ServerDeleted
import tech.sco.hetznerkloud.response.ServerItem
import tech.sco.hetznerkloud.response.ServerList
import tech.sco.hetznerkloud.response.ServerMetrics
import tech.sco.hetznerkloud.response.ServerTypeItem
import tech.sco.hetznerkloud.response.ServerTypeList
import tech.sco.hetznerkloud.response.ServerUpdated
import tech.sco.hetznerkloud.model.ServerMetrics.Type as ServerMetricsType

private const val BASE_URL = "https://api.hetzner.cloud/v1"

class CloudApiClient private constructor(
    httpEngine: HttpClientEngine,
    private val token: ApiToken,
) {
    private val client =
        HttpClient(httpEngine) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
//                        isLenient = true
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
        }

    companion object {
        fun of(
            token: ApiToken,
            httpEngine: HttpClientEngine = CIO.create(),
        ) = CloudApiClient(httpEngine, token)
    }

    suspend fun actions(): ActionList = request(Route.GET_ALL_ACTIONS)

    suspend fun actions(id: Long): ActionItem = request(Route.GET_ACTION, id)

    suspend fun servers(): ServerList = request(Route.GET_ALL_SERVERS)

    suspend fun servers(id: Long): ServerItem = request(Route.GET_SERVER, id)

    suspend fun servers(body: CreateServer): ServerCreated = request(Route.CREATE_SERVER, body = body)

    suspend fun servers(id: Long, body: UpdateServer): ServerUpdated = request(Route.UPDATE_SERVER, id, body)

    suspend fun serverMetrics(id: Long, type: Set<ServerMetricsType>): ServerMetrics = request(Route.GET_SERVER_METRICS, id, queryParams = mapOf("type" to listOf(type.joinToString(","))))

    suspend fun serversDelete(id: Long): ServerDeleted = request(Route.DELETE_SERVER, id)

    suspend fun datacenters(): DatacenterList = request(Route.GET_ALL_DATACENTERS)

    suspend fun datacenters(id: Long): DatacenterItem = request(Route.GET_DATACENTER, id)

    suspend fun images(): ImageList = request(Route.GET_ALL_IMAGES)

    suspend fun images(id: Long): ImageItem = request(Route.GET_IMAGE, id)

    suspend fun isos(): IsoList = request(Route.GET_ALL_ISOS)

    suspend fun isos(id: Long): IsoItem = request(Route.GET_ISO, id)

    suspend fun serverTypes(): ServerTypeList = request(Route.GET_ALL_SERVER_TYPES)

    suspend fun serverTypes(id: Long): ServerTypeItem = request(Route.GET_SERVER_TYPE, id)

    private suspend inline fun <reified T> request(route: Route, resourceId: Long? = null, body: HttpBody? = null, queryParams: Map<String, List<String>> = emptyMap()): T =
        route.value.let {
            val (httpMethod, path) = it

            client
                .request(BASE_URL) {
                    method = httpMethod
                    url {
                        if (resourceId != null) {
                            appendPathSegments(path.withId(resourceId).value)
                        } else {
                            appendPathSegments(path.value)
                        }
                    }
                    if (body != null) {
                        contentType(ContentType.Application.Json)
                        setBody(body)
                    }
                    if (queryParams.isNotEmpty()) {
                        parameters {
                            queryParams.forEach { (key, values) ->
                                values.forEach { value ->
                                    append(key, value)
                                }
                            }
                        }
                    }
                }.body()
        }
}
