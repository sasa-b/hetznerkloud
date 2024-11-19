package tech.sco.hetznerkloud.api

import io.ktor.client.HttpClient
import io.ktor.utils.io.InternalAPI
import tech.sco.hetznerkloud.Failure
import tech.sco.hetznerkloud.Route
import tech.sco.hetznerkloud.makeRequest
import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.Server
import tech.sco.hetznerkloud.model.Server.Id
import tech.sco.hetznerkloud.model.ServerMetrics
import tech.sco.hetznerkloud.request.AddToPlacementGroup
import tech.sco.hetznerkloud.request.AttachIsoById
import tech.sco.hetznerkloud.request.AttachIsoByName
import tech.sco.hetznerkloud.request.AttachToNetwork
import tech.sco.hetznerkloud.request.ChangeServerType
import tech.sco.hetznerkloud.request.CreateImageFromServer
import tech.sco.hetznerkloud.request.CreateServer
import tech.sco.hetznerkloud.request.DetachFromNetwork
import tech.sco.hetznerkloud.request.EnableRescueMode
import tech.sco.hetznerkloud.request.FilterFields
import tech.sco.hetznerkloud.request.Pagination
import tech.sco.hetznerkloud.request.RebuildFromImageById
import tech.sco.hetznerkloud.request.RebuildFromImageByName
import tech.sco.hetznerkloud.request.ServerActionFilter
import tech.sco.hetznerkloud.request.ServerFilter
import tech.sco.hetznerkloud.request.ServerMetricsFilter
import tech.sco.hetznerkloud.request.ServerSorting
import tech.sco.hetznerkloud.request.UpdateResource
import tech.sco.hetznerkloud.request.toQueryParams
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.Items
import tech.sco.hetznerkloud.response.ServerActionWithImage
import tech.sco.hetznerkloud.response.ServerActionWithRootPassword
import tech.sco.hetznerkloud.response.ServerConsoleRequestedAction
import tech.sco.hetznerkloud.response.ServerCreated
import tech.sco.hetznerkloud.response.ServerDeleted

class Servers @InternalAPI constructor(private val httpClient: HttpClient) {

    @Throws(Failure::class)
    suspend fun all(
        filter: Set<ServerFilter> = emptySet(),
        sorting: Set<ServerSorting> = emptySet(),
        pagination: Pagination = Pagination(),
    ): Items<Server> = httpClient.makeRequest(
        Route.GET_ALL_SERVERS,
        queryParams = filter.toQueryParams() + sorting.toQueryParams() + pagination.toQueryParams(),
    )

