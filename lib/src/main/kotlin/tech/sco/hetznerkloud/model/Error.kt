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
