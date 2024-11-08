@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.serialization.OffsetDateTimeSerializer
import java.time.OffsetDateTime

@Serializable
sealed interface Certificate {
    val id: Id

    @Serializable
    @JvmInline
    value class Id(val value: Long)

    @Serializable
    data class Status(val error: Error? = null, val issuance: Issuance, val renewal: Renewal) {

        @Serializable
        enum class Issuance {
            @SerialName("pending")
            PENDING,

            @SerialName("completed")
            COMPLETED,

            @SerialName("failed")
            FAILED,
        }

        @Serializable
        enum class Renewal {
            @SerialName("scheduled")
            SCHEDULED,

            @SerialName("pending")
            PENDING,

            @SerialName("failed")
            FAILED,

            @SerialName("unavailable")
            UNAVAILABLE,

            @SerialName("")
            EMPTY,
        }
    }
}

@Serializable
@SerialName("managed")
data class ManagedCertificate(
    override val id: Certificate.Id,
    val certificate: String? = null,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val created: OffsetDateTime,
    @JsonNames("domain_names")
    val domainNames: List<String>,
    val fingerprint: String?,
    val name: String,
    @JsonNames("not_valid_after")
    @Serializable(with = OffsetDateTimeSerializer::class)
    val notValidAfter: OffsetDateTime? = null,
    @JsonNames("not_valid_before")
    @Serializable(with = OffsetDateTimeSerializer::class)
    val notValidBefore: OffsetDateTime? = null,
    val status: Certificate.Status? = null,
    @JsonNames("used_by")
    val usedBy: List<Resource>,
) : Certificate

@Serializable
@SerialName("uploaded")
data class UploadedCertificate(
    override val id: Certificate.Id,
    val certificate: String,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val created: OffsetDateTime,
    @JsonNames("domain_names")
    val domainNames: List<String>,
    val fingerprint: String?,
    val labels: Labels,
    val name: String,
    @JsonNames("not_valid_after")
    @Serializable(with = OffsetDateTimeSerializer::class)
    val notValidAfter: OffsetDateTime,
    @JsonNames("not_valid_before")
    @Serializable(with = OffsetDateTimeSerializer::class)
    val notValidBefore: OffsetDateTime,
    val status: Certificate.Status? = null,
    @JsonNames("used_by")
    val usedBy: List<Resource>,
) : Certificate
