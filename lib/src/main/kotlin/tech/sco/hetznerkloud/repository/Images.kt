package tech.sco.hetznerkloud.repository

import io.ktor.client.HttpClient
import io.ktor.utils.io.InternalAPI
import tech.sco.hetznerkloud.Failure
import tech.sco.hetznerkloud.Route
import tech.sco.hetznerkloud.makeRequest
import tech.sco.hetznerkloud.model.Image
import tech.sco.hetznerkloud.model.Image.Id
import tech.sco.hetznerkloud.request.ImageFilter
import tech.sco.hetznerkloud.request.ImageSorting
import tech.sco.hetznerkloud.request.Pagination
import tech.sco.hetznerkloud.request.UpdateImage
import tech.sco.hetznerkloud.request.toQueryParams
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.Items

class Images @InternalAPI constructor(private val httpClient: HttpClient) {

    @Throws(Failure::class)
    suspend fun all(
        filter: Set<ImageFilter> = emptySet(),
        sorting: Set<ImageSorting> = emptySet(),
        pagination: Pagination = Pagination(),
    ): Items<Image> =
        httpClient.makeRequest(Route.GET_ALL_IMAGES, queryParams = filter.toQueryParams() + sorting.toQueryParams() + pagination.toQueryParams())

    @Throws(Failure::class)
    suspend fun find(id: Id): Item<Image> = httpClient.makeRequest(Route.GET_IMAGE, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun update(id: Id, body: UpdateImage): Item<Image> = httpClient.makeRequest(Route.UPDATE_IMAGE, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun delete(id: Id): Unit = httpClient.makeRequest(Route.DELETE_IMAGE, resourceId = id.value)
}
