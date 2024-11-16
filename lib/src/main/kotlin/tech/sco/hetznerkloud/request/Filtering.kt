package tech.sco.hetznerkloud.request

import tech.sco.hetznerkloud.QueryParams

typealias Filter = Pair<FilterField, String>
typealias ActionFilter = Pair<FilterFields.Action, String>
typealias ServerFilter = Pair<FilterFields.Server, String>
typealias ServerActionFilter = Pair<FilterFields.ServerAction, String>
typealias ServerMetricsFilter = Pair<FilterFields.ServerMetrics, String>
typealias ServerTypeFilter = Pair<FilterFields.ServerType, String>
typealias DatacenterFilter = Pair<FilterFields.Datacenter, String>
typealias ImageFilter = Pair<FilterFields.Image, String>
typealias IsoFilter = Pair<FilterFields.Iso, String>
typealias NetworkFilter = Pair<FilterFields.Network, String>
typealias LoadBalancerFilter = Pair<FilterFields.LoadBalancer, String>
typealias LoadBalancerTypeFilter = Pair<FilterFields.LoadBalancerType, String>
typealias SSHKeyFilter = Pair<FilterFields.SSHKey, String>
typealias VolumeFilter = Pair<FilterFields.Volume, String>
typealias CertificateFilter = Pair<FilterFields.Certificate, String>
typealias FirewallFilter = Pair<FilterFields.Firewall, String>
typealias PrimaryIpFilter = Pair<FilterFields.PrimaryIp, String>
typealias FloatingIpFilter = Pair<FilterFields.FloatingIp, String>

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

    enum class ServerAction(override val value: String) : FilterField {
        ID("id"),
        STATUS("status")
    }

    enum class ServerMetrics(override val value: String) : FilterField {
        TYPE("type"),
        START("start"),
        END("end"),
        STEP("step"),
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

    enum class Network(override val value: String) : FilterField {
        NAME("name"),
        LABEL_SELECTOR("label_selector"),
    }

    enum class LoadBalancer(override val value: String) : FilterField {
        NAME("name"),
        LABEL_SELECTOR("label_selector"),
    }

    enum class LoadBalancerType(override val value: String) : FilterField {
        NAME("name"),
    }

    enum class SSHKey(override val value: String) : FilterField {
        NAME("name"),
        FINGERPRINT("fingerprint"),
        LABEL_SELECTOR("label_selector"),
    }

    enum class Volume(override val value: String) : FilterField {
        STATUS("status"),
        NAME("name"),
        LABEL_SELECTOR("label_selector"),
    }

    enum class Certificate(override val value: String) : FilterField {
        NAME("name"),
        TYPE("type"),
        LABEL_SELECTOR("label_selector"),
    }

    enum class Firewall(override val value: String) : FilterField {
        NAME("name"),
        LABEL_SELECTOR("label_selector"),
    }

    enum class PrimaryIp(override val value: String) : FilterField {
        NAME("name"),
        LABEL_SELECTOR("label_selector"),
    }

    enum class FloatingIp(override val value: String) : FilterField {
        NAME("name"),
        LABEL_SELECTOR("label_selector"),
    }
}
