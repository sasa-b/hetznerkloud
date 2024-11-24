package tech.sco.hetznerkloud.api

import io.ktor.client.HttpClient
import io.ktor.utils.io.InternalAPI
import tech.sco.hetznerkloud.Failure
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
        when (resourceType) {
            ResourceType.SERVER -> Route.GET_ALL_SERVER_ACTIONS
            ResourceType.NETWORK -> Route.GET_ALL_NETWORK_ACTIONS
            ResourceType.IMAGE -> Route.GET_ALL_IMAGE_ACTIONS
            ResourceType.LOAD_BALANCER -> Route.GET_ALL_LOAD_BALANCER_ACTIONS
            ResourceType.VOLUME -> Route.GET_ALL_VOLUME_ACTIONS
            ResourceType.CERTIFICATE -> Route.GET_ALL_CERTIFICATE_ACTIONS
            ResourceType.FIREWALL -> Route.GET_ALL_FIREWALL_ACTIONS
            ResourceType.PRIMARY_IP -> Route.GET_ALL_PRIMARY_IP_ACTIONS
            ResourceType.FLOATING_IP -> Route.GET_ALL_FLOATING_IP_ACTIONS
            else -> throw InvalidResourceType(resourceType)
        },
        queryParams = filter.toQueryParams() + sorting.toQueryParams() + pagination.toQueryParams(),
    )

    @Throws(Failure::class, InvalidResourceId::class)
    suspend fun <T : ResourceId> all(
        resourceId: T,
        filter: Set<ActionFilter> = emptySet(),
        sorting: Set<ActionFilter> = emptySet(),
        pagination: Pagination = Pagination(),
    ): Items<Action> = httpClient.makeRequest(
        when (resourceId) {
            is Server.Id -> Route.GET_SERVER_ACTIONS
            is Network.Id -> Route.GET_NETWORK_ACTIONS
            is Image.Id -> Route.GET_IMAGE_ACTIONS
            is LoadBalancer.Id -> Route.GET_LOAD_BALANCER_ACTIONS
            is Volume.Id -> Route.GET_VOLUME_ACTIONS
            is Certificate.Id -> Route.GET_CERTIFICATE_ACTIONS
            is Firewall.Id -> Route.GET_FIREWALL_ACTIONS
            is PrimaryIp.Id -> Route.GET_PRIMARY_IP_ACTIONS
            is FloatingIp.Id -> Route.GET_FLOATING_IP_ACTIONS
            else -> throw InvalidResourceId(resourceId)
        },
        resourceId = resourceId.value,
        queryParams = filter.toQueryParams() + sorting.toQueryParams() + pagination.toQueryParams(),
    )

    @Throws(Failure::class)
    suspend fun find(id: Action.Id): Item<Action> = httpClient.makeRequest(Route.GET_ACTION, id.value)

    @Throws(Failure::class)
    suspend fun find(resourceType: ResourceType, actionId: Action.Id): Item<Action> = httpClient.makeRequest(
        when (resourceType) {
            ResourceType.SERVER -> Route.GET_SERVER_ACTION
            ResourceType.NETWORK -> Route.GET_NETWORK_ACTION
            ResourceType.IMAGE -> Route.GET_IMAGE_ACTION
            ResourceType.LOAD_BALANCER -> Route.GET_LOAD_BALANCER_ACTION
            ResourceType.VOLUME -> Route.GET_VOLUME_ACTION
            ResourceType.CERTIFICATE -> Route.GET_CERTIFICATE_ACTION
            ResourceType.FIREWALL -> Route.GET_FIREWALL_ACTION
            ResourceType.PRIMARY_IP -> Route.GET_PRIMARY_IP_ACTION
            ResourceType.FLOATING_IP -> Route.GET_FLOATING_IP_ACTION
            else -> throw InvalidResourceType(resourceType)
        },
        mapOf("id" to actionId.asString()),
    )

    @Throws(Failure::class, InvalidResourceId::class)
    suspend fun <T : ResourceId> find(resourceId: T, actionId: Action.Id): Item<Action> = httpClient.makeRequest(
        when (resourceId) {
            is Server.Id -> Route.GET_SERVER_ACTION_FOR_SERVER
            is Network.Id -> Route.GET_NETWORK_ACTION_FOR_NETWORK
            is Image.Id -> Route.GET_IMAGE_ACTION_FOR_IMAGE
            is LoadBalancer.Id -> Route.GET_LOAD_BALANCER_ACTION_FOR_LOAD_BALANCER
            is Volume.Id -> Route.GET_VOLUME_ACTION_FOR_VOLUME
            is Certificate.Id -> Route.GET_CERTIFICATE_ACTION_FOR_CERTIFICATE
            is Firewall.Id -> Route.GET_FIREWALL_ACTION_FOR_FIREWALL
            is PrimaryIp.Id -> Route.GET_PRIMARY_IP_ACTION
            is FloatingIp.Id -> Route.GET_FLOATING_IP_ACTION_FOR_FLOATING_IP
            else -> throw InvalidResourceId(resourceId)
        },
        routeParams = mapOf(
            "id" to resourceId.asString(),
            "action_id" to actionId.asString(),
        ),
    )
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
            ).joinToString(",")
        }",
    )
}

class InvalidResourceType(override val message: String) : Throwable(message = message) {

    constructor(resourceId: ResourceType) : this(
        "Got ${resourceId::class.simpleName} expected ${
            listOf(
                ResourceType.NETWORK,
                ResourceType.IMAGE,
                ResourceType.LOAD_BALANCER,
                ResourceType.VOLUME,
                ResourceType.CERTIFICATE,
                ResourceType.FIREWALL,
                ResourceType.PRIMARY_IP,
                ResourceType.FLOATING_IP,
            ).joinToString(",")
        }",
    )
}
