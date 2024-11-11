@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.request

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.model.IpType
import tech.sco.hetznerkloud.model.Labels
import tech.sco.hetznerkloud.model.PrimaryIp

@Serializable
data class CreatePrimaryIp(
    @JsonNames("assignee_id")
    val assigneeId: Long?,
    @JsonNames("assignee_type")
    val assigneeType: PrimaryIp.AssigneeType?,
    @JsonNames("auto_delete")
    val autoDelete: Boolean = false,
    // Accepts id or name
    val datacenter: String?,
    val labels: Labels = emptyMap(),
    val name: String,
    val type: IpType,
) : HttpBody {
    init {
        require(!(datacenter != null && (assigneeId != null || assigneeType != null))) {
            "datacenter and assigneeId / assigneeType fields are mutually exclusive"
        }
    }
}
