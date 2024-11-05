package tech.sco.hetznerkloud.repository

import io.ktor.client.HttpClient
import tech.sco.hetznerkloud.Route
import tech.sco.hetznerkloud.makeRequest
import tech.sco.hetznerkloud.model.Error
import tech.sco.hetznerkloud.model.Firewall
import tech.sco.hetznerkloud.model.Firewall.Id
import tech.sco.hetznerkloud.request.FirewallFilter
import tech.sco.hetznerkloud.request.FirewallSorting
import tech.sco.hetznerkloud.request.Pagination
import tech.sco.hetznerkloud.request.toQueryParams
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.Items

class Firewalls(private val httpClient: HttpClient) {

    @Throws(Error::class)
    suspend fun all(
        filter: Set<FirewallFilter> = emptySet(),
        sorting: Set<FirewallSorting> = emptySet(),
        pagination: Pagination = Pagination(),
    ): Items<Firewall> = httpClient.makeRequest(Route.GET_ALL_FIREWALLS, queryParams = filter.toQueryParams() + sorting.toQueryParams() + pagination.toQueryParams())

    @Throws(Error::class)
    suspend fun find(id: Id): Item<Firewall> = httpClient.makeRequest(Route.GET_FIREWALL, resourceId = id.value)

    @Throws(Error::class)
    suspend fun delete(id: Id): Unit = httpClient.makeRequest(Route.DELETE_FIREWALL, resourceId = id.value)
}
