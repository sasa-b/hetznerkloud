package tech.sco.hetznerkloud.repository

import io.ktor.client.HttpClient
import io.ktor.utils.io.InternalAPI
import tech.sco.hetznerkloud.Failure
import tech.sco.hetznerkloud.Route
import tech.sco.hetznerkloud.makeRequest
import tech.sco.hetznerkloud.model.Firewall
import tech.sco.hetznerkloud.model.Firewall.Id
import tech.sco.hetznerkloud.request.CreateFirewall
import tech.sco.hetznerkloud.request.FirewallFilter
import tech.sco.hetznerkloud.request.FirewallSorting
import tech.sco.hetznerkloud.request.Pagination
import tech.sco.hetznerkloud.request.UpdateResource
import tech.sco.hetznerkloud.request.toQueryParams
import tech.sco.hetznerkloud.response.FirewallCreated
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.Items

class Firewalls @InternalAPI constructor(private val httpClient: HttpClient) {

    @Throws(Failure::class)
    suspend fun all(
        filter: Set<FirewallFilter> = emptySet(),
        sorting: Set<FirewallSorting> = emptySet(),
        pagination: Pagination = Pagination(),
    ): Items<Firewall> = httpClient.makeRequest(Route.GET_ALL_FIREWALLS, queryParams = filter.toQueryParams() + sorting.toQueryParams() + pagination.toQueryParams())

    @Throws(Failure::class)
    suspend fun find(id: Id): Item<Firewall> = httpClient.makeRequest(Route.GET_FIREWALL, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun create(body: CreateFirewall): FirewallCreated = httpClient.makeRequest(Route.CREATE_FIREWALL, body = body)

    @Throws(Failure::class)
    suspend fun update(id: Id, body: UpdateResource): Item<Firewall> = httpClient.makeRequest(Route.UPDATE_FIREWALL, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun delete(id: Id): Unit = httpClient.makeRequest(Route.DELETE_FIREWALL, resourceId = id.value)
}
