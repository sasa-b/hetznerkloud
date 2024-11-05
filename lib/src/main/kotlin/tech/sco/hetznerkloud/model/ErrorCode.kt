package tech.sco.hetznerkloud.model

enum class ErrorCode(val value: String) {
    FORBIDDEN("forbidden"),
    UNAUTHORIZED("unauthorized"),
    INVALID_INPUT("invalid_input"),
    JSON_ERROR("json_error"),
    LOCKED("locked"),
    NOT_FOUND("not_found"),
    RATE_LIMIT_EXCEEDED("rate_limit_exceeded"),
    RESOURCE_LIMIT_EXCEEDED("resource_limit_exceeded"),
    RESOURCE_UNAVAILABLE("resource_unavailable"),
    SERVER_ERROR("server_error"),
    SERVICE_ERROR("service_error"),
    UNIQUENESS_ERROR("uniqueness_error"),
    PROTECTED("protected"),
    MAINTENANCE("maintenance"),
    CONFLICT("conflict"),
    UNSUPPORTED_ERROR("unsupported_error"),
    TOKEN_READONLY("token_readonly"),
    UNAVAILABLE("unavailable"),
    NO_SPACE_LEFT_IN_LOCATION("no_space_left_in_location"),
}
