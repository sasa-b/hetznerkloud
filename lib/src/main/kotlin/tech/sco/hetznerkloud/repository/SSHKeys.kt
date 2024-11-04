package tech.sco.hetznerkloud.repository

import io.ktor.client.HttpClient
import tech.sco.hetznerkloud.Route
import tech.sco.hetznerkloud.makeRequest
import tech.sco.hetznerkloud.model.Error
import tech.sco.hetznerkloud.model.SSHKey
import tech.sco.hetznerkloud.model.SSHKey.Id
import tech.sco.hetznerkloud.request.CreateSSHKey
import tech.sco.hetznerkloud.request.Pagination
import tech.sco.hetznerkloud.request.SSHKeyFilter
import tech.sco.hetznerkloud.request.SSHKeySorting
import tech.sco.hetznerkloud.request.UpdateSSHKey
import tech.sco.hetznerkloud.request.toQueryParams
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.Items

class SSHKeys(private val httpClient: HttpClient) {

    @Throws(Error::class)
    suspend fun all(
        filter: Set<SSHKeyFilter> = emptySet(),
        sorting: Set<SSHKeySorting> = emptySet(),
        pagination: Pagination = Pagination(),
    ): Items<SSHKey> =
        httpClient.makeRequest(Route.GET_ALL_SSH_KEYS, queryParams = filter.toQueryParams() + sorting.toQueryParams() + pagination.toQueryParams())

    @Throws(Error::class)
    suspend fun find(id: Id): Item<SSHKey> = httpClient.makeRequest(Route.GET_SSH_KEY, resourceId = id)

    @Throws(Error::class)
    suspend fun create(body: CreateSSHKey): Item<SSHKey> = httpClient.makeRequest(Route.CREATE_SSH_KEY, body = body)

    @Throws(Error::class)
    suspend fun update(id: Id, body: UpdateSSHKey): Item<SSHKey> = httpClient.makeRequest(Route.UPDATE_SSH_KEY, resourceId = id, body = body)

    @Throws(Error::class)
    suspend fun delete(id: Id): Unit = httpClient.makeRequest(Route.DELETE_SSH_KEY, resourceId = id)
}
