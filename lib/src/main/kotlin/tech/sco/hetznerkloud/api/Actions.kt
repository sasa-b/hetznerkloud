package tech.sco.hetznerkloud.api

import io.ktor.client.HttpClient
import tech.sco.hetznerkloud.Failure
import tech.sco.hetznerkloud.InternalAPI
import tech.sco.hetznerkloud.Route
import tech.sco.hetznerkloud.makeRequest
import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.Certificate
import tech.sco.hetznerkloud.model.Firewall
import tech.sco.hetznerkloud.model.FloatingIp
import tech.sco.hetznerkloud.model.Image
import tech.sco.hetznerkloud.model.LoadBalancer
import tech.sco.hetznerkloud.model.Network
import tech.sco.hetznerkloud.model.PrimaryIp
import tech.sco.hetznerkloud.model.ResourceId
import tech.sco.hetznerkloud.model.ResourceType
import tech.sco.hetznerkloud.model.Server
import tech.sco.hetznerkloud.model.StorageBox
import tech.sco.hetznerkloud.model.Volume
import tech.sco.hetznerkloud.request.ActionFilter
import tech.sco.hetznerkloud.request.ActionSorting
import tech.sco.hetznerkloud.request.Pagination
import tech.sco.hetznerkloud.request.toQueryParams
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.Items

class Actions @InternalAPI constructor(private val httpClient: HttpClient) {

    @Throws(Failure::class)
    suspend fun all(filter: Set<ActionFilter> = emptySet(), sorting: Set<ActionSorting> = emptySet(), pagination: Pagination = Pagination()): Items<Action> =
        httpClient.makeRequest(Route.GET_ALL_ACTIONS, queryParams = (filter.toQueryParams() + sorting.toQueryParams() + pagination.toQueryParams()))

    @Throws(Failure::class)
    suspend fun all(
        resourceType: ResourceType,
        filter: Set<ActionFilter> = emptySet(),
        sorting: Set<ActionFilter> = emptySet(),
        pagination: Pagination = Pagination(),
    ): Items<Action> = httpClient.makeRequest(
        resourceType.toRoute(null),
        queryParams = filter.toQueryParams() + sorting.toQueryParams() + pagination.toQueryParams(),
    )

    @Throws(Failure::class, InvalidResourceId::class)
    suspend fun <T : ResourceId> all(
        resourceId: T,
        filter: Set<ActionFilter> = emptySet(),
        sorting: Set<ActionFilter> = emptySet(),
        pagination: Pagination = Pagination(),
    ): Items<Action> = httpClient.makeRequest(
        resourceId.toRoute(null),
        resourceId = resourceId.value,
        queryParams = filter.toQueryParams() + sorting.toQueryParams() + pagination.toQueryParams(),
    )

    @Throws(Failure::class)
    suspend fun find(id: Action.Id): Item<Action> = httpClient.makeRequest(Route.GET_ACTION, id.value)

    @Throws(Failure::class)
    suspend fun find(resourceType: ResourceType, actionId: Action.Id): Item<Action> = httpClient.makeRequest(
        resourceType.toRoute(actionId),
        mapOf("id" to actionId.asString()),
    )

    @Throws(Failure::class, InvalidResourceId::class)
    suspend fun <T : ResourceId> find(resourceId: T, actionId: Action.Id): Item<Action> = httpClient.makeRequest(
        resourceId.toRoute(actionId),
        routeParams = mapOf(
            "id" to resourceId.asString(),
            "action_id" to actionId.asString(),
        ),
    )
}

