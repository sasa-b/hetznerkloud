@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import java.time.Instant

@Serializable
@JsonClassDiscriminator("code")
sealed class Error : Throwable() {
    abstract override val message: String
    abstract val errorCode: ErrorCode
}

@Serializable
@SerialName("unauthorized")
data class UnauthorizedError(override val message: String) : Error() {
    override val errorCode = ErrorCode.UNAUTHORIZED
}

@Serializable
@SerialName("forbidden")
data class ForbiddenError(override val message: String) : Error() {
    override val errorCode = ErrorCode.FORBIDDEN
}

@Serializable
@SerialName("method_not_allowed")
data class MethodNotAllowed(override val message: String) : Error() {
    override val errorCode = ErrorCode.METHOD_NOT_ALLOWED
}

@Serializable
@SerialName("invalid_input")
data class InvalidInputError(override val message: String, val details: Details) : Error() {

    override val errorCode = ErrorCode.INVALID_INPUT

    @Serializable
    data class Details(val fields: List<Field>) {

        @Serializable
        data class Field(val name: String, val messages: List<String>)
    }
}

@Serializable
@SerialName("json_error")
data class JsonError(override val message: String) : Error() {
    override val errorCode = ErrorCode.JSON_ERROR
}

@Serializable
@SerialName("locked")
data class LockedError(override val message: String) : Error() {
    override val errorCode = ErrorCode.LOCKED
}

@Serializable
@SerialName("not_found")
data class NotFoundError(override val message: String) : Error() {
    override val errorCode = ErrorCode.NOT_FOUND
}

@Serializable
@SerialName("rate_limit_exceeded")
data class RateLimitExceededError(
    override val message: String,
    val hourlyRateLimit: Int = 3600,
    val hourlyRateLimitRemaining: Int? = null,
    val hourlyRateLimitResetTimestamp: Long? = null,
) : Error() {
    override val errorCode = ErrorCode.RATE_LIMIT_EXCEEDED
    val hourlyRateLimitReset: Instant? get() = if (hourlyRateLimitResetTimestamp != null) {
        Instant.ofEpochSecond(hourlyRateLimitResetTimestamp)
    } else {
        null
    }
}

@Serializable
@SerialName("resource_limit_exceeded")
data class ResourceLimitExceededError(override val message: String, val details: Details) : Error() {
    override val errorCode = ErrorCode.RESOURCE_LIMIT_EXCEEDED

    @Serializable
    data class Details(val limits: List<Field>) {
        @Serializable
        data class Field(val name: String)
    }
}

@Serializable
@SerialName("resource_unavailable")
data class ResourceUnavailableError(override val message: String) : Error() {
    override val errorCode = ErrorCode.RESOURCE_UNAVAILABLE
}

@Serializable
@SerialName("server_error")
data class ServerError(override val message: String) : Error() {
    override val errorCode = ErrorCode.SERVER_ERROR
}

@Serializable
@SerialName("service_error")
data class ServiceError(override val message: String) : Error() {
    override val errorCode = ErrorCode.SERVICE_ERROR
}

@Serializable
@SerialName("uniqueness_error")
data class UniquenessError(override val message: String, val details: Details) : Error() {
    override val errorCode = ErrorCode.UNIQUENESS_ERROR

    @Serializable
    data class Details(val fields: List<Field>) {
        @Serializable
        data class Field(val name: String)
    }
}

@Serializable
@SerialName("protected")
data class ProtectedError(override val message: String) : Error() {
    override val errorCode = ErrorCode.PROTECTED
}

@Serializable
@SerialName("maintenance")
data class MaintenanceError(override val message: String) : Error() {
    override val errorCode = ErrorCode.MAINTENANCE
}

@Serializable
@SerialName("conflict")
data class ConflictError(override val message: String) : Error() {
    override val errorCode = ErrorCode.CONFLICT
}

