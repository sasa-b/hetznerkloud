package tech.sco.hetznerkloud.request

import tech.sco.hetznerkloud.QueryParams

typealias Sorting = Pair<SortingField, SortingDirection>
typealias ActionSorting = Pair<SortingFields.Action, SortingDirection>
typealias ServerSorting = Pair<SortingFields.Server, SortingDirection>
typealias DatacenterSorting = Pair<SortingFields.Datacenter, SortingDirection>
typealias ImageSorting = Pair<SortingFields.Image, SortingDirection>
typealias LoadBalancerSorting = Pair<SortingFields.LoadBalancer, SortingDirection>
typealias SSHKeySorting = Pair<SortingFields.SSHKey, SortingDirection>

enum class SortingDirection(val value: String) {
    ASC("asc"),
    DESC("desc"),
}

fun Collection<Sorting>.toQueryParams(): QueryParams = map { Pair("sorting", it.toString()) }

sealed interface SortingField {
    val value: String
}

object SortingFields {

    enum class Action(override val value: String) : SortingField {
        ID("id"),
        STATUS("status"),
        COMMAND("command"),
        STARTED("started"),
        FINISHED("finished"),
    }

    enum class Server(override val value: String) : SortingField {
        ID("id"),
        NAME("name"),
        CREATED("created"),
    }

    enum class Datacenter(override val value: String) : SortingField {
        ID("id"),
        NAME("name"),
    }

    enum class Image(override val value: String) : SortingField {
        ID("id"),
        NAME("name"),
        CREATED("created"),
    }

    enum class LoadBalancer(override val value: String) : SortingField {
        ID("id"),
        NAME("name"),
        CREATED("created"),
    }

    enum class SSHKey(override val value: String) : SortingField {
        ID("id"),
        NAME("name"),
    }
}
