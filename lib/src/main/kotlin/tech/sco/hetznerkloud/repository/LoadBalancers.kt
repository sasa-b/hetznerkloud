package tech.sco.hetznerkloud.repository

import io.ktor.client.HttpClient
import io.ktor.utils.io.InternalAPI
import tech.sco.hetznerkloud.Failure
import tech.sco.hetznerkloud.Route
import tech.sco.hetznerkloud.makeRequest
import tech.sco.hetznerkloud.model.LoadBalancer
import tech.sco.hetznerkloud.model.LoadBalancer.Id
import tech.sco.hetznerkloud.request.CreateLoadBalancer
import tech.sco.hetznerkloud.request.LoadBalancerFilter
import tech.sco.hetznerkloud.request.LoadBalancerSorting
import tech.sco.hetznerkloud.request.Pagination
import tech.sco.hetznerkloud.request.UpdateResource
import tech.sco.hetznerkloud.request.toQueryParams
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.ItemCreated
import tech.sco.hetznerkloud.response.Items

class LoadBalancers @InternalAPI constructor(private val httpClient: HttpClient) {

    @Throws(Failure::class)
    suspend fun all(
        filter: Set<LoadBalancerFilter> = emptySet(),
        sorting: Set<LoadBalancerSorting> = emptySet(),
        pagination: Pagination = Pagination(),
    ): Items<LoadBalancer> = httpClient.makeRequest(
        Route.GET_ALL_LOAD_BALANCERS,
        queryParams = filter.toQueryParams() + sorting.toQueryParams() + pagination.toQueryParams(),
    )

    @Throws(Failure::class)
    suspend fun find(id: Id): Item<LoadBalancer> = httpClient.makeRequest(Route.GET_LOAD_BALANCER, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun create(body: CreateLoadBalancer): ItemCreated<LoadBalancer> = httpClient.makeRequest(Route.CREATE_LOAD_BALANCER, body = body)

    @Throws(Failure::class)
    suspend fun update(id: Id, body: UpdateResource): Item<LoadBalancer> = httpClient.makeRequest(Route.UPDATE_LOAD_BALANCER, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun delete(id: Id): Unit = httpClient.makeRequest(Route.DELETE_LOAD_BALANCER, resourceId = id.value)
}