    @Throws(Failure::class)
    suspend fun find(id: Id): Item<Server> = httpClient.makeRequest(Route.GET_SERVER, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun create(body: CreateServer): ServerCreated = httpClient.makeRequest(Route.CREATE_SERVER, body = body)

    @Throws(Failure::class)
    suspend fun update(id: Id, body: UpdateResource): Item<Server> = httpClient.makeRequest(Route.UPDATE_SERVER, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun delete(id: Id): ServerDeleted = httpClient.makeRequest(Route.DELETE_SERVER, id.value)

    @Throws(Failure::class)
    suspend fun metrics(id: Id, filter: Set<ServerMetricsFilter>): Item<ServerMetrics> {
        val (types, other) = filter.partition { it.first == FilterFields.ServerMetrics.TYPE }

        return httpClient.makeRequest(
            Route.GET_SERVER_METRICS,
            id.value,
            queryParams = listOf(
                Pair("type", types.joinToString(",") { it.second }),
            ) + other.toQueryParams(),
        )
    }

    @Throws(Failure::class)
    suspend fun actions(
        filter: Set<ServerActionFilter> = emptySet(),
        sorting: Set<ServerActionFilter> = emptySet(),
        pagination: Pagination = Pagination(),
    ): Items<Action> = httpClient.makeRequest(
        Route.GET_ALL_SERVER_ACTIONS,
        queryParams = filter.toQueryParams() + sorting.toQueryParams() + pagination.toQueryParams(),
    )

    @Throws(Failure::class)
    suspend fun actions(
        serverId: Id,
        filter: Set<ServerActionFilter> = emptySet(),
        sorting: Set<ServerActionFilter> = emptySet(),
        pagination: Pagination = Pagination(),
    ): Items<Action> = httpClient.makeRequest(
        Route.GET_SERVER_ACTIONS,
        resourceId = serverId.value,
        queryParams = filter.toQueryParams() + sorting.toQueryParams() + pagination.toQueryParams(),
    )

    @Throws(Failure::class)
    suspend fun action(actionId: Action.Id): Item<Action> = httpClient.makeRequest(Route.GET_SERVER_ACTION, resourceId = actionId.value)

    @Throws(Failure::class)
    suspend fun action(serverId: Id, actionId: Action.Id): Item<Action> = httpClient.makeRequest(Route.GET_SERVER_ACTION, routeParams = mapOf("id" to serverId.toString(), "action_id" to actionId.toString()))

    @Throws(Failure::class)
    suspend fun addToPlacementGroup(id: Id, body: AddToPlacementGroup): Item<Action> = httpClient.makeRequest(Route.ADD_SERVER_TO_PLACEMENT_GROUP, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun attachIso(id: Id, body: AttachIsoByName): Item<Action> = httpClient.makeRequest(Route.ATTACH_ISO_TO_SERVER, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun attachIso(id: Id, body: AttachIsoById): Item<Action> = httpClient.makeRequest(Route.ATTACH_ISO_TO_SERVER, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun detachIso(id: Id): Item<Action> = httpClient.makeRequest(Route.DETACH_ISO_FROM_SERVER, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun attachToNetwork(id: Id, body: AttachToNetwork): Item<Action> = httpClient.makeRequest(Route.ATTACH_SERVER_TO_NETWORK, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun detachFromNetwork(id: Id, body: DetachFromNetwork): Item<Action> = httpClient.makeRequest(Route.DETACH_SERVER_FROM_NETWORK, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun shutdown(id: Id): Item<Action> = httpClient.makeRequest(Route.SHUTDOWN_SERVER, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun reset(id: Id): Item<Action> = httpClient.makeRequest(Route.RESET_SERVER, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun powerOn(id: Id): Item<Action> = httpClient.makeRequest(Route.POWER_ON_SERVER, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun powerOff(id: Id): Item<Action> = httpClient.makeRequest(Route.POWER_OFF_SERVER, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun softReboot(id: Id): Item<Action> = httpClient.makeRequest(Route.SOFT_REBOOT_SERVER, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun resetRootPassword(id: Id): ServerActionWithRootPassword = httpClient.makeRequest(Route.RESET_SERVER_ROOT_PASSWORD, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun requestConsole(id: Id): ServerConsoleRequestedAction = httpClient.makeRequest(Route.REQUEST_CONSOLE_FOR_SERVER, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun removeFromPlacementGroup(id: Id): Item<Action> = httpClient.makeRequest(Route.REMOVE_SERVER_FROM_PLACEMENT_GROUP, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun rebuildFromImage(id: Id, body: RebuildFromImageById): ServerActionWithRootPassword = httpClient.makeRequest(Route.REBUILD_SERVER_FROM_IMAGE, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun rebuildFromImage(id: Id, body: RebuildFromImageByName): ServerActionWithRootPassword = httpClient.makeRequest(Route.REBUILD_SERVER_FROM_IMAGE, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun enableBackup(id: Id): Item<Action> = httpClient.makeRequest(Route.ENABLE_SERVER_BACKUP, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun disableBackup(id: Id): Item<Action> = httpClient.makeRequest(Route.DISABLE_SERVER_BACKUP, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun enableRescueMode(id: Id, body: EnableRescueMode): ServerActionWithRootPassword = httpClient.makeRequest(Route.ENABLE_SERVER_RESCUE_MODE, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun disableRescueMode(id: Id): Item<Action> = httpClient.makeRequest(Route.DISABLE_SERVER_RESCUE_MODE, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun createImage(id: Id, body: CreateImageFromServer): ServerActionWithImage = httpClient.makeRequest(Route.CREATE_IMAGE_FROM_SERVER, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun changeType(id: Id, body: ChangeServerType): Item<Action> = httpClient.makeRequest(Route.CHANGE_SERVER_TYPE, resourceId = id.value, body = body)
}
