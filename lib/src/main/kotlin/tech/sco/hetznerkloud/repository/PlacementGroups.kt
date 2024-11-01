package tech.sco.hetznerkloud.repository

import io.ktor.client.HttpClient
import tech.sco.hetznerkloud.Route
import tech.sco.hetznerkloud.makeRequest
import tech.sco.hetznerkloud.model.Error
import tech.sco.hetznerkloud.model.PlacementGroup.Id
import tech.sco.hetznerkloud.request.CreatePlacementGroup
import tech.sco.hetznerkloud.request.UpdatePlacementGroup
import tech.sco.hetznerkloud.response.PlacementGroupItem
import tech.sco.hetznerkloud.response.PlacementGroupList

class PlacementGroups(private val httpClient: HttpClient) {
    @Throws(Error::class)
    suspend fun all(): PlacementGroupList = httpClient.makeRequest(Route.GET_ALL_PLACEMENT_GROUPS)

    @Throws(Error::class)
    suspend fun find(id: Id): PlacementGroupItem = httpClient.makeRequest(Route.GET_A_PLACEMENT_GROUP, id)

    @Throws(Error::class)
    suspend fun create(body: CreatePlacementGroup): PlacementGroupItem = httpClient.makeRequest(Route.CREATE_PLACEMENT_GROUP, body = body)

    suspend fun update(id: Id, body: UpdatePlacementGroup): PlacementGroupItem = httpClient.makeRequest(Route.UPDATE_PLACEMENT_GROUP, resourceId = id, body = body)

    @Throws(Error::class)
    suspend fun delete(id: Id): Unit = httpClient.makeRequest(Route.DELETE_PLACEMENT_GROUP, id)
}
