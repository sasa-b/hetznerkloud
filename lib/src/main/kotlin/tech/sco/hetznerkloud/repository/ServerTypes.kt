package tech.sco.hetznerkloud.repository

import io.ktor.client.HttpClient
import tech.sco.hetznerkloud.Route
import tech.sco.hetznerkloud.makeRequest
import tech.sco.hetznerkloud.model.Error
import tech.sco.hetznerkloud.response.ServerTypeItem
import tech.sco.hetznerkloud.response.ServerTypeList

class ServerTypes(private val httpClient: HttpClient) {
    @Throws(Error::class)
    suspend fun all(): ServerTypeList = httpClient.makeRequest(Route.GET_ALL_SERVER_TYPES)

    @Throws(Error::class)
    suspend fun find(id: Long): ServerTypeItem = httpClient.makeRequest(Route.GET_SERVER_TYPE, id)
}