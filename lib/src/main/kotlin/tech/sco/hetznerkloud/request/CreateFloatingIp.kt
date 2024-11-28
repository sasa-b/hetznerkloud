@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.request

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.model.IpType
import tech.sco.hetznerkloud.model.Labels
import tech.sco.hetznerkloud.model.Server

@Serializable
data class CreateFloatingIp(
    // id or name of the location
    @SerialName("home_location")
    val homeLocation: String?,
    val labels: Labels? = null,
    val name: String,
    val type: IpType,
    val server: Server.Id?,
    val description: String? = null,
) : HttpBody {
    init {
        require(!(homeLocation == null && server == null)) {
            "Home location is optional only when no server is provided"
        }
    }
}
