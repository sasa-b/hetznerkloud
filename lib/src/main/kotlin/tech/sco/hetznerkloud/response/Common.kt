@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.response

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.model.Error
import tech.sco.hetznerkloud.model.Meta

@Serializable
data class Items<out T>(
    val meta: Meta,
    @JsonNames(
        "actions",
        "servers",
        "server_types",
        "networks",
        "placement_groups",
        "images",
        "isos",
        "datacenters",
        "load_balancers",
        "load_balancer_types",
        "volumes",
        "ssh_keys",
        "primary_ips",
        "floating_ips",
        "firewalls",
        "certificates",
    )
    val items: List<T>,
) : Collection<T> by items

@Serializable
data class Item<out T>(
    @JsonNames(
        "action",
        "server",
        "server_type",
        "network",
        "placement_group",
        "image",
        "iso",
        "datacenter",
        "load_balancer",
        "load_balancer_type",
        "metrics",
        "volume",
        "ssh_key",
        "primary_ip",
        "floating_ip",
        "firewall",
        "certificate",
    )
    val value: T,
)

@Serializable
data class Failure(val error: Error)
