package tech.sco.hetznerkloud.api

import io.ktor.client.HttpClient
import io.ktor.utils.io.InternalAPI
import tech.sco.hetznerkloud.Failure
import tech.sco.hetznerkloud.Route
import tech.sco.hetznerkloud.makeRequest
import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.Network
import tech.sco.hetznerkloud.model.Network.Id
import tech.sco.hetznerkloud.request.CreateNetwork
import tech.sco.hetznerkloud.request.NetworkActionFilter
import tech.sco.hetznerkloud.request.NetworkActionSorting
import tech.sco.hetznerkloud.request.NetworkFilter
import tech.sco.hetznerkloud.request.Pagination
import tech.sco.hetznerkloud.request.UpdateNetwork
import tech.sco.hetznerkloud.request.toQueryParams
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.Items

class Networks @InternalAPI constructor(private val httpClient: HttpClient) {

    @Throws(Failure::class)
    suspend fun all(filter: Set<NetworkFilter> = emptySet(), pagination: Pagination = Pagination()): Items<Network> =
        httpClient.makeRequest(Route.GET_ALL_NETWORKS, queryParams = filter.toQueryParams() + pagination.toQueryParams())

    @Throws(Failure::class)
    suspend fun find(id: Id): Item<Network> = httpClient.makeRequest(Route.GET_NETWORK, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun create(body: CreateNetwork): Item<Network> = httpClient.makeRequest(Route.CREATE_NETWORK, body = body)

    @Throws(Failure::class)
    suspend fun update(id: Id, body: UpdateNetwork): Item<Network> = httpClient.makeRequest(Route.UPDATE_NETWORK, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun delete(id: Id): Unit = httpClient.makeRequest(Route.DELETE_NETWORK, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun actions(filter: Set<NetworkActionFilter> = emptySet(), sorting: Set<NetworkActionSorting> = emptySet(), pagination: Pagination = Pagination()): Items<Action> =
        httpClient.makeRequest(
            Route.GET_ALL_NETWORK_ACTIONS,
            queryParams = filter.toQueryParams() + sorting.toQueryParams() + pagination.toQueryParams(),
        )

    @Throws(Failure::class)
    suspend fun actions(
        id: Id,
        filter: Set<NetworkActionFilter> = emptySet(),
        sorting: Set<NetworkActionSorting> = emptySet(),
        pagination: Pagination = Pagination(),
    ): Items<Action> = httpClient.makeRequest(
        Route.GET_NETWORK_ACTIONS,
        resourceId = id.value,
        queryParams = filter.toQueryParams() + sorting.toQueryParams() + pagination.toQueryParams(),
    )

    @Throws(Failure::class)
    suspend fun action(actionId: Action.Id): Item<Action> = httpClient.makeRequest(Route.GET_NETWORK_ACTION, resourceId = actionId.value)

    @Throws(Failure::class)
    suspend fun action(networkId: Id, actionId: Action.Id): Item<Action> = httpClient.makeRequest(
        Route.GET_NETWORK_ACTION_FOR_NETWORK,
        routeParams = mapOf(
            "id" to networkId.value.toString(),
            "action_id" to actionId.value.toString(),
        ),
    )
}
