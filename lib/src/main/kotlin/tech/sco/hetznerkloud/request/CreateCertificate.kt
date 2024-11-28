@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.request

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface CreateCertificate : HttpBody

@Serializable
@SerialName("managed")
data class CreateManagedCertificate(
    @SerialName("domain_names")
    val domainNames: List<String>,
    val name: String,
) : CreateCertificate {
    val type: CertificateType = CertificateType.MANAGED
}

@Serializable
@SerialName("uploaded")
data class CreateUploadedCertificate(
    val certificate: String,
    val name: String,
    @SerialName("private_key")
    val privateKey: String,
) : CreateCertificate {
    val type: CertificateType = CertificateType.UPLOADED
}

enum class CertificateType {
    @SerialName("managed")
    MANAGED,

    @SerialName("uploaded")
    UPLOADED,
}
