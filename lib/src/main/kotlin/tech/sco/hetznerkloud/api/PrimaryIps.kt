package tech.sco.hetznerkloud.api

import io.ktor.client.HttpClient
import tech.sco.hetznerkloud.Failure
import tech.sco.hetznerkloud.InternalAPI
import tech.sco.hetznerkloud.Route
import tech.sco.hetznerkloud.makeRequest
import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.PrimaryIp
import tech.sco.hetznerkloud.model.PrimaryIp.Id
import tech.sco.hetznerkloud.request.AssignPrimaryIp
import tech.sco.hetznerkloud.request.ChangeDeleteProtection
import tech.sco.hetznerkloud.request.ChangeReverseDns
import tech.sco.hetznerkloud.request.CreatePrimaryIp
import tech.sco.hetznerkloud.request.Pagination
import tech.sco.hetznerkloud.request.PrimaryIpFilter
import tech.sco.hetznerkloud.request.PrimaryIpSorting
import tech.sco.hetznerkloud.request.UpdatePrimaryIp
import tech.sco.hetznerkloud.request.toQueryParams
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.ItemCreated
import tech.sco.hetznerkloud.response.Items

class PrimaryIps @InternalAPI constructor(private val httpClient: HttpClient) {

    @Throws(Failure::class)
    suspend fun all(filter: Set<PrimaryIpFilter> = emptySet(), sorting: Set<PrimaryIpSorting> = emptySet(), pagination: Pagination = Pagination()): Items<PrimaryIp> =
        httpClient.makeRequest(Route.GET_ALL_PRIMARY_IPS, queryParams = filter.toQueryParams() + sorting.toQueryParams() + pagination.toQueryParams())

    @Throws(Failure::class)
    suspend fun find(id: Id): Item<PrimaryIp> = httpClient.makeRequest(Route.GET_PRIMARY_IP, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun create(body: CreatePrimaryIp): ItemCreated<PrimaryIp> = httpClient.makeRequest(Route.CREATE_PRIMARY_IP, body = body)

    @Throws(Failure::class)
    suspend fun update(id: Id, body: UpdatePrimaryIp): Item<PrimaryIp> = httpClient.makeRequest(Route.UPDATE_PRIMARY_IP, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun delete(id: Id): Unit = httpClient.makeRequest(Route.DELETE_PRIMARY_IP, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun changeProtection(id: Id, body: ChangeDeleteProtection): Item<Action> =
        httpClient.makeRequest(Route.CHANGE_PRIMARY_IP_PROTECTION, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun changeReverseDns(id: Id, body: ChangeReverseDns): Item<Action> = httpClient.makeRequest(Route.CHANGE_PRIMARY_IP_REVERSE_DNS, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun assign(id: Id, body: AssignPrimaryIp): Item<Action> = httpClient.makeRequest(Route.ASSIGN_PRIMARY_IP_TO_RESOURCE, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun unassign(id: Id): Item<Action> = httpClient.makeRequest(Route.UNASSIGN_PRIMARY_IP_FROM_RESOURCE, resourceId = id.value)
}
