package tech.sco.hetznerkloud

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import io.ktor.http.toURI
import io.ktor.utils.io.ByteReadChannel
import tech.sco.hetznerkloud.model.ErrorCode
import java.io.File

@Suppress("CyclomaticComplexMethod")
internal fun createMockEngine(apiToken: ApiToken, resourceIdProvider: ((HttpRequestData) -> Long)? = null) =
    MockEngine { request ->

        if (request.headers["Authorization"] != "Bearer ${apiToken.value}") {
            return@MockEngine errorResponse(ErrorCode.UNAUTHORIZED, HttpStatusCode.Unauthorized, mapOf(HttpHeaders.ContentType to "application/json"))
        }

        val defaultHeaders = mapOf(
            HttpHeaders.ContentType to "application/json",
            HttpHeaders.Authorization to apiToken.toString(),
        )

        val resourceId = resourceIdProvider?.invoke(request)

        val testPath = request.url.toURI().path
            .replace("{id}", resourceId?.toString() ?: "<none>")
            .replace("/v1/", "")

        val test = HttpMethodAndPath(request.method, Path("/$testPath"))

        when {
            matchRoute(Route.GET_ALL_ACTIONS, test, resourceId) -> response(Route.GET_ALL_ACTIONS, HttpStatusCode.OK, defaultHeaders)
            matchRoute(Route.GET_ACTION, test, resourceId) -> response(Route.GET_ACTION, HttpStatusCode.OK, defaultHeaders)

            matchRoute(Route.GET_ALL_SERVERS, test, resourceId) -> response(Route.GET_ALL_SERVERS, HttpStatusCode.OK, defaultHeaders)
            matchRoute(Route.GET_SERVER, test, resourceId) -> response(Route.GET_SERVER, HttpStatusCode.OK, defaultHeaders)
            matchRoute(Route.GET_SERVER_METRICS, test, resourceId) -> response(Route.GET_SERVER_METRICS, HttpStatusCode.OK, defaultHeaders)
            matchRoute(Route.CREATE_SERVER, test, resourceId) -> response(Route.CREATE_SERVER, HttpStatusCode.Created, defaultHeaders)
            matchRoute(Route.UPDATE_SERVER, test, resourceId) -> response(Route.UPDATE_SERVER, HttpStatusCode.OK, defaultHeaders)
            matchRoute(Route.DELETE_SERVER, test, resourceId) -> response(Route.DELETE_SERVER, HttpStatusCode.OK, defaultHeaders)

            matchRoute(Route.GET_ALL_SERVER_TYPES, test, resourceId) -> response(Route.GET_ALL_SERVER_TYPES, HttpStatusCode.OK, defaultHeaders)
            matchRoute(Route.GET_SERVER_TYPE, test, resourceId) -> response(Route.GET_SERVER_TYPE, HttpStatusCode.OK, defaultHeaders)

            matchRoute(Route.GET_ALL_DATACENTERS, test, resourceId) -> response(Route.GET_ALL_DATACENTERS, HttpStatusCode.OK, defaultHeaders)
            matchRoute(Route.GET_DATACENTER, test, resourceId) -> response(Route.GET_DATACENTER, HttpStatusCode.OK, defaultHeaders)

            matchRoute(Route.GET_ALL_IMAGES, test, resourceId) -> response(Route.GET_ALL_IMAGES, HttpStatusCode.OK, defaultHeaders)
            matchRoute(Route.GET_IMAGE, test, resourceId) -> response(Route.GET_IMAGE, HttpStatusCode.OK, defaultHeaders)
            matchRoute(Route.UPDATE_IMAGE, test, resourceId) -> response(Route.UPDATE_IMAGE, HttpStatusCode.OK, defaultHeaders)
            matchRoute(Route.DELETE_IMAGE, test, resourceId) -> response(Route.DELETE_IMAGE, HttpStatusCode.NoContent, defaultHeaders)

            matchRoute(Route.GET_ALL_ISOS, test, resourceId) -> response(Route.GET_ALL_ISOS, HttpStatusCode.OK, defaultHeaders)
            matchRoute(Route.GET_ISO, test, resourceId) -> response(Route.GET_ISO, HttpStatusCode.OK, defaultHeaders)

            matchRoute(Route.GET_ALL_PLACEMENT_GROUPS, test, resourceId) -> response(Route.GET_ALL_PLACEMENT_GROUPS, HttpStatusCode.OK, defaultHeaders)
            matchRoute(Route.GET_A_PLACEMENT_GROUP, test, resourceId) -> response(Route.GET_A_PLACEMENT_GROUP, HttpStatusCode.OK, defaultHeaders)
            matchRoute(Route.CREATE_PLACEMENT_GROUP, test, resourceId) -> response(Route.CREATE_PLACEMENT_GROUP, HttpStatusCode.Created, defaultHeaders)
            matchRoute(Route.UPDATE_PLACEMENT_GROUP, test, resourceId) -> response(Route.UPDATE_PLACEMENT_GROUP, HttpStatusCode.OK, defaultHeaders)
            matchRoute(Route.DELETE_PLACEMENT_GROUP, test, resourceId) -> response(Route.DELETE_PLACEMENT_GROUP, HttpStatusCode.NoContent, defaultHeaders)

            matchRoute(Route.GET_ALL_NETWORKS, test, resourceId) -> response(Route.GET_ALL_NETWORKS, HttpStatusCode.OK, defaultHeaders)
            matchRoute(Route.GET_NETWORK, test, resourceId) -> response(Route.GET_NETWORK, HttpStatusCode.OK, defaultHeaders)
            matchRoute(Route.CREATE_NETWORK, test, resourceId) -> response(Route.CREATE_NETWORK, HttpStatusCode.Created, defaultHeaders)
            matchRoute(Route.UPDATE_NETWORK, test, resourceId) -> response(Route.UPDATE_NETWORK, HttpStatusCode.OK, defaultHeaders)
            matchRoute(Route.DELETE_NETWORK, test, resourceId) -> response(Route.DELETE_NETWORK, HttpStatusCode.NoContent, defaultHeaders)

            matchRoute(Route.GET_ALL_LOAD_BALANCERS, test, resourceId) -> response(Route.GET_ALL_LOAD_BALANCERS, HttpStatusCode.OK, defaultHeaders)
            matchRoute(Route.GET_LOAD_BALANCER, test, resourceId) -> response(Route.GET_LOAD_BALANCER, HttpStatusCode.OK, defaultHeaders)
            matchRoute(Route.CREATE_LOAD_BALANCER, test, resourceId) -> response(Route.CREATE_LOAD_BALANCER, HttpStatusCode.Created, defaultHeaders)
            matchRoute(Route.UPDATE_LOAD_BALANCER, test, resourceId) -> response(Route.UPDATE_LOAD_BALANCER, HttpStatusCode.OK, defaultHeaders)
            matchRoute(Route.DELETE_LOAD_BALANCER, test, resourceId) -> response(Route.DELETE_LOAD_BALANCER, HttpStatusCode.NoContent, defaultHeaders)

            matchRoute(Route.GET_ALL_LOAD_BALANCER_TYPES, test, resourceId) -> response(Route.GET_ALL_LOAD_BALANCER_TYPES, HttpStatusCode.OK, defaultHeaders)
            matchRoute(Route.GET_LOAD_BALANCER_TYPE, test, resourceId) -> response(Route.GET_LOAD_BALANCER_TYPE, HttpStatusCode.OK, defaultHeaders)

            matchRoute(Route.GET_ALL_SSH_KEYS, test, resourceId) -> response(Route.GET_ALL_SSH_KEYS, HttpStatusCode.OK, defaultHeaders)
            matchRoute(Route.GET_SSH_KEY, test, resourceId) -> response(Route.GET_SSH_KEY, HttpStatusCode.OK, defaultHeaders)
            matchRoute(Route.CREATE_SSH_KEY, test, resourceId) -> response(Route.CREATE_SSH_KEY, HttpStatusCode.Created, defaultHeaders)
            matchRoute(Route.UPDATE_SSH_KEY, test, resourceId) -> response(Route.UPDATE_SSH_KEY, HttpStatusCode.OK, defaultHeaders)
            matchRoute(Route.DELETE_SSH_KEY, test, resourceId) -> response(Route.DELETE_SSH_KEY, HttpStatusCode.NoContent, defaultHeaders)

            matchRoute(Route.GET_ALL_VOLUMES, test, resourceId) -> response(Route.GET_ALL_VOLUMES, HttpStatusCode.OK, defaultHeaders)
            matchRoute(Route.GET_VOLUME, test, resourceId) -> response(Route.GET_VOLUME, HttpStatusCode.OK, defaultHeaders)
            matchRoute(Route.CREATE_VOLUME, test, resourceId) -> response(Route.CREATE_VOLUME, HttpStatusCode.Created, defaultHeaders)
            matchRoute(Route.UPDATE_VOLUME, test, resourceId) -> response(Route.UPDATE_VOLUME, HttpStatusCode.OK, defaultHeaders)
            matchRoute(Route.DELETE_VOLUME, test, resourceId) -> response(Route.DELETE_VOLUME, HttpStatusCode.NoContent, defaultHeaders)

            matchRoute(Route.GET_ALL_CERTIFICATES, test, resourceId) -> response(Route.GET_ALL_CERTIFICATES, HttpStatusCode.OK, defaultHeaders)
            matchRoute(Route.GET_CERTIFICATE, test, resourceId) -> response(Route.GET_CERTIFICATE, HttpStatusCode.OK, defaultHeaders)
            matchRoute(Route.CREATE_CERTIFICATE, test, resourceId) -> response(Route.CREATE_CERTIFICATE, HttpStatusCode.Created, defaultHeaders)
            matchRoute(Route.UPDATE_CERTIFICATE, test, resourceId) -> response(Route.UPDATE_CERTIFICATE, HttpStatusCode.OK, defaultHeaders)
            matchRoute(Route.DELETE_CERTIFICATE, test, resourceId) -> response(Route.DELETE_CERTIFICATE, HttpStatusCode.NoContent, defaultHeaders)

            matchRoute(Route.GET_ALL_FIREWALLS, test, resourceId) -> response(Route.GET_ALL_FIREWALLS, HttpStatusCode.OK, defaultHeaders)
            matchRoute(Route.GET_FIREWALL, test, resourceId) -> response(Route.GET_FIREWALL, HttpStatusCode.OK, defaultHeaders)
            matchRoute(Route.CREATE_FIREWALL, test, resourceId) -> response(Route.CREATE_FIREWALL, HttpStatusCode.Created, defaultHeaders)
            matchRoute(Route.UPDATE_FIREWALL, test, resourceId) -> response(Route.UPDATE_FIREWALL, HttpStatusCode.OK, defaultHeaders)
            matchRoute(Route.DELETE_FIREWALL, test, resourceId) -> response(Route.DELETE_FIREWALL, HttpStatusCode.NoContent, defaultHeaders)

            else -> respondError(HttpStatusCode.NotFound)
        }
    }

