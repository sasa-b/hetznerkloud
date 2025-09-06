package tech.sco.hetznerkloud.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.model.Labels

@Serializable
data class CreateStorageBox(
    @SerialName("storage_box_type")
    val storageBoxType: String,
    val location: String,
    val name: String,
    val password: String,
    val labels: Labels,
    @SerialName("ssh_keys")
    val sshKeys: List<String> = emptyList(),
    @SerialName("access_settings")
    val accessSettings: AccessSettings? = null,
) : HttpBody {
    @Serializable
    data class AccessSettings(
        @SerialName("reachable_externally")
        val reachableExternally: Boolean? = null,
        @SerialName("samba_enabled")
        val sambaEnabled: Boolean? = null,
        @SerialName("ssh_enabled")
        val sshEnabled: Boolean? = null,
        @SerialName("webdav_enabled")
        val webdavEnabled: Boolean? = null,
        @SerialName("zfs_enabled")
        val zfsEnabled: Boolean? = null,
    )
}
