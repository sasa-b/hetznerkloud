@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.request

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.model.IpType
import tech.sco.hetznerkloud.model.Labels
import tech.sco.hetznerkloud.model.PrimaryIp

// Hetzner docs is conflicting here, it says assignee_type is required
// But it also says to omit datacenter if assignee_id or assignee_type are present
// which is not possible since assignee_type is required
@Serializable
data class CreatePrimaryIp(
    @SerialName("assignee_id")
    val assigneeId: Long?,
    @SerialName("assignee_type")
    val assigneeType: PrimaryIp.AssigneeType?,
    @SerialName("auto_delete")
    val autoDelete: Boolean = false,
    // Accepts id or name
    val datacenter: String?,
    val labels: Labels? = null,
    val name: String,
    val type: IpType,
) : HttpBody

@Serializable
data class UpdatePrimaryIp(
    @SerialName("auto_delete")
    val autoDelete: Boolean = false,
    val labels: Labels? = null,
    val name: String? = null,
) : HttpBody
