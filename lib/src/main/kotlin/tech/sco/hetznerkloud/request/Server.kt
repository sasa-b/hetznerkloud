@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.request

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.model.Iso
import tech.sco.hetznerkloud.model.Labels
import tech.sco.hetznerkloud.model.Network
import tech.sco.hetznerkloud.model.PlacementGroup

// TODO: check which values can be omitted and which ones can be sent as null
@Serializable
data class CreateServer(
    val automount: Boolean = false,
    val datacenter: String,
    val firewalls: List<Firewall> = emptyList(),
    val image: String,
    val labels: Labels = emptyMap(),
    val location: String,
    val name: String,
    val networks: List<Int> = emptyList(),
    val placementGroup: PlacementGroup.Id? = null,
    @JsonNames("public_net")
    val publicNetwork: PublicNetwork,
    @JsonNames("server_type")
    val serverType: String,
    @JsonNames("ssh_keys")
    val sshKeys: List<String>,
    @JsonNames("start_after_create")
    val startAfterCreate: Boolean = true,
    @JsonNames("user_data")
    val userData: String,
    val volumes: List<Int> = emptyList(),
) : HttpBody {
    @Serializable
    data class Firewall(
        @JsonNames("firewall")
        val id: Long,
    )

    @Serializable
    data class PublicNetwork(
        @JsonNames("enable_ipv4")
        val enableIpv4: Boolean = true,
        @JsonNames("enable_ipv6")
        val enableIpv6: Boolean = true,
        val ipv4: String? = null,
        val ipv6: String? = null,
    )
}

@Serializable
data class AddToPlacementGroup(
    @JsonNames("placement_group")
    val id: PlacementGroup.Id,
) : HttpBody

@Serializable
data class AttachIsoByName(val iso: String) : HttpBody

@Serializable
data class AttachIsoById(val iso: Iso.Id) : HttpBody

@Serializable
data class AttachToNetwork(
    @JsonNames("alias_ips")
    val aliasIps: List<String>,
    val ip: String,
    val network: Network.Id,
) : HttpBody
