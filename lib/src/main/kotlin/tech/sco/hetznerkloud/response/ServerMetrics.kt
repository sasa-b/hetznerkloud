package tech.sco.hetznerkloud.response

import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.model.ServerMetrics as ServerMetricsModel

@Serializable
data class ServerMetrics(
    val metrics: ServerMetricsModel,
)