@Serializable
@SerialName("unsupported_error")
data class UnsupportedError(override val message: String) : Error() {
    override val errorCode = ErrorCode.UNSUPPORTED_ERROR
}

@Serializable
@SerialName("token_readonly")
data class TokenReadonlyError(override val message: String) : Error() {
    override val errorCode = ErrorCode.TOKEN_READONLY
}

@Serializable
@SerialName("unavailable")
data class UnavailableError(override val message: String) : Error() {
    override val errorCode = ErrorCode.UNAVAILABLE
}

@Serializable
@SerialName("no_space_left_in_location")
data class NoSpaceLeftInLocationError(override val message: String) : Error() {
    override val errorCode = ErrorCode.NO_SPACE_LEFT_IN_LOCATION
}

@Serializable
@SerialName("action_failed")
data class ActionFailedError(override val message: String) : Error() {
    override val errorCode = ErrorCode.ACTION_FAILED
}

@Serializable
@SerialName("placement_error")
data class PlacementError(override val message: String) : Error() {
    override val errorCode = ErrorCode.PLACEMENT_ERROR
}

@Serializable
@SerialName("primary_ip_assigned")
data class PrimaryIpAssignedError(override val message: String) : Error() {
    override val errorCode = ErrorCode.PRIMARY_IP_ASSIGNED
}

@Serializable
@SerialName("primary_ip_datacenter_mismatch")
data class PrimaryIpDatacenterMismatchError(override val message: String) : Error() {
    override val errorCode = ErrorCode.PRIMARY_IP_DATACENTER_MISMATCH
}

@Serializable
@SerialName("primary_ip_version_mismatch")
data class PrimaryIpVersionMismatchError(override val message: String) : Error() {
    override val errorCode = ErrorCode.PRIMARY_IP_VERSION_MISMATCH
}

@Serializable
@SerialName("cloud_resource_ip_not_allowed")
data class CloudResourceIpNotAllowedError(override val message: String) : Error() {
    override val errorCode = ErrorCode.CLOUD_RESOURCE_IP_NOT_ALLOWED
}

@Serializable
@SerialName("ip_not_owned")
data class IpNotOwnedError(override val message: String) : Error() {
    override val errorCode = ErrorCode.IP_NOT_OWNED
}

@Serializable
@SerialName("load_balancer_not_attached_to_network")
data class LoadBalancerNotAttachedToNetworkError(override val message: String) : Error() {
    override val errorCode = ErrorCode.LOAD_BALANCER_NOT_ATTACHED_TO_NETWORK
}

@Serializable
@SerialName("robot_unavailable")
data class RobotUnavailableError(override val message: String) : Error() {
    override val errorCode = ErrorCode.ROBOT_UNAVAILABLE
}

@Serializable
@SerialName("server_not_attached_to_network")
data class ServerNotAttachedToNetworkError(override val message: String) : Error() {
    override val errorCode = ErrorCode.SERVER_NOT_ATTACHED_TO_NETWORK
}

@Serializable
@SerialName("source_port_already_used")
data class SourcePortAlreadyUsedError(override val message: String) : Error() {
    override val errorCode = ErrorCode.SOURCE_PORT_ALREADY_USED
}

@Serializable
@SerialName("missing_ipv4")
data class MissingIpv4Error(override val message: String) : Error() {
    override val errorCode = ErrorCode.MISSING_IPV4
}

@Serializable
@SerialName("target_already_defined")
data class TargetAlreadyDefinedError(override val message: String) : Error() {
    override val errorCode = ErrorCode.TARGET_ALREADY_DEFINED
}

@Serializable
@SerialName("server_already_added")
data class ServerAlreadyAddedError(override val message: String) : Error() {
    override val errorCode = ErrorCode.SERVER_ALREADY_ADDED
}

@Serializable
@SerialName("incompatible_network_type")
data class IncompatibleNetworkTypeError(override val message: String) : Error() {
    override val errorCode = ErrorCode.INCOMPATIBLE_NETWORK_TYPE
}

