package tech.sco.hetznerkloud.model

import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.serialization.CertificateIdSerializer

@Serializable
class Certificate(val id: Id) {
    @Serializable(with = CertificateIdSerializer::class)
    data class Id(override val value: Long) : ResourceId()
}
