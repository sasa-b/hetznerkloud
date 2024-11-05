@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

typealias Labels = Map<String, String>

@Serializable
data class Meta(
    val pagination: Pagination,
) {
    @Serializable
    data class Pagination(
        @JsonNames("last_page")
        val lastPage: Int?,
        @JsonNames("next_page")
        val nextPage: Int?,
        val page: Int,
        @JsonNames("per_page")
        val perPage: Int,
        @JsonNames("previous_page")
        val previousPage: Int?,
        @JsonNames("total_entries")
        val totalEntries: Int?,
    )

    companion object {
        fun of(
            lastPage: Int?,
            nextPage: Int?,
            page: Int,
            perPage: Int,
            previousPage: Int?,
            totalEntries: Int?,
        ): Meta = Meta(Pagination(lastPage, nextPage, page, perPage, previousPage, totalEntries))
    }
}

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

@Serializable
data class Protection(val delete: Boolean)

enum class NetworkZone {
    @SerialName("eu-central")
    EU_CENTRAL,

    @SerialName("us-east")
    US_EAST,

    @SerialName("us-west")
    US_WEST,

    @SerialName("ap-southeast")
    AP_SOUTHEAST,
}

// TODO: add a sealed interface serialization approach for the resource
@Serializable
data class Resource(
    val id: Long,
    val type: String,
)
