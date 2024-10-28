@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.request

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.model.Labels

@Serializable
data class CreateServer(
    val automount: Boolean = false,
    val datacenter: String,
    val firewalls: List<Firewall>,
    val image: String,
    val labels: Labels,
    val location: String,
    val name: String,
    val networks: List<Int>,
    val placementGroup: Int,
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
    val volumes: List<Int>,
) : HttpMessage {
    @Serializable
    data class Firewall(
        @JsonNames("firewall")
        val id: Long,
    )

    @Serializable
    data class PublicNetwork(
        @JsonNames("enable_ipv4")
        val enableIpv4: Boolean = false,
        @JsonNames("enable_ipv6")
        val enableIpv6: Boolean = false,
        val ipv4: String? = null,
        val ipv6: String? = null,
    )
}
