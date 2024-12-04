package tech.sco.hetznerkloud.api

import io.ktor.client.HttpClient
import io.ktor.utils.io.InternalAPI
import tech.sco.hetznerkloud.Failure
import tech.sco.hetznerkloud.Route
import tech.sco.hetznerkloud.makeRequest
import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.Network
import tech.sco.hetznerkloud.model.Network.Id
import tech.sco.hetznerkloud.request.AddRoute
import tech.sco.hetznerkloud.request.AddSubnet
import tech.sco.hetznerkloud.request.ChangeDeleteProtection
import tech.sco.hetznerkloud.request.ChangeIpRange
import tech.sco.hetznerkloud.request.CreateNetwork
import tech.sco.hetznerkloud.request.DeleteRoute
import tech.sco.hetznerkloud.request.DeleteSubnet
import tech.sco.hetznerkloud.request.NetworkFilter
import tech.sco.hetznerkloud.request.Pagination
import tech.sco.hetznerkloud.request.UpdateNetwork
import tech.sco.hetznerkloud.request.toQueryParams
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.Items

@Suppress("TooManyFunctions")
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
    suspend fun changeProtection(id: Id, body: ChangeDeleteProtection): Item<Action> = httpClient.makeRequest(Route.CHANGE_NETWORK_PROTECTION, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun changeIpRange(id: Id, body: ChangeIpRange): Item<Action> = httpClient.makeRequest(Route.CHANGE_NETWORK_IP_RANGE, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun addRoute(id: Id, body: AddRoute): Item<Action> = httpClient.makeRequest(Route.ADD_ROUTE_TO_NETWORK, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun deleteRoute(id: Id, body: DeleteRoute): Item<Action> = httpClient.makeRequest(Route.DELETE_ROUTE_FROM_NETWORK, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun addSubnet(id: Id, body: AddSubnet): Item<Action> = httpClient.makeRequest(Route.ADD_SUBNET_TO_NETWORK, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun deleteSubnet(id: Id, body: DeleteSubnet): Item<Action> = httpClient.makeRequest(Route.DELETE_SUBNET_FROM_NETWORK, resourceId = id.value, body = body)
}
