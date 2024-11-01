@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

typealias Labels = Map<String, String>

sealed class ResourceId {
    abstract val value: Long
}

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
            lastPage: Int,
            nextPage: Int,
            page: Int,
            perPage: Int,
            previousPage: Int,
            totalEntries: Int,
        ): Meta = Meta(Pagination(lastPage, nextPage, page, perPage, previousPage, totalEntries))
    }
}
