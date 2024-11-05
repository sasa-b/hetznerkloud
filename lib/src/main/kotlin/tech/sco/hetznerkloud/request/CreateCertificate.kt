@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.request

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
sealed interface CreateCertificate : HttpBody

@Serializable
@SerialName("managed")
data class CreateManagedCertificate(
    @JsonNames("domain_names")
    val domainNames: List<String>,
    val name: String,
) : CreateCertificate

@Serializable
@SerialName("uploaded")
data class CreateUploadedCertificate(
    val certificate: String,
    val name: String,
    @JsonNames("private_key")
    val privateKey: String,
) : CreateCertificate
