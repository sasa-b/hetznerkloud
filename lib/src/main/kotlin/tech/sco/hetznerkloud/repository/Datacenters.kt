package tech.sco.hetznerkloud.repository

import io.ktor.client.HttpClient
import tech.sco.hetznerkloud.Route
import tech.sco.hetznerkloud.makeRequest
import tech.sco.hetznerkloud.model.Error
import tech.sco.hetznerkloud.response.DatacenterItem
import tech.sco.hetznerkloud.response.DatacenterList

class Datacenters(private val httpClient: HttpClient) {
    @Throws(Error::class)
    suspend fun all(): DatacenterList = httpClient.makeRequest(Route.GET_ALL_DATACENTERS)

    @Throws(Error::class)
    suspend fun find(id: Long): DatacenterItem = httpClient.makeRequest(Route.GET_DATACENTER, id)
}
