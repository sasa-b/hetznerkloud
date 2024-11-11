package tech.sco.hetznerkloud.repository

import io.ktor.client.HttpClient
import io.ktor.utils.io.InternalAPI
import tech.sco.hetznerkloud.Failure
import tech.sco.hetznerkloud.Route
import tech.sco.hetznerkloud.makeRequest
import tech.sco.hetznerkloud.model.SSHKey
import tech.sco.hetznerkloud.model.SSHKey.Id
import tech.sco.hetznerkloud.request.CreateSSHKey
import tech.sco.hetznerkloud.request.Pagination
import tech.sco.hetznerkloud.request.SSHKeyFilter
import tech.sco.hetznerkloud.request.SSHKeySorting
import tech.sco.hetznerkloud.request.UpdateResource
import tech.sco.hetznerkloud.request.toQueryParams
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.Items

class SSHKeys @InternalAPI constructor(private val httpClient: HttpClient) {

    @Throws(Failure::class)
    suspend fun all(
        filter: Set<SSHKeyFilter> = emptySet(),
        sorting: Set<SSHKeySorting> = emptySet(),
        pagination: Pagination = Pagination(),
    ): Items<SSHKey> =
        httpClient.makeRequest(Route.GET_ALL_SSH_KEYS, queryParams = filter.toQueryParams() + sorting.toQueryParams() + pagination.toQueryParams())

    @Throws(Failure::class)
    suspend fun find(id: Id): Item<SSHKey> = httpClient.makeRequest(Route.GET_SSH_KEY, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun create(body: CreateSSHKey): Item<SSHKey> = httpClient.makeRequest(Route.CREATE_SSH_KEY, body = body)

    @Throws(Failure::class)
    suspend fun update(id: Id, body: UpdateResource): Item<SSHKey> = httpClient.makeRequest(Route.UPDATE_SSH_KEY, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun delete(id: Id): Unit = httpClient.makeRequest(Route.DELETE_SSH_KEY, resourceId = id.value)
}
