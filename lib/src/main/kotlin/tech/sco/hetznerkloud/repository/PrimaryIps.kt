package tech.sco.hetznerkloud.repository

import io.ktor.client.HttpClient
import io.ktor.utils.io.InternalAPI
import tech.sco.hetznerkloud.Route
import tech.sco.hetznerkloud.makeRequest
import tech.sco.hetznerkloud.model.PrimaryIp
import tech.sco.hetznerkloud.model.PrimaryIp.Id
import tech.sco.hetznerkloud.request.Pagination
import tech.sco.hetznerkloud.request.PrimaryIpFilter
import tech.sco.hetznerkloud.request.PrimaryIpSorting
import tech.sco.hetznerkloud.request.toQueryParams
import tech.sco.hetznerkloud.response.Failure
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.Items

class PrimaryIps @InternalAPI constructor(private val httpClient: HttpClient) {

    @Throws(Failure::class)
    suspend fun all(
        filter: Set<PrimaryIpFilter> = emptySet(),
        sorting: Set<PrimaryIpSorting> = emptySet(),
        pagination: Pagination = Pagination(),
    ): Items<PrimaryIp> = httpClient.makeRequest(Route.GET_ALL_PRIMARY_IPS, queryParams = filter.toQueryParams() + sorting.toQueryParams() + pagination.toQueryParams())

    @Throws(Failure::class)
    suspend fun find(id: Id): Item<PrimaryIp> = httpClient.makeRequest(Route.GET_PRIMARY_IP, resourceId = id.value)
}
