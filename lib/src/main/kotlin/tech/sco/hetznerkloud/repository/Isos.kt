package tech.sco.hetznerkloud.repository

import io.ktor.client.HttpClient
import tech.sco.hetznerkloud.Route
import tech.sco.hetznerkloud.makeRequest
import tech.sco.hetznerkloud.model.Error
import tech.sco.hetznerkloud.response.IsoItem
import tech.sco.hetznerkloud.response.IsoList

class Isos(private val httpClient: HttpClient) {
    @Throws(Error::class)
    suspend fun all(): IsoList = httpClient.makeRequest(Route.GET_ALL_ISOS)

    @Throws(Error::class)
    suspend fun find(id: Long): IsoItem = httpClient.makeRequest(Route.GET_ISO, id)
}
