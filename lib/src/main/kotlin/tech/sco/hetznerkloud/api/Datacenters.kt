package tech.sco.hetznerkloud.api

import io.ktor.client.HttpClient
import tech.sco.hetznerkloud.Failure
import tech.sco.hetznerkloud.InternalAPI
import tech.sco.hetznerkloud.Route
import tech.sco.hetznerkloud.makeRequest
import tech.sco.hetznerkloud.model.Datacenter
import tech.sco.hetznerkloud.model.Datacenter.Id
import tech.sco.hetznerkloud.request.DatacenterFilter
import tech.sco.hetznerkloud.request.DatacenterSorting
import tech.sco.hetznerkloud.request.Pagination
import tech.sco.hetznerkloud.request.toQueryParams
import tech.sco.hetznerkloud.response.DatacenterItems
import tech.sco.hetznerkloud.response.Item

class Datacenters @InternalAPI constructor(private val httpClient: HttpClient) {

    @Throws(Failure::class)
    suspend fun all(filter: Set<DatacenterFilter> = emptySet(), sorting: Set<DatacenterSorting> = emptySet(), pagination: Pagination = Pagination()): DatacenterItems =
        httpClient.makeRequest(Route.GET_ALL_DATACENTERS, queryParams = filter.toQueryParams() + sorting.toQueryParams() + pagination.toQueryParams())

    @Throws(Failure::class)
    suspend fun find(id: Id): Item<Datacenter> = httpClient.makeRequest(Route.GET_DATACENTER, id.value)
}
