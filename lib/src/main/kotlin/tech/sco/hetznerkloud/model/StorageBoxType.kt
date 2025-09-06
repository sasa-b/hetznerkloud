@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.model.Price.Amount

@Serializable
data class StorageBoxType
@OptIn(ExperimentalSerializationApi::class)
constructor(
    val id: Id,
    val name: String,
    val description: String,
    @JsonNames("snapshot_limit")
    val snapshotLimit: Int?,
    @JsonNames("automatic_snapshot_limit")
    val automaticSnapshotLimit: Int?,
    @JsonNames("subaccounts_limit")
    val subaccountsLimit: Int,
    val size: Long,
    val prices: List<Price>,
    val deprecation: Deprecation? = null,
) {
    @Serializable
    @JvmInline
    value class Id(override val value: Long) : ResourceId {
        override val type: ResourceType
            get() = ResourceType.STORAGE_BOX_TYPE
    }

    @Serializable
    data class Price(
        val location: String,
        @JsonNames("price_hourly")
        val priceHourly: Amount,
        @JsonNames("price_monthly")
        val priceMonthly: Amount,
        @JsonNames("setup_fee")
        val setupFee: Amount,
    )
}
