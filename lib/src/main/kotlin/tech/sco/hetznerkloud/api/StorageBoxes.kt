package tech.sco.hetznerkloud.api

import io.ktor.client.HttpClient
import tech.sco.hetznerkloud.Failure
import tech.sco.hetznerkloud.InternalAPI
import tech.sco.hetznerkloud.Route
import tech.sco.hetznerkloud.makeRequest
import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.Snapshot
import tech.sco.hetznerkloud.model.StorageBox
import tech.sco.hetznerkloud.model.StorageBox.Id
import tech.sco.hetznerkloud.model.Subaccount
import tech.sco.hetznerkloud.request.ChangeDeleteProtection
import tech.sco.hetznerkloud.request.ChangeStorageBoxType
import tech.sco.hetznerkloud.request.CreateStorageBox
import tech.sco.hetznerkloud.request.CreateStorageBoxSnapshot
import tech.sco.hetznerkloud.request.CreateStorageBoxSubaccount
import tech.sco.hetznerkloud.request.Pagination
import tech.sco.hetznerkloud.request.ResetStorageBoxPassword
import tech.sco.hetznerkloud.request.SnapshotFilter
import tech.sco.hetznerkloud.request.StorageBoxFilter
import tech.sco.hetznerkloud.request.SubaccountFilter
import tech.sco.hetznerkloud.request.UpdateResource
import tech.sco.hetznerkloud.request.toQueryParams
import tech.sco.hetznerkloud.response.Folders
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.ItemCreated
import tech.sco.hetznerkloud.response.ItemDeleted
import tech.sco.hetznerkloud.response.Items
import tech.sco.hetznerkloud.response.StorageBoxSnapshotCreated
import tech.sco.hetznerkloud.response.StorageBoxSubaccountCreated

@Suppress("TooManyFunctions")
class StorageBoxes @InternalAPI constructor(private val httpClient: HttpClient) {
    @Throws(Failure::class)
    suspend fun all(filter: Set<StorageBoxFilter> = emptySet(), pagination: Pagination = Pagination()): Items<StorageBox> = httpClient.makeRequest(
        Route.GET_ALL_STORAGE_BOXES,
        queryParams = filter.toQueryParams() + pagination.toQueryParams(),
    )

    @Throws(Failure::class)
    suspend fun find(id: Id): Item<StorageBox> = httpClient.makeRequest(Route.GET_STORAGE_BOX, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun create(body: CreateStorageBox): ItemCreated<StorageBox> = httpClient.makeRequest(Route.CREATE_STORAGE_BOX, body = body)

    @Throws(Failure::class)
    suspend fun createSnapshot(storageBoxId: Id, body: CreateStorageBoxSnapshot): StorageBoxSnapshotCreated =
        httpClient.makeRequest(Route.CREATE_STORAGE_BOX_SNAPSHOT, resourceId = storageBoxId.value, body = body)

    @Throws(Failure::class)
    suspend fun createSubaccount(storageBoxId: Id, body: CreateStorageBoxSubaccount): StorageBoxSubaccountCreated =
        httpClient.makeRequest(Route.CREATE_STORAGE_BOX_SUBACCOUNT, resourceId = storageBoxId.value, body = body)

    @Throws(Failure::class)
    suspend fun update(id: Id, body: UpdateResource): Item<StorageBox> = httpClient.makeRequest(Route.UPDATE_STORAGE_BOX, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun updateSnapshot(id: Id, body: UpdateResource): Item<Snapshot> = httpClient.makeRequest(Route.UPDATE_STORAGE_BOX_SNAPSHOT, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun updateSubaccount(id: Id, body: UpdateResource): Item<Subaccount> = httpClient.makeRequest(Route.UPDATE_STORAGE_BOX_SUBACCOUNT, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun content(id: Id): Folders = httpClient.makeRequest(Route.GET_STORAGE_BOX_CONTENT, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun delete(id: Id): ItemDeleted = httpClient.makeRequest(Route.DELETE_STORAGE_BOX, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun deleteSnapshot(id: Snapshot.Id): ItemDeleted = httpClient.makeRequest(Route.DELETE_STORAGE_BOX_SNAPSHOT, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun deleteSubaccount(id: Subaccount.Id): ItemDeleted = httpClient.makeRequest(Route.DELETE_STORAGE_BOX_SUBACCOUNT, resourceId = id.value)

    @Throws(Failure::class)
    suspend fun subaccounts(storageBoxId: Id, filter: Set<SubaccountFilter> = emptySet(), pagination: Pagination = Pagination()): Subaccount.Items = httpClient.makeRequest(
        Route.GET_STORAGE_BOX_SUBACCOUNTS,
        resourceId = storageBoxId.value,
        queryParams = filter.toQueryParams() + pagination.toQueryParams(),
    )

    @Throws(Failure::class)
    suspend fun subaccount(storageBoxId: Id, subaccountId: Subaccount.Id): Item<Subaccount> = httpClient.makeRequest(
        Route.GET_STORAGE_BOX_SUBACCOUNT,
        routeParams = mapOf("id" to storageBoxId.value.toString(), "subaccount_id" to subaccountId.value.toString()),
    )

    @Throws(Failure::class)
    suspend fun snapshots(storageBoxId: Id, filter: Set<SnapshotFilter> = emptySet(), pagination: Pagination = Pagination()): Snapshot.Items = httpClient.makeRequest(
        Route.GET_STORAGE_BOX_SNAPSHOTS,
        resourceId = storageBoxId.value,
        queryParams = filter.toQueryParams() + pagination.toQueryParams(),
    )

    @Throws(Failure::class)
    suspend fun snapshot(storageBoxId: Id, snapshotId: Snapshot.Id): Item<Snapshot> = httpClient.makeRequest(
        Route.GET_STORAGE_BOX_SNAPSHOT,
        routeParams = mapOf("id" to storageBoxId.value.toString(), "snapshot_id" to snapshotId.value.toString()),
    )

    @Throws(Failure::class)
    suspend fun changeProtection(id: Id, body: ChangeDeleteProtection): Item<Action> =
        httpClient.makeRequest(Route.CHANGE_STORAGE_BOX_PROTECTION, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun changeType(id: Id, body: ChangeStorageBoxType): Item<Action> = httpClient.makeRequest(Route.CHANGE_STORAGE_BOX_TYPE, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun resetPassword(id: Id, body: ResetStorageBoxPassword): Item<Action> = httpClient.makeRequest(Route.RESET_STORAGE_BOX_PASSWORD, resourceId = id.value, body = body)
}
