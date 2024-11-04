package tech.sco.hetznerkloud.repository

import io.ktor.client.HttpClient
import tech.sco.hetznerkloud.Route
import tech.sco.hetznerkloud.makeRequest
import tech.sco.hetznerkloud.model.Error
import tech.sco.hetznerkloud.model.Network
import tech.sco.hetznerkloud.model.Network.Id
import tech.sco.hetznerkloud.request.CreateNetwork
import tech.sco.hetznerkloud.request.NetworkFilter
import tech.sco.hetznerkloud.request.Pagination
import tech.sco.hetznerkloud.request.UpdateNetwork
import tech.sco.hetznerkloud.request.toQueryParams
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.Items

class Networks(private val httpClient: HttpClient) {

    @Throws(Error::class)
    suspend fun all(
        filter: Set<NetworkFilter> = emptySet(),
        pagination: Pagination = Pagination(),
    ): Items<Network> = httpClient.makeRequest(Route.GET_ALL_NETWORKS, queryParams = filter.toQueryParams() + pagination.toQueryParams())

    @Throws(Error::class)
    suspend fun find(id: Id): Item<Network> = httpClient.makeRequest(Route.GET_NETWORK, resourceId = id)

    @Throws(Error::class)
    suspend fun create(body: CreateNetwork): Item<Network> = httpClient.makeRequest(Route.CREATE_NETWORK, body = body)

    @Throws(Error::class)
    suspend fun update(id: Id, body: UpdateNetwork): Item<Network> = httpClient.makeRequest(Route.UPDATE_NETWORK, resourceId = id, body = body)

    @Throws(Error::class)
    suspend fun delete(id: Id): Unit = httpClient.makeRequest(Route.DELETE_NETWORK, resourceId = id)
}