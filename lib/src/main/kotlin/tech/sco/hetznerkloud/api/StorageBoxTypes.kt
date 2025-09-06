package tech.sco.hetznerkloud.api

import io.ktor.client.HttpClient
import tech.sco.hetznerkloud.Failure
import tech.sco.hetznerkloud.InternalAPI
import tech.sco.hetznerkloud.Route
import tech.sco.hetznerkloud.makeRequest
import tech.sco.hetznerkloud.model.StorageBoxType
import tech.sco.hetznerkloud.model.StorageBoxType.Id
import tech.sco.hetznerkloud.request.Pagination
import tech.sco.hetznerkloud.request.StorageBoxTypeFilter
import tech.sco.hetznerkloud.request.toQueryParams
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.Items

class StorageBoxTypes @InternalAPI constructor(private val httpClient: HttpClient) {
    @Throws(Failure::class)
    suspend fun all(filter: Set<StorageBoxTypeFilter> = emptySet(), pagination: Pagination = Pagination()): Items<StorageBoxType> = httpClient.makeRequest(
        Route.GET_ALL_STORAGE_BOX_TYPES,
        queryParams = filter.toQueryParams() + pagination.toQueryParams(),
    )

    @Throws(Failure::class)
    suspend fun find(id: Id): Item<StorageBoxType> = httpClient.makeRequest(Route.GET_STORAGE_BOX_TYPE, resourceId = id.value)
}
