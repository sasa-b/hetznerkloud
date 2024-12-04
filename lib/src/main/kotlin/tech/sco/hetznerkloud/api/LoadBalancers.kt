package tech.sco.hetznerkloud.api

import io.ktor.client.HttpClient
import io.ktor.utils.io.InternalAPI
import tech.sco.hetznerkloud.Failure
import tech.sco.hetznerkloud.Route
import tech.sco.hetznerkloud.makeRequest
import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.LoadBalancer
import tech.sco.hetznerkloud.model.LoadBalancer.Id
import tech.sco.hetznerkloud.request.AddTarget
import tech.sco.hetznerkloud.request.AttachToNetwork
import tech.sco.hetznerkloud.request.ChangeAlgorithm
import tech.sco.hetznerkloud.request.ChangeDeleteProtection
import tech.sco.hetznerkloud.request.ChangeLoadBalancerType
import tech.sco.hetznerkloud.request.ChangeReverseDns
import tech.sco.hetznerkloud.request.CreateLoadBalancer
import tech.sco.hetznerkloud.request.DeleteService
import tech.sco.hetznerkloud.request.DetachFromNetwork
import tech.sco.hetznerkloud.request.LoadBalancerFilter
import tech.sco.hetznerkloud.request.LoadBalancerSorting
import tech.sco.hetznerkloud.request.Pagination
import tech.sco.hetznerkloud.request.RemoveTarget
import tech.sco.hetznerkloud.request.UpdateResource
import tech.sco.hetznerkloud.request.UpsertService
import tech.sco.hetznerkloud.request.toQueryParams
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.ItemCreated
import tech.sco.hetznerkloud.response.Items

@Suppress("TooManyFunctions")
class LoadBalancers @InternalAPI constructor(private val httpClient: HttpClient) {

    @Throws(Failure::class)
    suspend fun all(filter: Set<LoadBalancerFilter> = emptySet(), sorting: Set<LoadBalancerSorting> = emptySet(), pagination: Pagination = Pagination()): Items<LoadBalancer> =
        httpClient.makeRequest(
            Route.GET_ALL_LOAD_BALANCERS,
            queryParams = filter.toQueryParams() + sorting.toQueryParams() + pagination.toQueryParams(),
        )

    @Throws(Failure::class)
    suspend fun find(id: Id): Item<LoadBalancer> = httpClient.makeRequest(Route.GET_LOAD_BALANCER, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun create(body: CreateLoadBalancer): ItemCreated<LoadBalancer> = httpClient.makeRequest(Route.CREATE_LOAD_BALANCER, body = body)

    @Throws(Failure::class)
    suspend fun update(id: Id, body: UpdateResource): Item<LoadBalancer> = httpClient.makeRequest(Route.UPDATE_LOAD_BALANCER, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun delete(id: Id): Unit = httpClient.makeRequest(Route.DELETE_LOAD_BALANCER, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun addService(id: Id, body: UpsertService): Item<Action> = httpClient.makeRequest(Route.LOAD_BALANCER_ADD_SERVICE, body = body, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun updateService(id: Id, body: UpsertService): Item<Action> = httpClient.makeRequest(Route.LOAD_BALANCER_UPDATE_SERVICE, body = body, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun deleteService(id: Id, body: DeleteService): Item<Action> = httpClient.makeRequest(Route.LOAD_BALANCER_DELETE_SERVICE, body = body, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun addTarget(id: Id, body: AddTarget): Item<Action> = httpClient.makeRequest(Route.LOAD_BALANCER_ADD_TARGET, body = body, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun removeTarget(id: Id, body: RemoveTarget): Item<Action> = httpClient.makeRequest(Route.LOAD_BALANCER_REMOVE_TARGET, body = body, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun attachToNetwork(id: Id, body: AttachToNetwork.LoadBalancer): Item<Action> =
        httpClient.makeRequest(Route.ATTACH_LOAD_BALANCER_TO_NETWORK, body = body, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun detachFromNetwork(id: Id, body: DetachFromNetwork): Item<Action> =
        httpClient.makeRequest(Route.DETACH_LOAD_BALANCER_FROM_NETWORK, body = body, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun changeAlgorithm(id: Id, body: ChangeAlgorithm): Item<Action> = httpClient.makeRequest(Route.CHANGE_LOAD_BALANCER_ALGORITHM, body = body, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun changeReverseDns(id: Id, body: ChangeReverseDns): Item<Action> = httpClient.makeRequest(Route.CHANGE_LOAD_BALANCER_REVERSE_DNS, body = body, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun changeProtection(id: Id, body: ChangeDeleteProtection): Item<Action> =
        httpClient.makeRequest(Route.CHANGE_LOAD_BALANCER_PROTECTION, body = body, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun changeType(id: Id, body: ChangeLoadBalancerType): Item<Action> = httpClient.makeRequest(Route.CHANGE_LOAD_BALANCER_TYPE, body = body, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun enablePublicInterface(id: Id): Item<Action> = httpClient.makeRequest(Route.ENABLE_LOAD_BALANCER_PUBLIC_INTERFACE, id.value)

    @Throws(Failure::class)
    suspend fun disablePublicInterface(id: Id): Item<Action> = httpClient.makeRequest(Route.DISABLE_LOAD_BALANCER_PUBLIC_INTERFACE, id.value)
}
