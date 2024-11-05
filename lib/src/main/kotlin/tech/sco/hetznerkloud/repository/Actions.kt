package tech.sco.hetznerkloud.repository

import io.ktor.client.HttpClient
import tech.sco.hetznerkloud.Route
import tech.sco.hetznerkloud.makeRequest
import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.Action.Id
import tech.sco.hetznerkloud.model.Error
import tech.sco.hetznerkloud.request.ActionFilter
import tech.sco.hetznerkloud.request.ActionSorting
import tech.sco.hetznerkloud.request.Pagination
import tech.sco.hetznerkloud.request.toQueryParams
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.Items

class Actions(private val httpClient: HttpClient) {

    @Throws(Error::class)
    suspend fun all(
        filter: Set<ActionFilter> = emptySet(),
        sorting: Set<ActionSorting> = emptySet(),
        pagination: Pagination = Pagination(),
    ): Items<Action> = httpClient.makeRequest(Route.GET_ALL_ACTIONS, queryParams = (filter.toQueryParams() + sorting.toQueryParams() + pagination.toQueryParams()))

    @Throws(Error::class)
    suspend fun find(id: Id): Item<Action> = httpClient.makeRequest(Route.GET_ACTION, id.value)
}
