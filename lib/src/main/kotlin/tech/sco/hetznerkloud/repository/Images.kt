package tech.sco.hetznerkloud.repository

import io.ktor.client.HttpClient
import tech.sco.hetznerkloud.Route
import tech.sco.hetznerkloud.makeRequest
import tech.sco.hetznerkloud.model.Error
import tech.sco.hetznerkloud.request.UpdateImage
import tech.sco.hetznerkloud.response.ImageItem
import tech.sco.hetznerkloud.response.ImageList

class Images(private val httpClient: HttpClient) {
    @Throws(Error::class)
    suspend fun all(): ImageList = httpClient.makeRequest(Route.GET_ALL_IMAGES)

    @Throws(Error::class)
    suspend fun find(id: Long): ImageItem = httpClient.makeRequest(Route.GET_IMAGE, id)

    @Throws(Error::class)
    suspend fun update(id: Long, body: UpdateImage): ImageItem = httpClient.makeRequest(Route.UPDATE_IMAGE, id, body)

    @Throws(Error::class)
    suspend fun delete(id: Long): Unit = httpClient.makeRequest(Route.DELETE_IMAGE, id)
}
