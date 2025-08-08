package tech.sco.hetznerkloud.api

import io.ktor.client.HttpClient
import tech.sco.hetznerkloud.Failure
import tech.sco.hetznerkloud.InternalAPI
import tech.sco.hetznerkloud.Route
import tech.sco.hetznerkloud.makeRequest
import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.FloatingIp
import tech.sco.hetznerkloud.model.FloatingIp.Id
import tech.sco.hetznerkloud.request.AssignFloatingIp
import tech.sco.hetznerkloud.request.ChangeDeleteProtection
import tech.sco.hetznerkloud.request.ChangeReverseDns
import tech.sco.hetznerkloud.request.CreateFloatingIp
import tech.sco.hetznerkloud.request.FloatingIpFilter
import tech.sco.hetznerkloud.request.FloatingIpSorting
import tech.sco.hetznerkloud.request.Pagination
import tech.sco.hetznerkloud.request.UpdateFloatingIp
import tech.sco.hetznerkloud.request.toQueryParams
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.ItemCreated
import tech.sco.hetznerkloud.response.Items

class FloatingIps @InternalAPI constructor(private val httpClient: HttpClient) {
    @Throws(Failure::class)
    suspend fun all(filter: Set<FloatingIpFilter> = emptySet(), sorting: Set<FloatingIpSorting> = emptySet(), pagination: Pagination = Pagination()): Items<FloatingIp> =
        httpClient.makeRequest(Route.GET_ALL_FLOATING_IPS, queryParams = filter.toQueryParams() + sorting.toQueryParams() + pagination.toQueryParams())

    @Throws(Failure::class)
    suspend fun find(id: Id): Item<FloatingIp> = httpClient.makeRequest(Route.GET_FLOATING_IP, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun create(body: CreateFloatingIp): ItemCreated<FloatingIp> = httpClient.makeRequest(Route.CREATE_FLOATING_IP, body = body)

    @Throws(Failure::class)
    suspend fun update(id: Id, body: UpdateFloatingIp): Item<FloatingIp> = httpClient.makeRequest(Route.UPDATE_FLOATING_IP, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun delete(id: Id): Unit = httpClient.makeRequest(Route.DELETE_FLOATING_IP, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun changeProtection(id: Id, body: ChangeDeleteProtection): Item<Action> =
        httpClient.makeRequest(Route.CHANGE_FLOATING_IP_PROTECTION, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun changeReverseDns(id: Id, body: ChangeReverseDns): Item<Action> = httpClient.makeRequest(Route.CHANGE_FLOATING_IP_REVERSE_DNS, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun assign(id: Id, body: AssignFloatingIp): Item<Action> = httpClient.makeRequest(Route.ASSIGN_FLOATING_IP_TO_SERVER, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun unassign(id: Id): Item<Action> = httpClient.makeRequest(Route.UNASSIGN_FLOATING_IP_FROM_SERVER, resourceId = id.value)
}
