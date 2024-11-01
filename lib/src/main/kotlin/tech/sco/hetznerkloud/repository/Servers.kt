package tech.sco.hetznerkloud.repository

import io.ktor.client.HttpClient
import tech.sco.hetznerkloud.Route
import tech.sco.hetznerkloud.makeRequest
import tech.sco.hetznerkloud.model.Error
import tech.sco.hetznerkloud.model.ServerMetrics.Type
import tech.sco.hetznerkloud.request.CreateServer
import tech.sco.hetznerkloud.request.UpdateServer
import tech.sco.hetznerkloud.response.ServerCreated
import tech.sco.hetznerkloud.response.ServerDeleted
import tech.sco.hetznerkloud.response.ServerItem
import tech.sco.hetznerkloud.response.ServerList
import tech.sco.hetznerkloud.response.ServerMetrics
import tech.sco.hetznerkloud.response.ServerUpdated

class Servers(private val httpClient: HttpClient) {
    @Throws(Error::class)
    suspend fun all(): ServerList = httpClient.makeRequest(Route.GET_ALL_SERVERS)

    @Throws(Error::class)
    suspend fun find(id: Long): ServerItem = httpClient.makeRequest(Route.GET_SERVER, id)

    @Throws(Error::class)
    suspend fun create(body: CreateServer): ServerCreated = httpClient.makeRequest(Route.CREATE_SERVER, body = body)

    @Throws(Error::class)
    suspend fun update(id: Long, body: UpdateServer): ServerUpdated = httpClient.makeRequest(Route.UPDATE_SERVER, id, body)

    @Throws(Error::class)
    suspend fun delete(id: Long): ServerDeleted = httpClient.makeRequest(Route.DELETE_SERVER, id)

    @Throws(Error::class)
    suspend fun metrics(id: Long, type: Set<Type>): ServerMetrics = httpClient.makeRequest(Route.GET_SERVER_METRICS, id, queryParams = mapOf("type" to listOf(type.joinToString(","))))
}