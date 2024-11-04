package tech.sco.hetznerkloud.repository

import io.ktor.client.HttpClient
import tech.sco.hetznerkloud.Route
import tech.sco.hetznerkloud.makeRequest
import tech.sco.hetznerkloud.model.Error
import tech.sco.hetznerkloud.model.LoadBalancerType
import tech.sco.hetznerkloud.request.LoadBalancerTypeFilter
import tech.sco.hetznerkloud.request.Pagination
import tech.sco.hetznerkloud.request.toQueryParams
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.Items

class LoadBalancerTypes(private val httpClient: HttpClient) {
    @Throws(Error::class)
    suspend fun all(
        filter: Set<LoadBalancerTypeFilter> = emptySet(),
        pagination: Pagination = Pagination(),
    ): Items<LoadBalancerType> =
        httpClient.makeRequest(
            Route.GET_ALL_LOAD_BALANCER_TYPES,
            queryParams = filter.toQueryParams() + pagination.toQueryParams(),
        )

    @Throws(Error::class)
    suspend fun find(id: LoadBalancerType.Id): Item<LoadBalancerType> = httpClient.makeRequest(Route.GET_LOAD_BALANCER_TYPE, resourceId = id)
}