internal fun createErrorEngine(error: ErrorCode, httpStatusCode: HttpStatusCode) = MockEngine { request ->

    val headers = if (error == ErrorCode.RATE_LIMIT_EXCEEDED) {
        mapOf(
            HttpHeaders.ContentType to "application/json",
            "RateLimit-Limit" to "3600",
            "RateLimit-Remaining" to "2456",
            "RateLimit-Reset" to "1731011315",
        )
    } else {
        mapOf(HttpHeaders.ContentType to "application/json")
    }

    errorResponse(error, httpStatusCode, headers)
}

private fun matchRoute(route: Route, test: HttpMethodAndPath, resourceId: Long?) = if (resourceId != null) {
    val (httpMethod, path) = route.value
    httpMethod == test.first && path.withId(resourceId).value == test.second.value
} else {
    route.value == test
}

@Suppress("CyclomaticComplexMethod")
private fun content(route: Route): String = when (route) {
    Route.GET_ALL_ACTIONS -> "src/test/resources/examples/response/get_all_actions.json"
    Route.GET_ACTION -> "src/test/resources/examples/response/get_an_action.json"

    Route.GET_ALL_SERVERS -> "src/test/resources/examples/response/get_all_servers.json"
    Route.GET_SERVER -> "src/test/resources/examples/response/get_a_server.json"
    Route.GET_SERVER_METRICS -> "src/test/resources/examples/response/get_server_metrics.json"
    Route.CREATE_SERVER -> "src/test/resources/examples/response/create_a_server.json"
    Route.UPDATE_SERVER -> "src/test/resources/examples/response/update_a_server.json"
    Route.DELETE_SERVER -> "src/test/resources/examples/response/delete_a_server.json"

    Route.GET_ALL_SERVER_TYPES -> "src/test/resources/examples/response/get_all_server_types.json"
    Route.GET_SERVER_TYPE -> "src/test/resources/examples/response/get_a_server_type.json"

    Route.GET_ALL_DATACENTERS -> "src/test/resources/examples/response/get_all_datacenters.json"
    Route.GET_DATACENTER -> "src/test/resources/examples/response/get_a_datacenter.json"

    Route.GET_ALL_ISOS -> "src/test/resources/examples/response/get_all_isos.json"
    Route.GET_ISO -> "src/test/resources/examples/response/get_an_iso.json"

    Route.GET_ALL_IMAGES -> "src/test/resources/examples/response/get_all_images.json"
    Route.GET_IMAGE -> "src/test/resources/examples/response/get_an_image.json"
    Route.UPDATE_IMAGE -> "src/test/resources/examples/response/update_an_image.json"
    Route.DELETE_IMAGE -> "src/test/resources/examples/response/no_content.json"

    Route.GET_ALL_PLACEMENT_GROUPS -> "src/test/resources/examples/response/get_all_placement_groups.json"
    Route.GET_A_PLACEMENT_GROUP -> "src/test/resources/examples/response/get_a_placement_group.json"
    Route.CREATE_PLACEMENT_GROUP -> "src/test/resources/examples/response/create_a_placement_group.json"
    Route.UPDATE_PLACEMENT_GROUP -> "src/test/resources/examples/response/update_a_placement_group.json"
    Route.DELETE_PLACEMENT_GROUP -> "src/test/resources/examples/response/no_content.json"

    Route.GET_ALL_NETWORKS -> "src/test/resources/examples/response/get_all_networks.json"
    Route.GET_NETWORK -> "src/test/resources/examples/response/get_a_network.json"
    Route.CREATE_NETWORK -> "src/test/resources/examples/response/create_a_network.json"
    Route.UPDATE_NETWORK -> "src/test/resources/examples/response/update_a_network.json"
    Route.DELETE_NETWORK -> "src/test/resources/examples/response/no_content.json"

    Route.GET_ALL_LOAD_BALANCERS -> "src/test/resources/examples/response/get_all_load_balancers.json"
    Route.GET_LOAD_BALANCER -> "src/test/resources/examples/response/get_a_load_balancer.json"
    Route.CREATE_LOAD_BALANCER -> "src/test/resources/examples/response/create_a_load_balancer.json"
    Route.UPDATE_LOAD_BALANCER -> "src/test/resources/examples/response/update_a_load_balancer.json"
    Route.DELETE_LOAD_BALANCER -> "src/test/resources/examples/response/no_content.json"

    Route.GET_ALL_LOAD_BALANCER_TYPES -> "src/test/resources/examples/response/get_all_load_balancer_types.json"
    Route.GET_LOAD_BALANCER_TYPE -> "src/test/resources/examples/response/get_a_load_balancer_type.json"

    Route.GET_ALL_SSH_KEYS -> "src/test/resources/examples/response/get_all_ssh_keys.json"
    Route.GET_SSH_KEY -> "src/test/resources/examples/response/get_an_ssh_key.json"
    Route.CREATE_SSH_KEY -> "src/test/resources/examples/response/create_an_ssh_key.json"
    Route.UPDATE_SSH_KEY -> "src/test/resources/examples/response/update_an_ssh_key.json"
    Route.DELETE_SSH_KEY -> "src/test/resources/examples/response/no_content.json"

    Route.GET_ALL_VOLUMES -> "src/test/resources/examples/response/get_all_volumes.json"
    Route.GET_VOLUME -> "src/test/resources/examples/response/get_a_volume.json"
    Route.CREATE_VOLUME -> "src/test/resources/examples/response/create_a_volume.json"
    Route.UPDATE_VOLUME -> "src/test/resources/examples/response/update_a_volume.json"
    Route.DELETE_VOLUME -> "src/test/resources/examples/response/no_content.json"

    Route.GET_ALL_CERTIFICATES -> "src/test/resources/examples/response/get_all_certificates.json"
    Route.GET_CERTIFICATE -> "src/test/resources/examples/response/get_a_certificate.json"
    Route.CREATE_CERTIFICATE -> "src/test/resources/examples/response/create_a_managed_certificate.json"
    Route.UPDATE_CERTIFICATE -> "src/test/resources/examples/response/update_a_certificate.json"
    Route.DELETE_CERTIFICATE -> "src/test/resources/examples/response/no_content.json"

    Route.GET_ALL_FIREWALLS -> "src/test/resources/examples/response/get_all_firewalls.json"
    Route.GET_FIREWALL -> "src/test/resources/examples/response/get_a_firewall.json"
    Route.CREATE_FIREWALL -> "src/test/resources/examples/response/create_a_firewall.json"
    Route.UPDATE_FIREWALL -> "src/test/resources/examples/response/update_a_firewall.json"
    Route.DELETE_FIREWALL -> "src/test/resources/examples/response/no_content.json"
}.let {
    File(it).readText(Charsets.UTF_8)
}

