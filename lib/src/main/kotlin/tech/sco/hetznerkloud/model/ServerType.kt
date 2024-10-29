@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.model.Server.Deprecation

@Serializable
data class ServerType(
    val id: Long,
    val architecture: String,
    val cores: Int,
    @JsonNames("cpu_type")
    val cpuType: String,
    val deprecated: Boolean,
    val deprecation: Deprecation? = null,
    val description: String,
    val disk: Int,
    val memory: Int,
    val name: String,
    val prices: List<Price>,
    @JsonNames("storage_type")
    val storageType: String,
) {
    @Serializable
    data class Price(
        @JsonNames("included_traffic")
        val includedTraffic: Long,
        val location: String,
        @JsonNames("price_hourly")
        val priceHourly: Amount,
        @JsonNames("price_monthly")
        val priceMonthly: Amount,
        @JsonNames("price_per_tb_traffic")
        val pricePerTbTraffic: Amount,
    ) {
        @Serializable
        data class Amount(
            val gross: String,
            val net: String,
        )
    }
}
