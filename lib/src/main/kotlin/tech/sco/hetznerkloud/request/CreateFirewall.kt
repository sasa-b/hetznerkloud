@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.request

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.model.Firewall
import tech.sco.hetznerkloud.model.Labels

@Serializable
data class CreateFirewall(
    @SerialName("apply_to")
    val applyTo: List<ApplyTo>,
    val labels: Labels,
    val name: String,
    val rules: List<Firewall.Rule>,
) : HttpBody {
    @Serializable
    data class ApplyTo(val server: Firewall.AppliedTo.ServerResource, val type: String)
}
