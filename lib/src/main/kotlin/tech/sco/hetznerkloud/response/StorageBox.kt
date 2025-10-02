@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.response

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.Snapshot
import tech.sco.hetznerkloud.model.StorageBox
import tech.sco.hetznerkloud.model.Subaccount

@Serializable
data class StorageBoxSnapshotCreated(val snapshot: Ids, val action: Action) {
    @Serializable
    data class Ids(
        val id: Snapshot.Id,
        @JsonNames("storage_box")
        val storageBox: StorageBox.Id,
    )
}

@Serializable
data class StorageBoxSubaccountCreated(val subaccount: Ids, val action: Action) {
    @Serializable
    data class Ids(
        val id: Subaccount.Id,
        @JsonNames("storage_box")
        val storageBox: StorageBox.Id,
    )
}
