package tech.sco.hetznerkloud.request

import tech.sco.hetznerkloud.QueryParams

data class Pagination(
    val page: Int = 1,
    val perPage: Int = 25,
) {
    fun toQueryParams(): QueryParams = listOf(Pair("page", "$page"), Pair("per_page", "$perPage"))
}
