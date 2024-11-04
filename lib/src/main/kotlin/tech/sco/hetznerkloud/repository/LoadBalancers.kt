package tech.sco.hetznerkloud.repository

import io.ktor.client.HttpClient
import tech.sco.hetznerkloud.Route
import tech.sco.hetznerkloud.makeRequest
import tech.sco.hetznerkloud.model.Error
import tech.sco.hetznerkloud.model.LoadBalancer
import tech.sco.hetznerkloud.model.LoadBalancer.Id
import tech.sco.hetznerkloud.request.CreateLoadBalancer
import tech.sco.hetznerkloud.request.LoadBalancerFilter
import tech.sco.hetznerkloud.request.LoadBalancerSorting
import tech.sco.hetznerkloud.request.Pagination
import tech.sco.hetznerkloud.request.UpdateLoadBalancer
import tech.sco.hetznerkloud.request.toQueryParams
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.Items
import tech.sco.hetznerkloud.response.LoadBalancerCreated

class LoadBalancers(private val httpClient: HttpClient) {

    @Throws(Error::class)
    suspend fun all(
        filter: Set<LoadBalancerFilter> = emptySet(),
        sorting: Set<LoadBalancerSorting> = emptySet(),
        pagination: Pagination = Pagination(),
    ): Items<LoadBalancer> = httpClient.makeRequest(
        Route.GET_ALL_LOAD_BALANCERS,
        queryParams = filter.toQueryParams() + sorting.toQueryParams() + pagination.toQueryParams(),
    )

    @Throws(Error::class)
    suspend fun find(id: Id): Item<LoadBalancer> = httpClient.makeRequest(Route.GET_LOAD_BALANCER, resourceId = id)

    @Throws(Error::class)
    suspend fun create(body: CreateLoadBalancer): LoadBalancerCreated = httpClient.makeRequest(Route.CREATE_LOAD_BALANCER, body = body)

    @Throws(Error::class)
    suspend fun update(id: Id, body: UpdateLoadBalancer): Item<LoadBalancer> = httpClient.makeRequest(Route.UPDATE_LOAD_BALANCER, resourceId = id, body = body)

    @Throws(Error::class)
    suspend fun delete(id: Id): Unit = httpClient.makeRequest(Route.DELETE_LOAD_BALANCER, resourceId = id)
}