@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.response

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.Meta
import tech.sco.hetznerkloud.model.Error as DomainError

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
        "storage_boxes",
        "storage_box_types",
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
        "pricing",
        "storage_box",
        "storage_box_type",
    )
    val value: T,
)

@Serializable
data class ItemCreated<out T>(
    val action: Action? = null,
    @JsonNames("next_actions")
    val nextActions: List<Action> = emptyList(),
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
    val item: T,
)

@Serializable
data class Error(val error: DomainError)
