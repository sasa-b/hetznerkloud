package tech.sco.hetznerkloud.request

import tech.sco.hetznerkloud.QueryParams

typealias Sorting = Pair<SortingField, SortingDirection>
typealias ActionSorting = Pair<SortingFields.Action, SortingDirection>
typealias ServerSorting = Pair<SortingFields.Server, SortingDirection>
typealias ServerActionSorting = Pair<SortingFields.Server, SortingDirection>
typealias DatacenterSorting = Pair<SortingFields.Datacenter, SortingDirection>
typealias ImageSorting = Pair<SortingFields.Image, SortingDirection>
typealias LoadBalancerSorting = Pair<SortingFields.LoadBalancer, SortingDirection>
typealias SSHKeySorting = Pair<SortingFields.SSHKey, SortingDirection>
typealias VolumeSorting = Pair<SortingFields.Volume, SortingDirection>
typealias CertificateSorting = Pair<SortingFields.Certificate, SortingDirection>
typealias FirewallSorting = Pair<SortingFields.Firewall, SortingDirection>
typealias PrimaryIpSorting = Pair<SortingFields.PrimaryIp, SortingDirection>
typealias FloatingIpSorting = Pair<SortingFields.FloatingIp, SortingDirection>
typealias NetworkActionSorting = Pair<SortingFields.NetworkAction, SortingDirection>

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

    enum class ServerAction(override val value: String) : SortingField {
        COMMAND("command"),
        STATUS("status"),
        STARTED("started"),
        FINISHED("finished"),
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

    enum class Volume(override val value: String) : SortingField {
        ID("id"),
        NAME("name"),
        CREATED("created"),
    }

    enum class Certificate(override val value: String) : SortingField {
        ID("id"),
        NAME("name"),
        CREATED("created"),
    }

    enum class Firewall(override val value: String) : SortingField {
        ID("id"),
        NAME("name"),
        CREATED("created"),
    }

    enum class PrimaryIp(override val value: String) : SortingField {
        ID("id"),
        CREATED("created"),
    }

    enum class FloatingIp(override val value: String) : SortingField {
        ID("id"),
        CREATED("created"),
    }

    enum class NetworkAction(override val value: String) : SortingField {
        ID("id"),
        COMMAND("command"),
        STATUS("status"),
        STARTED("started"),
        FINISHED("finished"),
    }
}
