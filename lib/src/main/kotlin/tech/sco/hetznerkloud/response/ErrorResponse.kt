package tech.sco.hetznerkloud.response

import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.model.Error

@Serializable
data class ErrorResponse(val error: Error)
