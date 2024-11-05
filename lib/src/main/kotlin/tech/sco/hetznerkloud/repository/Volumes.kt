package tech.sco.hetznerkloud.repository

import io.ktor.client.HttpClient
import tech.sco.hetznerkloud.Route
import tech.sco.hetznerkloud.makeRequest
import tech.sco.hetznerkloud.model.Error
import tech.sco.hetznerkloud.model.Volume
import tech.sco.hetznerkloud.model.Volume.Id
import tech.sco.hetznerkloud.request.CreateVolume
import tech.sco.hetznerkloud.request.Pagination
import tech.sco.hetznerkloud.request.UpdateResource
import tech.sco.hetznerkloud.request.VolumeFilter
import tech.sco.hetznerkloud.request.VolumeSorting
import tech.sco.hetznerkloud.request.toQueryParams
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.ItemCreated
import tech.sco.hetznerkloud.response.Items

class Volumes(private val httpClient: HttpClient) {
    @Throws(Error::class)
    suspend fun all(
        filter: Set<VolumeFilter> = emptySet(),
        sorting: Set<VolumeSorting> = emptySet(),
        pagination: Pagination = Pagination(),
    ): Items<Volume> = httpClient.makeRequest(Route.GET_ALL_VOLUMES, queryParams = filter.toQueryParams() + sorting.toQueryParams() + pagination.toQueryParams())

    @Throws(Error::class)
    suspend fun find(id: Id): Item<Volume> = httpClient.makeRequest(Route.GET_VOLUME, resourceId = id.value)

    @Throws(Error::class)
    suspend fun create(body: CreateVolume): ItemCreated<Volume> = httpClient.makeRequest(Route.CREATE_VOLUME, body = body)

    @Throws(Error::class)
    suspend fun update(id: Id, body: UpdateResource): Item<Volume> = httpClient.makeRequest(Route.UPDATE_VOLUME, resourceId = id.value, body = body)

    @Throws(Error::class)
    suspend fun delete(id: Id): Unit = httpClient.makeRequest(Route.DELETE_VOLUME, resourceId = id.value)
}