@Suppress("CyclomaticComplexMethod")
private fun error(code: ErrorCode): String = when (code) {
    ErrorCode.NOT_FOUND -> "src/test/resources/examples/error/not_found.json"
    ErrorCode.FORBIDDEN -> "src/test/resources/examples/error/forbidden.json"
    ErrorCode.UNAUTHORIZED -> "src/test/resources/examples/error/unauthorized.json"
    ErrorCode.INVALID_INPUT -> "src/test/resources/examples/error/invalid_input.json"
    ErrorCode.JSON_ERROR -> "src/test/resources/examples/error/json_error.json"
    ErrorCode.LOCKED -> "src/test/resources/examples/error/locked.json"
    ErrorCode.RATE_LIMIT_EXCEEDED -> "src/test/resources/examples/error/rate_limit_exceeded.json"
    ErrorCode.RESOURCE_LIMIT_EXCEEDED -> "src/test/resources/examples/error/resource_limit_exceeded.json"
    ErrorCode.RESOURCE_UNAVAILABLE -> "src/test/resources/examples/error/resource_unavailable.json"
    ErrorCode.SERVER_ERROR -> "src/test/resources/examples/error/server_error.json"
    ErrorCode.SERVICE_ERROR -> "src/test/resources/examples/error/service_error.json"
    ErrorCode.UNIQUENESS_ERROR -> "src/test/resources/examples/error/uniqueness_error.json"
    ErrorCode.PROTECTED -> "src/test/resources/examples/error/protected.json"
    ErrorCode.MAINTENANCE -> "src/test/resources/examples/error/maintenance.json"
    ErrorCode.CONFLICT -> "src/test/resources/examples/error/conflict.json"
    ErrorCode.UNSUPPORTED_ERROR -> "src/test/resources/examples/error/unsupported_error.json"
    ErrorCode.TOKEN_READONLY -> "src/test/resources/examples/error/token_readonly.json"
    ErrorCode.UNAVAILABLE -> "src/test/resources/examples/error/unavailable.json"
    ErrorCode.NO_SPACE_LEFT_IN_LOCATION -> "src/test/resources/examples/error/no_space_left_in_location.json"
    ErrorCode.ACTION_FAILED -> "src/test/resources/examples/error/action_failed.json"
    ErrorCode.PLACEMENT_ERROR -> "src/test/resources/examples/error/placement_error.json"
    ErrorCode.PRIMARY_IP_ASSIGNED -> "src/test/resources/examples/error/primary_ip_assigned.json"
    ErrorCode.PRIMARY_IP_DATACENTER_MISMATCH -> "src/test/resources/examples/error/primary_ip_datacenter_mismatch.json"
    ErrorCode.PRIMARY_IP_VERSION_MISMATCH -> "src/test/resources/examples/error/primary_ip_version_mismatch.json"
    ErrorCode.CLOUD_RESOURCE_IP_NOT_ALLOWED -> "src/test/resources/examples/error/cloud_resource_ip_not_allowed.json"
    ErrorCode.IP_NOT_OWNED -> "src/test/resources/examples/error/ip_not_owned.json"
    ErrorCode.LOAD_BALANCER_NOT_ATTACHED_TO_NETWORK -> "src/test/resources/examples/error/load_balancer_not_attached_to_network.json"
    ErrorCode.ROBOT_UNAVAILABLE -> "src/test/resources/examples/error/robot_unavailable.json"
    ErrorCode.SERVER_NOT_ATTACHED_TO_NETWORK -> "src/test/resources/examples/error/server_not_attached_to_network.json"
    ErrorCode.SOURCE_PORT_ALREADY_USED -> "src/test/resources/examples/error/source_port_already_used.json"
    ErrorCode.MISSING_IPV4 -> "src/test/resources/examples/error/missing_ipv4.json"
    ErrorCode.TARGET_ALREADY_DEFINED -> "src/test/resources/examples/error/target_already_defined.json"
    ErrorCode.SERVER_ALREADY_ADDED -> "src/test/resources/examples/error/server_already_added.json"
    ErrorCode.INCOMPATIBLE_NETWORK_TYPE -> "src/test/resources/examples/error/incompatible_network_type.json"
    ErrorCode.FIREWALL_RESOURCE_NOT_FOUND -> "src/test/resources/examples/error/firewall_resource_not_found.json"
    ErrorCode.SERVER_NOT_STOPPED -> "src/test/resources/examples/error/server_not_stopped.json"
    ErrorCode.SERVER_HAS_IPV4 -> "src/test/resources/examples/error/server_has_ipv4.json"
    ErrorCode.SERVER_HAS_IPV6 -> "src/test/resources/examples/error/server_has_ipv6.json"
}.let {
    File(it).readText(Charsets.UTF_8)
}

private fun MockRequestHandleScope.response(
    route: Route,
    statusCode: HttpStatusCode,
    headers: Map<String, String>,
): HttpResponseData =
    respond(
        content = ByteReadChannel(content(route)),
        status = statusCode,
        headers = headers {
            headers.forEach {
                append(it.key, it.value)
            }
        },
    )

private fun MockRequestHandleScope.errorResponse(
    error: ErrorCode,
    statusCode: HttpStatusCode,
    headers: Map<String, String>,
): HttpResponseData =
    respond(
        content = ByteReadChannel(error(error)),
        status = statusCode,
        headers = headers {
            headers.forEach {
                append(it.key, it.value)
            }
        },
    )
