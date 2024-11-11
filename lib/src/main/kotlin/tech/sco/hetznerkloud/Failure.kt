package tech.sco.hetznerkloud

import io.ktor.client.request.HttpRequest
import tech.sco.hetznerkloud.model.Error

data class Failure(val error: Error, val request: HttpRequest) : Throwable(error.message, error)
