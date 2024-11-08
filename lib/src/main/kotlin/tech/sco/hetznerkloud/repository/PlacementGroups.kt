package tech.sco.hetznerkloud.repository

import io.ktor.client.HttpClient
import io.ktor.utils.io.InternalAPI
import tech.sco.hetznerkloud.Route
import tech.sco.hetznerkloud.makeRequest
import tech.sco.hetznerkloud.model.PlacementGroup
import tech.sco.hetznerkloud.model.PlacementGroup.Id
import tech.sco.hetznerkloud.request.CreatePlacementGroup
import tech.sco.hetznerkloud.request.UpdateResource
import tech.sco.hetznerkloud.response.Failure
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.Items

class PlacementGroups @InternalAPI constructor(private val httpClient: HttpClient) {

    @Throws(Failure::class)
    suspend fun all(): Items<PlacementGroup> = httpClient.makeRequest(Route.GET_ALL_PLACEMENT_GROUPS)

    @Throws(Failure::class)
    suspend fun find(id: Id): Item<PlacementGroup> = httpClient.makeRequest(Route.GET_A_PLACEMENT_GROUP, id.value)

    @Throws(Failure::class)
    suspend fun create(body: CreatePlacementGroup): Item<PlacementGroup> = httpClient.makeRequest(Route.CREATE_PLACEMENT_GROUP, body = body)

    suspend fun update(id: Id, body: UpdateResource): Item<PlacementGroup> = httpClient.makeRequest(Route.UPDATE_PLACEMENT_GROUP, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun delete(id: Id): Unit = httpClient.makeRequest(Route.DELETE_PLACEMENT_GROUP, id.value)
}
