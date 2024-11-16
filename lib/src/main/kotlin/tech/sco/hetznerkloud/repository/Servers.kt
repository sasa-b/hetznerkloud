package tech.sco.hetznerkloud.repository

import io.ktor.client.HttpClient
import io.ktor.utils.io.InternalAPI
import tech.sco.hetznerkloud.Failure
import tech.sco.hetznerkloud.Route
import tech.sco.hetznerkloud.makeRequest
import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.Server
import tech.sco.hetznerkloud.model.Server.Id
import tech.sco.hetznerkloud.model.ServerMetrics
import tech.sco.hetznerkloud.request.AddToPlacementGroup
import tech.sco.hetznerkloud.request.CreateServer
import tech.sco.hetznerkloud.request.FilterFields
import tech.sco.hetznerkloud.request.Pagination
import tech.sco.hetznerkloud.request.ServerActionFilter
import tech.sco.hetznerkloud.request.ServerFilter
import tech.sco.hetznerkloud.request.ServerMetricsFilter
import tech.sco.hetznerkloud.request.ServerSorting
import tech.sco.hetznerkloud.request.UpdateResource
import tech.sco.hetznerkloud.request.toQueryParams
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.Items
import tech.sco.hetznerkloud.response.ServerCreated
import tech.sco.hetznerkloud.response.ServerDeleted

class Servers @InternalAPI constructor(private val httpClient: HttpClient) {

    @Throws(Failure::class)
    suspend fun all(
        filter: Set<ServerFilter> = emptySet(),
        sorting: Set<ServerSorting> = emptySet(),
        pagination: Pagination = Pagination(),
    ): Items<Server> = httpClient.makeRequest(
        Route.GET_ALL_SERVERS,
        queryParams = filter.toQueryParams() + sorting.toQueryParams() + pagination.toQueryParams(),
    )

    @Throws(Failure::class)
    suspend fun find(id: Id): Item<Server> = httpClient.makeRequest(Route.GET_SERVER, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun create(body: CreateServer): ServerCreated = httpClient.makeRequest(Route.CREATE_SERVER, body = body)

    @Throws(Failure::class)
    suspend fun update(id: Id, body: UpdateResource): Item<Server> = httpClient.makeRequest(Route.UPDATE_SERVER, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun delete(id: Id): ServerDeleted = httpClient.makeRequest(Route.DELETE_SERVER, id.value)

    @Throws(Failure::class)
    suspend fun metrics(id: Id, filter: Set<ServerMetricsFilter>): Item<ServerMetrics> {
        val (types, other) = filter.partition { it.first == FilterFields.ServerMetrics.TYPE }

        return httpClient.makeRequest(
            Route.GET_SERVER_METRICS,
            id.value,
            queryParams = listOf(
                Pair("type", types.joinToString(",") { it.second }),
            ) + other.toQueryParams(),
        )
    }

    @Throws(Failure::class)
    suspend fun actions(
        filter: Set<ServerActionFilter> = emptySet(),
        sorting: Set<ServerActionFilter> = emptySet(),
        pagination: Pagination = Pagination(),
    ): Items<Action> = httpClient.makeRequest(
        Route.GET_ALL_SERVER_ACTIONS,
        queryParams = filter.toQueryParams() + sorting.toQueryParams() + pagination.toQueryParams()
    )

    @Throws(Failure::class)
    suspend fun actions(
        serverId: Id,
        filter: Set<ServerActionFilter> = emptySet(),
        sorting: Set<ServerActionFilter> = emptySet(),
        pagination: Pagination = Pagination(),
    ): Items<Action> = httpClient.makeRequest(
        Route.GET_SERVER_ACTIONS,
        resourceId = serverId.value,
        queryParams = filter.toQueryParams() + sorting.toQueryParams() + pagination.toQueryParams()
    )

    @Throws(Failure::class)
    suspend fun action(id: Action.Id): Item<Action> = httpClient.makeRequest(Route.GET_SERVER_ACTION, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun addToPlacementGroup(id: Id, body: AddToPlacementGroup): Item<Action> = httpClient.makeRequest(Route.ADD_SERVER_TO_PLACEMENT_GROUP, resourceId = id.value, body = body)
}