private fun ResourceType.toRoute(actionId: Action.Id?) = when (this) {
    ResourceType.SERVER if actionId != null -> Route.GET_SERVER_ACTION
    ResourceType.NETWORK if actionId != null -> Route.GET_NETWORK_ACTION
    ResourceType.IMAGE if actionId != null -> Route.GET_IMAGE_ACTION
    ResourceType.LOAD_BALANCER if actionId != null -> Route.GET_LOAD_BALANCER_ACTION
    ResourceType.VOLUME if actionId != null -> Route.GET_VOLUME_ACTION
    ResourceType.CERTIFICATE if actionId != null -> Route.GET_CERTIFICATE_ACTION
    ResourceType.FIREWALL if actionId != null -> Route.GET_FIREWALL_ACTION
    ResourceType.PRIMARY_IP if actionId != null -> Route.GET_PRIMARY_IP_ACTION
    ResourceType.FLOATING_IP if actionId != null -> Route.GET_FLOATING_IP_ACTION
    ResourceType.STORAGE_BOX if actionId != null -> Route.GET_STORAGE_BOX_ACTION

    ResourceType.SERVER -> Route.GET_ALL_SERVER_ACTIONS
    ResourceType.NETWORK -> Route.GET_ALL_NETWORK_ACTIONS
    ResourceType.IMAGE -> Route.GET_ALL_IMAGE_ACTIONS
    ResourceType.LOAD_BALANCER -> Route.GET_ALL_LOAD_BALANCER_ACTIONS
    ResourceType.VOLUME -> Route.GET_ALL_VOLUME_ACTIONS
    ResourceType.CERTIFICATE -> Route.GET_ALL_CERTIFICATE_ACTIONS
    ResourceType.FIREWALL -> Route.GET_ALL_FIREWALL_ACTIONS
    ResourceType.PRIMARY_IP -> Route.GET_ALL_PRIMARY_IP_ACTIONS
    ResourceType.FLOATING_IP -> Route.GET_ALL_FLOATING_IP_ACTIONS
    ResourceType.STORAGE_BOX -> Route.GET_ALL_STORAGE_BOX_ACTIONS

    else -> throw InvalidResourceType(this)
}

private fun ResourceId.toRoute(actionId: Action.Id?) = when (this) {
    is Server.Id if actionId != null -> Route.GET_SERVER_ACTION_FOR_SERVER
    is Network.Id if actionId != null -> Route.GET_NETWORK_ACTION_FOR_NETWORK
    is Image.Id if actionId != null -> Route.GET_IMAGE_ACTION_FOR_IMAGE
    is LoadBalancer.Id if actionId != null -> Route.GET_LOAD_BALANCER_ACTION_FOR_LOAD_BALANCER
    is Volume.Id if actionId != null -> Route.GET_VOLUME_ACTION_FOR_VOLUME
    is Certificate.Id if actionId != null -> Route.GET_CERTIFICATE_ACTION_FOR_CERTIFICATE
    is Firewall.Id if actionId != null -> Route.GET_FIREWALL_ACTION_FOR_FIREWALL
    is FloatingIp.Id if actionId != null -> Route.GET_FLOATING_IP_ACTION_FOR_FLOATING_IP
    is StorageBox.Id if actionId != null -> Route.GET_STORAGE_BOX_ACTION_FOR_STORAGE_BOX

    is Server.Id -> Route.GET_SERVER_ACTIONS
    is Network.Id -> Route.GET_NETWORK_ACTIONS
    is Image.Id -> Route.GET_IMAGE_ACTIONS
    is LoadBalancer.Id -> Route.GET_LOAD_BALANCER_ACTIONS
    is Volume.Id -> Route.GET_VOLUME_ACTIONS
    is Certificate.Id -> Route.GET_CERTIFICATE_ACTIONS
    is Firewall.Id -> Route.GET_FIREWALL_ACTIONS
    is FloatingIp.Id -> Route.GET_FLOATING_IP_ACTIONS
    is StorageBox.Id -> Route.GET_STORAGE_BOX_ACTIONS

    else -> throw InvalidResourceId(this)
}

class InvalidResourceId(override val message: String) : Throwable(message = message) {

    constructor(resourceId: ResourceId) : this(
        "Got ${resourceId::class.simpleName} expected ${
            listOf(
                Server.Id,
                Network.Id,
                Image.Id,
                LoadBalancer.Id,
                Volume.Id,
                Certificate.Id,
                Firewall.Id,
                PrimaryIp.Id,
                FloatingIp.Id,
                StorageBox.Id,
            ).joinToString(",")
        }",
    )
}

class InvalidResourceType(override val message: String) : Throwable(message = message) {

    constructor(resourceId: ResourceType) : this(
        "Got ${resourceId::class.simpleName} expected ${
            ResourceType.entries.joinToString(",")
        }",
    )
}
