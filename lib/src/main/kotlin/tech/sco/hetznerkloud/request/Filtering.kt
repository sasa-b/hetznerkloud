package tech.sco.hetznerkloud.request

import tech.sco.hetznerkloud.QueryParams

typealias Filter = Pair<FilterField, String>
typealias ActionFilter = Pair<FilterFields.Action, String>
typealias ServerFilter = Pair<FilterFields.Server, String>
typealias ServerTypeFilter = Pair<FilterFields.ServerType, String>
typealias DatacenterFilter = Pair<FilterFields.Datacenter, String>
typealias ImageFilter = Pair<FilterFields.Image, String>
typealias IsoFilter = Pair<FilterFields.Iso, String>

sealed interface FilterField {
    val value: String
}

fun Collection<Filter>.toQueryParams(): QueryParams = map { Pair(it.first.value, it.second) }

object FilterFields {
    enum class Action(override val value: String) : FilterField {
        ID("id"),
        STATUS("status"),
    }

    enum class Server(override val value: String) : FilterField {
        NAME("name"),
        LABEL_SELECTOR("label_selector"),
        STATUS("status"),
    }

    enum class ServerType(override val value: String) : FilterField {
        NAME("name"),
    }

    enum class Datacenter(override val value: String) : FilterField {
        NAME("name"),
    }

    enum class Image(override val value: String) : FilterField {
        TYPE("type"),
        BOUND_TO("bound_to"),
        STATUS("status"),
        INCLUDE_DEPRECATED("include_deprecated"),
        NAME("name"),
        LABEL_SELECTOR("label_selector"),
        ARCHITECTURE("architecture"),
    }

    enum class Iso(override val value: String) : FilterField {
        NAME("name"),
        ARCHITECTURE("architecture"),
        INCLUDE_ARCHITECTURE_WILDCARD("include_architecture_wildcard"),
    }
}