@Serializable
@SerialName("firewall_resource_not_found")
data class FirewallResourceNotFoundError(override val message: String) : Error() {
    override val errorCode = ErrorCode.FIREWALL_RESOURCE_NOT_FOUND
}

@Serializable
@SerialName("server_not_stopped")
data class ServerNotStoppedError(override val message: String) : Error() {
    override val errorCode = ErrorCode.SERVER_NOT_STOPPED
}

@Serializable
@SerialName("server_has_ipv4")
data class ServerHasIpv4Error(override val message: String) : Error() {
    override val errorCode = ErrorCode.SERVER_HAS_IPV4
}

@Serializable
@SerialName("server_has_ipv6")
data class ServerHasIpv6Error(override val message: String) : Error() {
    override val errorCode = ErrorCode.SERVER_HAS_IPV6
}

@Serializable
@SerialName("caa_record_does_not_allow_ca")
data class CaaRecordDoesNotAllowCaError(override val message: String) : Error() {
    override val errorCode: ErrorCode
        get() = ErrorCode.CAA_RECORD_DOES_NOT_ALLOW_CA
}

@Serializable
@SerialName("ca_dns_validation_failed")
data class CaDnsValidationFailedError(override val message: String) : Error() {
    override val errorCode: ErrorCode
        get() = ErrorCode.CA_DNS_VALIDATION_FAILED
}

@Serializable
@SerialName("ca_too_many_authorizations_failed_recently")
data class CaTooManyAuthorizationsFailedError(override val message: String) : Error() {
    override val errorCode: ErrorCode
        get() = ErrorCode.CA_TOO_MANY_AUTHORIZATIONS_FAILED
}

@Serializable
@SerialName("ca_too_many_certificates_issued_for_registered_domain")
data class CaTooManyCertificatesIssuedForDomainError(override val message: String) : Error() {
    override val errorCode: ErrorCode
        get() = ErrorCode.CA_TOO_MANY_CERTIFICATES_ISSUED_FOR_DOMAIN
}

@Serializable
@SerialName("ca_too_many_duplicate_certificates")
data class CaTooManyCertificatesDuplicateError(override val message: String) : Error() {
    override val errorCode: ErrorCode
        get() = ErrorCode.CA_TOO_MANY_DUPLICATE_CERTIFICATES
}

@Serializable
@SerialName("could_not_verify_domain_delegated_to_zone")
data class CouldNotVerifyDomainDelegatedToZoneError(override val message: String) : Error() {
    override val errorCode: ErrorCode
        get() = ErrorCode.COULD_NOT_VERIFY_DOMAIN_DELEGATE_TO_ZONE
}

@Serializable
@SerialName("dns_zone_not_found")
data class DnsZoneNotFoundError(override val message: String) : Error() {
    override val errorCode: ErrorCode
        get() = ErrorCode.DNS_ZONE_NOT_FOUND
}

@Serializable
@SerialName("dns_zone_is_secondary_zone")
data class DnsZoneIsSecondaryZoneError(override val message: String) : Error() {
    override val errorCode: ErrorCode
        get() = ErrorCode.DNS_ZONE_IS_SECONDARY_ZONE
}

@Serializable
@SerialName("firewall_already_removed")
data class FirewallAlreadyRemovedError(override val message: String) : Error() {
    override val errorCode: ErrorCode
        get() = ErrorCode.FIREWALL_ALREADY_REMOVED
}

@Serializable
@SerialName("firewall_managed_by_label_selector")
data class FirewallManagedByLabelSelectorError(override val message: String) : Error() {
    override val errorCode: ErrorCode
        get() = ErrorCode.FIREWALL_MANAGED_BY_LABEL_SELECTOR
}

