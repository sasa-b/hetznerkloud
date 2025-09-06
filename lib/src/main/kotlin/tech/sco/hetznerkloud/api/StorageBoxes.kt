package tech.sco.hetznerkloud.api

import io.ktor.client.HttpClient
import tech.sco.hetznerkloud.Failure
import tech.sco.hetznerkloud.InternalAPI
import tech.sco.hetznerkloud.Route
import tech.sco.hetznerkloud.makeRequest
import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.Server
import tech.sco.hetznerkloud.model.StorageBox
import tech.sco.hetznerkloud.model.StorageBox.Id
import tech.sco.hetznerkloud.request.CreateStorageBox
import tech.sco.hetznerkloud.request.Pagination
import tech.sco.hetznerkloud.request.StorageBoxFilter
import tech.sco.hetznerkloud.request.UpdateResource
import tech.sco.hetznerkloud.request.toQueryParams
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.ItemCreated
import tech.sco.hetznerkloud.response.ItemDeleted
import tech.sco.hetznerkloud.response.Items

class StorageBoxes @InternalAPI constructor(private val httpClient: HttpClient) {
    @Throws(Failure::class)
    suspend fun all(filter: Set<StorageBoxFilter> = emptySet(), pagination: Pagination = Pagination()): Items<StorageBox> = httpClient.makeRequest(
        Route.GET_ALL_STORAGE_BOXES,
        queryParams = filter.toQueryParams() + pagination.toQueryParams(),
    )

    @Throws(Failure::class)
    suspend fun find(id: Id): Item<StorageBox> = httpClient.makeRequest(Route.GET_STORAGE_BOX, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun create(body: CreateStorageBox): ItemCreated<StorageBox> = httpClient.makeRequest(Route.CREATE_STORAGE_BOX, body = body)

    @Throws(Failure::class)
    suspend fun update(id: Id, body: UpdateResource): Item<StorageBox> = httpClient.makeRequest(Route.UPDATE_STORAGE_BOX, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun delete(id: Id): ItemDeleted = httpClient.makeRequest(Route.DELETE_STORAGE_BOX, resourceId = id.value)
}
