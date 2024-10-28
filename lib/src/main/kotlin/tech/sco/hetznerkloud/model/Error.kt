package tech.sco.hetznerkloud.model

import kotlinx.serialization.Serializable

@Serializable
data class Error(
    val code: ErrorCode,
    val message: String,
)
