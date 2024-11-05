@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@Serializable
@JsonClassDiscriminator("code")
sealed class Error : Throwable() {
    abstract override val message: String
    abstract val errorCode: ErrorCode
}

@Serializable
@SerialName("unauthorized")
data class UnauthorizedError(
    override val message: String,
) : Error() {
    override val errorCode = ErrorCode.UNAUTHORIZED
}

@Serializable
@SerialName("forbidden")
data class ForbiddenError(
    override val message: String,
) : Error() {
    override val errorCode = ErrorCode.FORBIDDEN
}

@Serializable
@SerialName("invalid_input")
data class InvalidInputError(
    override val message: String,
    val details: Details,
) : Error() {

    override val errorCode = ErrorCode.INVALID_INPUT

    @Serializable
    data class Details(
        val fields: List<Field>,
    ) {

        @Serializable
        data class Field(
            val name: String,
            val messages: List<String>,
        )
    }
}

@Serializable
@SerialName("json_error")
data class JsonError(
    override val message: String,
) : Error() {
    override val errorCode = ErrorCode.JSON_ERROR
}

@Serializable
@SerialName("locked")
data class LockedError(
    override val message: String,
) : Error() {
    override val errorCode = ErrorCode.LOCKED
}

@Serializable
@SerialName("not_found")
data class NotFoundError(
    override val message: String,
) : Error() {
    override val errorCode = ErrorCode.NOT_FOUND
}

@Serializable
@SerialName("rate_limit_exceeded")
data class RateLimitExceeded(
    override val message: String,
) : Error() {
    override val errorCode = ErrorCode.RATE_LIMIT_EXCEEDED
}

@Serializable
@SerialName("resource_limit_exceeded")
data class ResourceLimitExceeded(
    override val message: String,
    val details: List<Map<String, String>>,
) : Error() {
    override val errorCode = ErrorCode.RESOURCE_LIMIT_EXCEEDED
}

@Serializable
@SerialName("resource_unavailable")
data class ResourceUnavailableError(
    override val message: String,
) : Error() {
    override val errorCode = ErrorCode.RESOURCE_UNAVAILABLE
}

@Serializable
@SerialName("server_error")
data class ServerError(
    override val message: String,
) : Error() {
    override val errorCode = ErrorCode.SERVER_ERROR
}

@Serializable
@SerialName("service_error")
data class ServiceError(
    override val message: String,
) : Error() {
    override val errorCode = ErrorCode.SERVICE_ERROR
}

@Serializable
@SerialName("uniqueness_error")
data class UniquenessError(
    override val message: String,
    val details: List<Map<String, String>>,
) : Error() {
    override val errorCode = ErrorCode.UNIQUENESS_ERROR
}

@Serializable
@SerialName("protected")
data class ProtectedError(
    override val message: String,
) : Error() {
    override val errorCode = ErrorCode.PROTECTED
}

@Serializable
@SerialName("maintenance")
data class MaintenanceError(
    override val message: String,
) : Error() {
    override val errorCode = ErrorCode.MAINTENANCE
}

@Serializable
@SerialName("conflict")
data class ConflictError(
    override val message: String,
) : Error() {
    override val errorCode = ErrorCode.CONFLICT
}

@Serializable
@SerialName("unsupported_error")
data class UnsupportedError(
    override val message: String,
) : Error() {
    override val errorCode = ErrorCode.UNSUPPORTED_ERROR
}

@Serializable
@SerialName("token_readonly")
data class TokenReadonlyError(
    override val message: String,
) : Error() {
    override val errorCode = ErrorCode.TOKEN_READONLY
}

@Serializable
@SerialName("unavailable")
data class UnavailableError(
    override val message: String,
) : Error() {
    override val errorCode = ErrorCode.UNAVAILABLE
}

@Serializable
@SerialName("no_space_left_in_location")
data class NoSpaceLeftInLocation(
    override val message: String,
) : Error() {
    override val errorCode = ErrorCode.NO_SPACE_LEFT_IN_LOCATION
}

@Serializable
@SerialName("action_failed")
data class ActionFailed(
    override val message: String,
) : Error() {
    override val errorCode = ErrorCode.ACTION_FAILED
}
