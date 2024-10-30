package tech.sco.hetznerkloud.repository

import io.ktor.client.HttpClient
import tech.sco.hetznerkloud.Route
import tech.sco.hetznerkloud.makeRequest
import tech.sco.hetznerkloud.model.Error
import tech.sco.hetznerkloud.response.ActionItem
import tech.sco.hetznerkloud.response.ActionList

class Actions(private val httpClient: HttpClient) {

    @Throws(Error::class)
    suspend fun all(): ActionList = httpClient.makeRequest(Route.GET_ALL_ACTIONS)

    @Throws(Error::class)
    suspend fun find(id: Long): ActionItem = httpClient.makeRequest(Route.GET_ACTION, id)
}
