package tech.sco.hetznerkloud.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.model.Deprecation

@Serializable
data class Iso(val id: Id, val architecture: String?, val deprecation: Deprecation?, val description: String, val name: String?, val type: Type?) {
    @Serializable
    @JvmInline
    value class Id(override val value: Long) : ResourceId {
        override val type: ResourceType
            get() = ResourceType.ISO
    }

    @Serializable
    enum class Type {
        @SerialName("public")
        PUBLIC,

        @SerialName("private")
        PRIVATE,
    }
}
