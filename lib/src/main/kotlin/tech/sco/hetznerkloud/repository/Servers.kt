package tech.sco.hetznerkloud.repository

import io.ktor.client.HttpClient
import tech.sco.hetznerkloud.Route
import tech.sco.hetznerkloud.makeRequest
import tech.sco.hetznerkloud.model.Error
import tech.sco.hetznerkloud.model.Server.Id
import tech.sco.hetznerkloud.model.ServerMetrics.Type
import tech.sco.hetznerkloud.request.CreateServer
import tech.sco.hetznerkloud.request.Pagination
import tech.sco.hetznerkloud.request.ServerSorting
import tech.sco.hetznerkloud.request.UpdateServer
import tech.sco.hetznerkloud.request.toQueryParams
import tech.sco.hetznerkloud.response.ServerCreated
import tech.sco.hetznerkloud.response.ServerDeleted
import tech.sco.hetznerkloud.response.ServerItem
import tech.sco.hetznerkloud.response.ServerList
import tech.sco.hetznerkloud.response.ServerMetrics
import tech.sco.hetznerkloud.response.ServerUpdated

class Servers(private val httpClient: HttpClient) {
    @Throws(Error::class)
    suspend fun all(
        sorting: Set<ServerSorting> = emptySet(),
        pagination: Pagination = Pagination(),
    ): ServerList = httpClient.makeRequest(Route.GET_ALL_SERVERS, queryParams = sorting.toQueryParams() + pagination.toQueryParams())

    @Throws(Error::class)
    suspend fun find(id: Id): ServerItem = httpClient.makeRequest(Route.GET_SERVER, id)

    @Throws(Error::class)
    suspend fun create(body: CreateServer): ServerCreated = httpClient.makeRequest(Route.CREATE_SERVER, body = body)

    @Throws(Error::class)
    suspend fun update(id: Id, body: UpdateServer): ServerUpdated = httpClient.makeRequest(Route.UPDATE_SERVER, id, body)

    @Throws(Error::class)
    suspend fun delete(id: Id): ServerDeleted = httpClient.makeRequest(Route.DELETE_SERVER, id)

    @Throws(Error::class)
    suspend fun metrics(id: Id, type: Set<Type>): ServerMetrics = httpClient.makeRequest(Route.GET_SERVER_METRICS, id, queryParams = listOf(Pair("type", type.joinToString(","))))
}
