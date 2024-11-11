@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.request

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.model.IpType
import tech.sco.hetznerkloud.model.Labels
import tech.sco.hetznerkloud.model.Server

@Serializable
class CreateFloatingIp(
    // id or name of the location
    @JsonNames("home_location")
    val homeLocation: String?,
    val labels: Labels = emptyMap(),
    val name: String,
    val type: IpType,
    val server: Server.Id?,
    val description: String? = null,
) {
    init {
        require(homeLocation == null && server != null) {
            "Home location is optional only when no server is provided"
        }
    }
}
