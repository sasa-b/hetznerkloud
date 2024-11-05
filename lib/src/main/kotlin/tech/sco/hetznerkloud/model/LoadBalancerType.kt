@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.serialization.OffsetDateTimeSerializer
import java.time.OffsetDateTime

@Serializable
data class LoadBalancerType(
    val id: Id,
    @Serializable(with = OffsetDateTimeSerializer::class)
    val deprecated: OffsetDateTime?,
    val description: String,
    @JsonNames("max_assigned_certificates")
    val maxAssignedCertificates: Int,
    @JsonNames("max_connections")
    val maxConnections: Long,
    @JsonNames("max_services")
    val maxServices: Int,
    @JsonNames("max_targets")
    val maxTargets: Int,
    val name: String,
    val prices: List<Price>,
) {
    @Serializable
    @JvmInline
    value class Id(val value: Long)
}
