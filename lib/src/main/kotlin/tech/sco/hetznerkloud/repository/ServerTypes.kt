package tech.sco.hetznerkloud.repository

import io.ktor.client.HttpClient
import tech.sco.hetznerkloud.Route
import tech.sco.hetznerkloud.makeRequest
import tech.sco.hetznerkloud.model.Error
import tech.sco.hetznerkloud.model.ServerType
import tech.sco.hetznerkloud.model.ServerType.Id
import tech.sco.hetznerkloud.request.Pagination
import tech.sco.hetznerkloud.request.ServerTypeFilter
import tech.sco.hetznerkloud.request.toQueryParams
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.Items

class ServerTypes(private val httpClient: HttpClient) {
    @Throws(Error::class)
    suspend fun all(
        filter: Set<ServerTypeFilter> = emptySet(),
        pagination: Pagination = Pagination(),
    ): Items<ServerType> =
        httpClient.makeRequest(
            Route.GET_ALL_SERVER_TYPES,
            queryParams = filter.toQueryParams() + pagination.toQueryParams(),
        )

    @Throws(Error::class)
    suspend fun find(id: Id): Item<ServerType> = httpClient.makeRequest(Route.GET_SERVER_TYPE, id.value)
}
