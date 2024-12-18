package tech.sco.hetznerkloud.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.model.Firewall
import tech.sco.hetznerkloud.model.Labels
import tech.sco.hetznerkloud.model.ServerResource

@Serializable
data class CreateFirewall(
    @SerialName("apply_to")
    val applyTo: List<ApplyTo.Resource>,
    val labels: Labels,
    val name: String,
    val rules: List<Firewall.Rule>,
) : HttpBody

@Serializable
data class ApplyTo(
    @SerialName("apply_to")
    val applyTo: List<Resource>,
) : HttpBody {

    @Serializable
    sealed interface Resource

    @Serializable
    @SerialName("server")
    data class Server(val server: ServerResource) : Resource

    @Serializable
    @SerialName("label_selector")
    data class LabelSelector(
        @SerialName("label_selector")
        val labelSelector: Value,
    ) : Resource {
        @Serializable
        data class Value(val selector: String)
    }
}

data class RemoveFrom(
    @SerialName("remove_from")
    val removeFrom: List<ApplyTo.Resource>,
) : HttpBody