@Serializable
@SerialName("firewall_already_applied")
data class FirewallAlreadyAppliedError(override val message: String) : Error() {
    override val errorCode: ErrorCode
        get() = ErrorCode.FIREWALL_ALREADY_APPLIED
}

@Serializable
@SerialName("private_net_only_server")
data class PrivateNetOnlyServerError(override val message: String) : Error() {
    override val errorCode: ErrorCode
        get() = ErrorCode.PRIVATE_NET_ONLY_SERVER
}

enum class ErrorCode(val value: String) {
    FORBIDDEN("forbidden"),
    UNAUTHORIZED("unauthorized"),
    INVALID_INPUT("invalid_input"),
    JSON_ERROR("json_error"),
    LOCKED("locked"),
    NOT_FOUND("not_found"),
    METHOD_NOT_ALLOWED("method_not_allowed"),
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
    ACTION_FAILED("action_failed "),
    PLACEMENT_ERROR("placement_error"),
    PRIMARY_IP_ASSIGNED("primary_ip_assigned"),
    PRIMARY_IP_DATACENTER_MISMATCH("primary_ip_datacenter_mismatch"),
    PRIMARY_IP_VERSION_MISMATCH("primary_ip_version_mismatch"),
    CLOUD_RESOURCE_IP_NOT_ALLOWED("cloud_resource_ip_not_allowed"),
    IP_NOT_OWNED("ip_not_owned"),
    IP_NOT_AVAILABLE("ip_not_available"),
    SUBNET_NOT_AVAILABLE("no_subnet_available"),
    LOAD_BALANCER_NOT_ATTACHED_TO_NETWORK("load_balancer_not_attached_to_network"),
    ROBOT_UNAVAILABLE("robot_unavailable"),
    SERVER_NOT_ATTACHED_TO_NETWORK("server_not_attached_to_network"),
    SERVER_ALREADY_ATTACHED_TO_NETWORK("server_already_attached"),
    NETWORKS_OVERLAP("networks_overlap"),
    SOURCE_PORT_ALREADY_USED("source_port_already_used"),
    MISSING_IPV4("missing_ipv4"),
    TARGET_ALREADY_DEFINED("target_already_defined"),
    SERVER_ALREADY_ADDED("server_already_added"),
    INCOMPATIBLE_NETWORK_TYPE("incompatible_network_type"),
    FIREWALL_RESOURCE_NOT_FOUND("firewall_resource_not_found"),
    FIREWALL_ALREADY_APPLIED("firewall_already_applied"),
    FIREWALL_ALREADY_REMOVED("firewall_already_removed"),
    FIREWALL_MANAGED_BY_LABEL_SELECTOR("firewall_managed_by_label_selector"),
    SERVER_NOT_STOPPED("server_not_stopped"),
    SERVER_HAS_IPV4("server_has_ipv4"),
    SERVER_HAS_IPV6("server_has_ipv6"),
    INVALID_SERVER_TYPE("invalid_server_type"),
    CAA_RECORD_DOES_NOT_ALLOW_CA("caa_record_does_not_allow_ca"),
    CA_DNS_VALIDATION_FAILED("ca_dns_validation_failed"),
    CA_TOO_MANY_AUTHORIZATIONS_FAILED("ca_too_many_authorizations_failed_recently"),
    CA_TOO_MANY_CERTIFICATES_ISSUED_FOR_DOMAIN("ca_too_many_certificates_issued_for_registered_domain"),
    CA_TOO_MANY_DUPLICATE_CERTIFICATES("ca_too_many_duplicate_certificates"),
    COULD_NOT_VERIFY_DOMAIN_DELEGATE_TO_ZONE("could_not_verify_domain_delegated_to_zone"),
    DNS_ZONE_NOT_FOUND("dns_zone_not_found"),
    DNS_ZONE_IS_SECONDARY_ZONE("dns_zone_is_secondary_zone"),
    PRIVATE_NET_ONLY_SERVER("private_net_only_server"),
}
