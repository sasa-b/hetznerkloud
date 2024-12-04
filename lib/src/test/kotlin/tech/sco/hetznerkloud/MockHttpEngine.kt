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
import java.net.URI

@Suppress("CyclomaticComplexMethod", "LongMethod")
internal fun createMockEngine(apiToken: ApiToken, routeParamsProvider: ((HttpRequestData) -> RouteParams)? = null) = MockEngine { request ->

    if (request.headers["Authorization"] != "Bearer ${apiToken.value}") {
        return@MockEngine errorResponse(ErrorCode.UNAUTHORIZED, HttpStatusCode.Unauthorized, mapOf(HttpHeaders.ContentType to "application/json"))
    }

    val defaultHeaders = mapOf(
        HttpHeaders.ContentType to "application/json",
        HttpHeaders.Authorization to apiToken.toString(),
    )

    when {
        matchRoute(Route.GET_ALL_ACTIONS, request, routeParamsProvider) -> response(Route.GET_ALL_ACTIONS, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.GET_ACTION, request, routeParamsProvider) -> response(Route.GET_ACTION, HttpStatusCode.OK, defaultHeaders)

        matchRoute(Route.GET_ALL_SERVERS, request, routeParamsProvider) -> response(Route.GET_ALL_SERVERS, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.GET_SERVER, request, routeParamsProvider) -> response(Route.GET_SERVER, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.GET_SERVER_METRICS, request, routeParamsProvider) -> response(Route.GET_SERVER_METRICS, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.GET_SERVER_ACTIONS, request, routeParamsProvider) -> response(Route.GET_SERVER_ACTIONS, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.GET_SERVER_ACTION, request, routeParamsProvider) -> response(Route.GET_SERVER_ACTION, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.GET_SERVER_ACTION_FOR_SERVER, request, routeParamsProvider) -> response(Route.GET_SERVER_ACTION_FOR_SERVER, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.GET_ALL_SERVER_ACTIONS, request, routeParamsProvider) -> response(Route.GET_ALL_SERVER_ACTIONS, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.CREATE_SERVER, request, routeParamsProvider) -> response(Route.CREATE_SERVER, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.UPDATE_SERVER, request, routeParamsProvider) -> response(Route.UPDATE_SERVER, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.DELETE_SERVER, request, routeParamsProvider) -> response(Route.DELETE_SERVER, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.ADD_SERVER_TO_PLACEMENT_GROUP, request, routeParamsProvider) -> response(Route.ADD_SERVER_TO_PLACEMENT_GROUP, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.ATTACH_ISO_TO_SERVER, request, routeParamsProvider) -> response(Route.ATTACH_ISO_TO_SERVER, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.DETACH_ISO_FROM_SERVER, request, routeParamsProvider) -> response(Route.DETACH_ISO_FROM_SERVER, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.ATTACH_SERVER_TO_NETWORK, request, routeParamsProvider) -> response(Route.ATTACH_SERVER_TO_NETWORK, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.DETACH_SERVER_FROM_NETWORK, request, routeParamsProvider) -> response(Route.DETACH_SERVER_FROM_NETWORK, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.SHUTDOWN_SERVER, request, routeParamsProvider) -> response(Route.SHUTDOWN_SERVER, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.RESET_SERVER, request, routeParamsProvider) -> response(Route.RESET_SERVER, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.POWER_ON_SERVER, request, routeParamsProvider) -> response(Route.POWER_ON_SERVER, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.POWER_OFF_SERVER, request, routeParamsProvider) -> response(Route.POWER_OFF_SERVER, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.SOFT_REBOOT_SERVER, request, routeParamsProvider) -> response(Route.SOFT_REBOOT_SERVER, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.RESET_SERVER_ROOT_PASSWORD, request, routeParamsProvider) -> response(Route.RESET_SERVER_ROOT_PASSWORD, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.REQUEST_CONSOLE_FOR_SERVER, request, routeParamsProvider) -> response(Route.REQUEST_CONSOLE_FOR_SERVER, HttpStatusCode.Created, defaultHeaders)
        matchRoute(
            Route.REMOVE_SERVER_FROM_PLACEMENT_GROUP,
            request,
            routeParamsProvider,
        ) -> response(Route.REMOVE_SERVER_FROM_PLACEMENT_GROUP, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.REBUILD_SERVER_FROM_IMAGE, request, routeParamsProvider) -> response(Route.REBUILD_SERVER_FROM_IMAGE, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.ENABLE_SERVER_BACKUP, request, routeParamsProvider) -> response(Route.ENABLE_SERVER_BACKUP, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.DISABLE_SERVER_BACKUP, request, routeParamsProvider) -> response(Route.DISABLE_SERVER_BACKUP, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.ENABLE_SERVER_RESCUE_MODE, request, routeParamsProvider) -> response(Route.ENABLE_SERVER_RESCUE_MODE, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.DISABLE_SERVER_RESCUE_MODE, request, routeParamsProvider) -> response(Route.DISABLE_SERVER_RESCUE_MODE, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.CREATE_IMAGE_FROM_SERVER, request, routeParamsProvider) -> response(Route.CREATE_IMAGE_FROM_SERVER, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.CHANGE_SERVER_TYPE, request, routeParamsProvider) -> response(Route.CHANGE_SERVER_TYPE, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.CHANGE_SERVER_PROTECTION, request, routeParamsProvider) -> response(Route.CHANGE_SERVER_PROTECTION, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.CHANGE_SERVER_REVERSE_DNS, request, routeParamsProvider) -> response(Route.CHANGE_SERVER_REVERSE_DNS, HttpStatusCode.Created, defaultHeaders)
        matchRoute(
            Route.CHANGE_SERVER_ALIAS_IP_OF_NETWORK,
            request,
            routeParamsProvider,
        ) -> response(Route.CHANGE_SERVER_ALIAS_IP_OF_NETWORK, HttpStatusCode.Created, defaultHeaders)

        matchRoute(Route.GET_ALL_SERVER_TYPES, request, routeParamsProvider) -> response(Route.GET_ALL_SERVER_TYPES, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.GET_SERVER_TYPE, request, routeParamsProvider) -> response(Route.GET_SERVER_TYPE, HttpStatusCode.OK, defaultHeaders)

        matchRoute(Route.GET_ALL_DATACENTERS, request, routeParamsProvider) -> response(Route.GET_ALL_DATACENTERS, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.GET_DATACENTER, request, routeParamsProvider) -> response(Route.GET_DATACENTER, HttpStatusCode.OK, defaultHeaders)

        matchRoute(Route.GET_ALL_IMAGES, request, routeParamsProvider) -> response(Route.GET_ALL_IMAGES, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.GET_IMAGE, request, routeParamsProvider) -> response(Route.GET_IMAGE, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.GET_IMAGE_ACTIONS, request, routeParamsProvider) -> response(Route.GET_IMAGE_ACTIONS, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.GET_IMAGE_ACTION, request, routeParamsProvider) -> response(Route.GET_IMAGE_ACTION, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.GET_IMAGE_ACTION_FOR_IMAGE, request, routeParamsProvider) -> response(Route.GET_IMAGE_ACTION_FOR_IMAGE, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.GET_ALL_IMAGE_ACTIONS, request, routeParamsProvider) -> response(Route.GET_ALL_IMAGE_ACTIONS, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.UPDATE_IMAGE, request, routeParamsProvider) -> response(Route.UPDATE_IMAGE, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.DELETE_IMAGE, request, routeParamsProvider) -> response(Route.DELETE_IMAGE, HttpStatusCode.NoContent, defaultHeaders)
        matchRoute(Route.CHANGE_IMAGE_PROTECTION, request, routeParamsProvider) -> response(Route.CHANGE_IMAGE_PROTECTION, HttpStatusCode.Created, defaultHeaders)

        matchRoute(Route.GET_ALL_ISOS, request, routeParamsProvider) -> response(Route.GET_ALL_ISOS, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.GET_ISO, request, routeParamsProvider) -> response(Route.GET_ISO, HttpStatusCode.OK, defaultHeaders)

        matchRoute(Route.GET_ALL_PLACEMENT_GROUPS, request, routeParamsProvider) -> response(Route.GET_ALL_PLACEMENT_GROUPS, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.GET_A_PLACEMENT_GROUP, request, routeParamsProvider) -> response(Route.GET_A_PLACEMENT_GROUP, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.CREATE_PLACEMENT_GROUP, request, routeParamsProvider) -> response(Route.CREATE_PLACEMENT_GROUP, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.UPDATE_PLACEMENT_GROUP, request, routeParamsProvider) -> response(Route.UPDATE_PLACEMENT_GROUP, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.DELETE_PLACEMENT_GROUP, request, routeParamsProvider) -> response(Route.DELETE_PLACEMENT_GROUP, HttpStatusCode.NoContent, defaultHeaders)

        matchRoute(Route.GET_ALL_NETWORKS, request, routeParamsProvider) -> response(Route.GET_ALL_NETWORKS, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.GET_NETWORK, request, routeParamsProvider) -> response(Route.GET_NETWORK, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.GET_NETWORK_ACTIONS, request, routeParamsProvider) -> response(Route.GET_NETWORK_ACTIONS, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.GET_NETWORK_ACTION, request, routeParamsProvider) -> response(Route.GET_NETWORK_ACTION, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.GET_NETWORK_ACTION_FOR_NETWORK, request, routeParamsProvider) -> response(Route.GET_NETWORK_ACTION_FOR_NETWORK, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.GET_ALL_NETWORK_ACTIONS, request, routeParamsProvider) -> response(Route.GET_ALL_NETWORK_ACTIONS, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.CREATE_NETWORK, request, routeParamsProvider) -> response(Route.CREATE_NETWORK, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.UPDATE_NETWORK, request, routeParamsProvider) -> response(Route.UPDATE_NETWORK, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.DELETE_NETWORK, request, routeParamsProvider) -> response(Route.DELETE_NETWORK, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.ADD_ROUTE_TO_NETWORK, request, routeParamsProvider) -> response(Route.ADD_ROUTE_TO_NETWORK, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.DELETE_ROUTE_FROM_NETWORK, request, routeParamsProvider) -> response(Route.DELETE_ROUTE_FROM_NETWORK, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.ADD_SUBNET_TO_NETWORK, request, routeParamsProvider) -> response(Route.ADD_SUBNET_TO_NETWORK, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.DELETE_SUBNET_FROM_NETWORK, request, routeParamsProvider) -> response(Route.DELETE_SUBNET_FROM_NETWORK, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.CHANGE_NETWORK_IP_RANGE, request, routeParamsProvider) -> response(Route.CHANGE_NETWORK_IP_RANGE, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.CHANGE_NETWORK_PROTECTION, request, routeParamsProvider) -> response(Route.CHANGE_NETWORK_PROTECTION, HttpStatusCode.Created, defaultHeaders)

        matchRoute(Route.GET_ALL_LOAD_BALANCERS, request, routeParamsProvider) -> response(Route.GET_ALL_LOAD_BALANCERS, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.GET_LOAD_BALANCER, request, routeParamsProvider) -> response(Route.GET_LOAD_BALANCER, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.GET_LOAD_BALANCER_ACTIONS, request, routeParamsProvider) -> response(Route.GET_LOAD_BALANCER_ACTIONS, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.GET_LOAD_BALANCER_ACTION, request, routeParamsProvider) -> response(Route.GET_LOAD_BALANCER_ACTION, HttpStatusCode.OK, defaultHeaders)
        matchRoute(
            Route.GET_LOAD_BALANCER_ACTION_FOR_LOAD_BALANCER,
            request,
            routeParamsProvider,
        ) -> response(Route.GET_LOAD_BALANCER_ACTION_FOR_LOAD_BALANCER, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.GET_ALL_LOAD_BALANCER_ACTIONS, request, routeParamsProvider) -> response(Route.GET_ALL_LOAD_BALANCER_ACTIONS, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.CREATE_LOAD_BALANCER, request, routeParamsProvider) -> response(Route.CREATE_LOAD_BALANCER, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.UPDATE_LOAD_BALANCER, request, routeParamsProvider) -> response(Route.UPDATE_LOAD_BALANCER, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.DELETE_LOAD_BALANCER, request, routeParamsProvider) -> response(Route.DELETE_LOAD_BALANCER, HttpStatusCode.NoContent, defaultHeaders)
        matchRoute(Route.LOAD_BALANCER_ADD_SERVICE, request, routeParamsProvider) -> response(Route.LOAD_BALANCER_ADD_SERVICE, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.LOAD_BALANCER_UPDATE_SERVICE, request, routeParamsProvider) -> response(Route.LOAD_BALANCER_UPDATE_SERVICE, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.LOAD_BALANCER_DELETE_SERVICE, request, routeParamsProvider) -> response(Route.LOAD_BALANCER_DELETE_SERVICE, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.LOAD_BALANCER_ADD_TARGET, request, routeParamsProvider) -> response(Route.LOAD_BALANCER_ADD_TARGET, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.LOAD_BALANCER_REMOVE_TARGET, request, routeParamsProvider) -> response(Route.LOAD_BALANCER_REMOVE_TARGET, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.ATTACH_LOAD_BALANCER_TO_NETWORK, request, routeParamsProvider) -> response(Route.ATTACH_LOAD_BALANCER_TO_NETWORK, HttpStatusCode.Created, defaultHeaders)
        matchRoute(
            Route.DETACH_LOAD_BALANCER_FROM_NETWORK,
            request,
            routeParamsProvider,
        ) -> response(Route.DETACH_LOAD_BALANCER_FROM_NETWORK, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.CHANGE_LOAD_BALANCER_ALGORITHM, request, routeParamsProvider) -> response(Route.CHANGE_LOAD_BALANCER_ALGORITHM, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.CHANGE_LOAD_BALANCER_REVERSE_DNS, request, routeParamsProvider) -> response(Route.CHANGE_LOAD_BALANCER_REVERSE_DNS, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.CHANGE_LOAD_BALANCER_PROTECTION, request, routeParamsProvider) -> response(Route.CHANGE_LOAD_BALANCER_PROTECTION, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.CHANGE_LOAD_BALANCER_TYPE, request, routeParamsProvider) -> response(Route.CHANGE_LOAD_BALANCER_TYPE, HttpStatusCode.Created, defaultHeaders)
        matchRoute(
            Route.ENABLE_LOAD_BALANCER_PUBLIC_INTERFACE,
            request,
            routeParamsProvider,
        ) -> response(Route.ENABLE_LOAD_BALANCER_PUBLIC_INTERFACE, HttpStatusCode.Created, defaultHeaders)
        matchRoute(
            Route.DISABLE_LOAD_BALANCER_PUBLIC_INTERFACE,
            request,
            routeParamsProvider,
        ) -> response(Route.DISABLE_LOAD_BALANCER_PUBLIC_INTERFACE, HttpStatusCode.Created, defaultHeaders)

        matchRoute(Route.GET_ALL_LOAD_BALANCER_TYPES, request, routeParamsProvider) -> response(Route.GET_ALL_LOAD_BALANCER_TYPES, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.GET_LOAD_BALANCER_TYPE, request, routeParamsProvider) -> response(Route.GET_LOAD_BALANCER_TYPE, HttpStatusCode.OK, defaultHeaders)

        matchRoute(Route.GET_ALL_SSH_KEYS, request, routeParamsProvider) -> response(Route.GET_ALL_SSH_KEYS, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.GET_SSH_KEY, request, routeParamsProvider) -> response(Route.GET_SSH_KEY, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.CREATE_SSH_KEY, request, routeParamsProvider) -> response(Route.CREATE_SSH_KEY, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.UPDATE_SSH_KEY, request, routeParamsProvider) -> response(Route.UPDATE_SSH_KEY, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.DELETE_SSH_KEY, request, routeParamsProvider) -> response(Route.DELETE_SSH_KEY, HttpStatusCode.NoContent, defaultHeaders)

        matchRoute(Route.GET_ALL_VOLUMES, request, routeParamsProvider) -> response(Route.GET_ALL_VOLUMES, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.GET_VOLUME, request, routeParamsProvider) -> response(Route.GET_VOLUME, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.CREATE_VOLUME, request, routeParamsProvider) -> response(Route.CREATE_VOLUME, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.UPDATE_VOLUME, request, routeParamsProvider) -> response(Route.UPDATE_VOLUME, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.DELETE_VOLUME, request, routeParamsProvider) -> response(Route.DELETE_VOLUME, HttpStatusCode.NoContent, defaultHeaders)

        matchRoute(Route.GET_ALL_CERTIFICATES, request, routeParamsProvider) -> response(Route.GET_ALL_CERTIFICATES, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.GET_CERTIFICATE, request, routeParamsProvider) -> response(Route.GET_CERTIFICATE, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.GET_CERTIFICATE_ACTIONS, request, routeParamsProvider) -> response(Route.GET_CERTIFICATE_ACTIONS, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.GET_CERTIFICATE_ACTION, request, routeParamsProvider) -> response(Route.GET_CERTIFICATE_ACTION, HttpStatusCode.OK, defaultHeaders)
        matchRoute(
            Route.GET_CERTIFICATE_ACTION_FOR_CERTIFICATE,
            request,
            routeParamsProvider,
        ) -> response(Route.GET_CERTIFICATE_ACTION_FOR_CERTIFICATE, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.GET_ALL_CERTIFICATE_ACTIONS, request, routeParamsProvider) -> response(Route.GET_ALL_CERTIFICATE_ACTIONS, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.CREATE_CERTIFICATE, request, routeParamsProvider) -> response(Route.CREATE_CERTIFICATE, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.UPDATE_CERTIFICATE, request, routeParamsProvider) -> response(Route.UPDATE_CERTIFICATE, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.DELETE_CERTIFICATE, request, routeParamsProvider) -> response(Route.DELETE_CERTIFICATE, HttpStatusCode.NoContent, defaultHeaders)
        matchRoute(
            Route.RETRY_CERTIFICATE_ISSUANCE_OR_RENEWAL,
            request,
            routeParamsProvider,
        ) -> response(Route.RETRY_CERTIFICATE_ISSUANCE_OR_RENEWAL, HttpStatusCode.Created, defaultHeaders)

        matchRoute(Route.GET_ALL_FIREWALLS, request, routeParamsProvider) -> response(Route.GET_ALL_FIREWALLS, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.GET_FIREWALL, request, routeParamsProvider) -> response(Route.GET_FIREWALL, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.CREATE_FIREWALL, request, routeParamsProvider) -> response(Route.CREATE_FIREWALL, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.UPDATE_FIREWALL, request, routeParamsProvider) -> response(Route.UPDATE_FIREWALL, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.DELETE_FIREWALL, request, routeParamsProvider) -> response(Route.DELETE_FIREWALL, HttpStatusCode.NoContent, defaultHeaders)

        matchRoute(Route.GET_ALL_PRIMARY_IPS, request, routeParamsProvider) -> response(Route.GET_ALL_PRIMARY_IPS, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.GET_PRIMARY_IP, request, routeParamsProvider) -> response(Route.GET_PRIMARY_IP, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.CREATE_PRIMARY_IP, request, routeParamsProvider) -> response(Route.CREATE_PRIMARY_IP, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.UPDATE_PRIMARY_IP, request, routeParamsProvider) -> response(Route.UPDATE_PRIMARY_IP, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.DELETE_PRIMARY_IP, request, routeParamsProvider) -> response(Route.DELETE_PRIMARY_IP, HttpStatusCode.NoContent, defaultHeaders)

        matchRoute(Route.GET_ALL_FLOATING_IPS, request, routeParamsProvider) -> response(Route.GET_ALL_FLOATING_IPS, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.GET_FLOATING_IP, request, routeParamsProvider) -> response(Route.GET_FLOATING_IP, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.CREATE_FLOATING_IP, request, routeParamsProvider) -> response(Route.CREATE_FLOATING_IP, HttpStatusCode.Created, defaultHeaders)
        matchRoute(Route.UPDATE_FLOATING_IP, request, routeParamsProvider) -> response(Route.UPDATE_FLOATING_IP, HttpStatusCode.OK, defaultHeaders)
        matchRoute(Route.DELETE_FLOATING_IP, request, routeParamsProvider) -> response(Route.DELETE_FLOATING_IP, HttpStatusCode.NoContent, defaultHeaders)

        matchRoute(Route.GET_ALL_PRICES, request, routeParamsProvider) -> response(Route.GET_ALL_PRICES, HttpStatusCode.OK, defaultHeaders)

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

private fun matchRoute(route: Route, request: HttpRequestData, routeParamsProvider: ((HttpRequestData) -> RouteParams)? = null): Boolean {
    val routeParams = routeParamsProvider?.invoke(request)

    val testPath = Path(request.url.toURI().pathWithoutVersion)

    val test = HttpMethodAndPath(request.method, testPath)

    return if (routeParams != null) {
        val (httpMethod, path) = route.value
        httpMethod == test.first && path.withParams(routeParams) == test.second
    } else {
        route.value == test
    }
}

@Suppress("CyclomaticComplexMethod", "LongMethod")
private fun content(route: Route): String = when (route) {
    Route.GET_ALL_ACTIONS -> "src/test/resources/examples/response/action/get_all_actions.json"
    Route.GET_ACTION -> "src/test/resources/examples/response/action/get_an_action.json"

    Route.GET_ALL_SERVERS -> "src/test/resources/examples/response/server/get_all_servers.json"
    Route.GET_SERVER -> "src/test/resources/examples/response/server/get_a_server.json"
    Route.GET_SERVER_METRICS -> "src/test/resources/examples/response/server/get_server_metrics.json"
    Route.GET_SERVER_ACTIONS -> "src/test/resources/examples/response/server/get_server_actions.json"
    Route.GET_SERVER_ACTION -> "src/test/resources/examples/response/server/get_a_server_action.json"
    Route.GET_SERVER_ACTION_FOR_SERVER -> "src/test/resources/examples/response/server/get_a_server_action_for_server.json"
    Route.GET_ALL_SERVER_ACTIONS -> "src/test/resources/examples/response/server/get_all_server_actions.json"
    Route.CREATE_SERVER -> "src/test/resources/examples/response/server/create_a_server.json"
    Route.UPDATE_SERVER -> "src/test/resources/examples/response/server/update_a_server.json"
    Route.DELETE_SERVER -> "src/test/resources/examples/response/server/delete_a_server.json"
    Route.ADD_SERVER_TO_PLACEMENT_GROUP -> "src/test/resources/examples/response/server/add_a_server_to_placement_group.json"
    Route.ATTACH_ISO_TO_SERVER -> "src/test/resources/examples/response/server/attach_an_iso_to_server.json"
    Route.DETACH_ISO_FROM_SERVER -> "src/test/resources/examples/response/server/detach_an_iso_from_server.json"
    Route.ATTACH_SERVER_TO_NETWORK -> "src/test/resources/examples/response/server/attach_a_server_to_network.json"
    Route.DETACH_SERVER_FROM_NETWORK -> "src/test/resources/examples/response/server/detach_server_from_network.json"
    Route.SHUTDOWN_SERVER -> "src/test/resources/examples/response/server/shutdown_a_server.json"
    Route.RESET_SERVER -> "src/test/resources/examples/response/server/reset_a_server.json"
    Route.SOFT_REBOOT_SERVER -> "src/test/resources/examples/response/server/soft_reboot_a_server.json"
    Route.POWER_ON_SERVER -> "src/test/resources/examples/response/server/power_on_a_server.json"
    Route.POWER_OFF_SERVER -> "src/test/resources/examples/response/server/power_off_a_server.json"
    Route.RESET_SERVER_ROOT_PASSWORD -> "src/test/resources/examples/response/server/reset_root_password_for_a_server.json"
    Route.REQUEST_CONSOLE_FOR_SERVER -> "src/test/resources/examples/response/server/request_console_for_a_server.json"
    Route.REMOVE_SERVER_FROM_PLACEMENT_GROUP -> "src/test/resources/examples/response/server/remove_server_from_a_placement_group.json"
    Route.REBUILD_SERVER_FROM_IMAGE -> "src/test/resources/examples/response/server/rebuild_server_from_an_image.json"
    Route.ENABLE_SERVER_BACKUP -> "src/test/resources/examples/response/server/enable_server_backup.json"
    Route.DISABLE_SERVER_BACKUP -> "src/test/resources/examples/response/server/disable_server_backup.json"
    Route.ENABLE_SERVER_RESCUE_MODE -> "src/test/resources/examples/response/server/enable_rescue_mode_for_server.json"
    Route.DISABLE_SERVER_RESCUE_MODE -> "src/test/resources/examples/response/server/disable_rescue_mode_for_server.json"
    Route.CREATE_IMAGE_FROM_SERVER -> "src/test/resources/examples/response/server/create_an_image_from_server.json"
    Route.CHANGE_SERVER_TYPE -> "src/test/resources/examples/response/server/change_server_type.json"
    Route.CHANGE_SERVER_PROTECTION -> "src/test/resources/examples/response/server/change_server_protection.json"
    Route.CHANGE_SERVER_REVERSE_DNS -> "src/test/resources/examples/response/server/change_server_reverse_dns.json"
    Route.CHANGE_SERVER_ALIAS_IP_OF_NETWORK -> "src/test/resources/examples/response/server/change_alias_ips_of_a_network.json"

    Route.GET_ALL_SERVER_TYPES -> "src/test/resources/examples/response/server/get_all_server_types.json"
    Route.GET_SERVER_TYPE -> "src/test/resources/examples/response/server/get_a_server_type.json"

    Route.GET_ALL_DATACENTERS -> "src/test/resources/examples/response/datacenter/get_all_datacenters.json"
    Route.GET_DATACENTER -> "src/test/resources/examples/response/datacenter/get_a_datacenter.json"

    Route.GET_ALL_ISOS -> "src/test/resources/examples/response/iso/get_all_isos.json"
    Route.GET_ISO -> "src/test/resources/examples/response/iso/get_an_iso.json"

    Route.GET_ALL_IMAGES -> "src/test/resources/examples/response/image/get_all_images.json"
    Route.GET_IMAGE -> "src/test/resources/examples/response/image/get_an_image.json"
    Route.GET_IMAGE_ACTIONS -> "src/test/resources/examples/response/image/get_image_actions.json"
    Route.GET_IMAGE_ACTION -> "src/test/resources/examples/response/image/get_an_image_action.json"
    Route.GET_IMAGE_ACTION_FOR_IMAGE -> "src/test/resources/examples/response/image/get_an_image_action_for_image.json"
    Route.GET_ALL_IMAGE_ACTIONS -> "src/test/resources/examples/response/image/get_all_image_actions.json"
    Route.UPDATE_IMAGE -> "src/test/resources/examples/response/image/update_an_image.json"
    Route.DELETE_IMAGE -> "src/test/resources/examples/response/no_content.json"
    Route.CHANGE_IMAGE_PROTECTION -> "src/test/resources/examples/response/image/change_image_protection.json"

    Route.GET_ALL_PLACEMENT_GROUPS -> "src/test/resources/examples/response/placement_group/get_all_placement_groups.json"
    Route.GET_A_PLACEMENT_GROUP -> "src/test/resources/examples/response/placement_group/get_a_placement_group.json"
    Route.CREATE_PLACEMENT_GROUP -> "src/test/resources/examples/response/placement_group/create_a_placement_group.json"
    Route.UPDATE_PLACEMENT_GROUP -> "src/test/resources/examples/response/placement_group/update_a_placement_group.json"
    Route.DELETE_PLACEMENT_GROUP -> "src/test/resources/examples/response/no_content.json"

    Route.GET_ALL_NETWORKS -> "src/test/resources/examples/response/network/get_all_networks.json"
    Route.GET_NETWORK -> "src/test/resources/examples/response/network/get_a_network.json"
    Route.GET_NETWORK_ACTIONS -> "src/test/resources/examples/response/network/get_network_actions.json"
    Route.GET_NETWORK_ACTION -> "src/test/resources/examples/response/network/get_a_network_action.json"
    Route.GET_NETWORK_ACTION_FOR_NETWORK -> "src/test/resources/examples/response/network/get_a_network_action_for_network.json"
    Route.GET_ALL_NETWORK_ACTIONS -> "src/test/resources/examples/response/network/get_all_network_actions.json"
    Route.CREATE_NETWORK -> "src/test/resources/examples/response/network/create_a_network.json"
    Route.UPDATE_NETWORK -> "src/test/resources/examples/response/network/update_a_network.json"
    Route.DELETE_NETWORK -> "src/test/resources/examples/response/no_content.json"
    Route.ADD_ROUTE_TO_NETWORK -> "src/test/resources/examples/response/network/add_a_route_to_network.json"
    Route.DELETE_ROUTE_FROM_NETWORK -> "src/test/resources/examples/response/network/delete_a_route_from_network.json"
    Route.ADD_SUBNET_TO_NETWORK -> "src/test/resources/examples/response/network/add_a_subnet_to_network.json"
    Route.DELETE_SUBNET_FROM_NETWORK -> "src/test/resources/examples/response/network/delete_a_subnet_from_network.json"
    Route.CHANGE_NETWORK_IP_RANGE -> "src/test/resources/examples/response/network/change_ip_range_of_network.json"
    Route.CHANGE_NETWORK_PROTECTION -> "src/test/resources/examples/response/network/change_network_protection.json"

    Route.GET_ALL_LOAD_BALANCERS -> "src/test/resources/examples/response/load_balancer/get_all_load_balancers.json"
    Route.GET_LOAD_BALANCER -> "src/test/resources/examples/response/load_balancer/get_a_load_balancer.json"
    Route.GET_LOAD_BALANCER_ACTIONS -> "src/test/resources/examples/response/load_balancer/get_load_balancer_actions.json"
    Route.GET_LOAD_BALANCER_ACTION -> "src/test/resources/examples/response/load_balancer/get_a_load_balancer_action.json"
    Route.GET_LOAD_BALANCER_ACTION_FOR_LOAD_BALANCER -> "src/test/resources/examples/response/load_balancer/get_a_load_balancer_action_for_load_balancer.json"
    Route.GET_ALL_LOAD_BALANCER_ACTIONS -> "src/test/resources/examples/response/load_balancer/get_all_load_balancer_actions.json"
    Route.CREATE_LOAD_BALANCER -> "src/test/resources/examples/response/load_balancer/create_a_load_balancer.json"
    Route.UPDATE_LOAD_BALANCER -> "src/test/resources/examples/response/load_balancer/update_a_load_balancer.json"
    Route.DELETE_LOAD_BALANCER -> "src/test/resources/examples/response/no_content.json"
    Route.LOAD_BALANCER_ADD_SERVICE -> "src/test/resources/examples/response/load_balancer/load_balancer_add_service.json"
    Route.LOAD_BALANCER_ADD_TARGET -> "src/test/resources/examples/response/load_balancer/load_balancer_add_target.json"
    Route.ATTACH_LOAD_BALANCER_TO_NETWORK -> "src/test/resources/examples/response/load_balancer/attach_a_load_balancer_to_network.json"
    Route.DETACH_LOAD_BALANCER_FROM_NETWORK -> "src/test/resources/examples/response/load_balancer/detach_a_load_balancer_from_network.json"
    Route.CHANGE_LOAD_BALANCER_ALGORITHM -> "src/test/resources/examples/response/load_balancer/change_load_balancer_algorithm.json"
    Route.CHANGE_LOAD_BALANCER_REVERSE_DNS -> "src/test/resources/examples/response/load_balancer/change_load_balancer_reverse_dns.json"
    Route.CHANGE_LOAD_BALANCER_PROTECTION -> "src/test/resources/examples/response/load_balancer/change_load_balancer_protection.json"
    Route.CHANGE_LOAD_BALANCER_TYPE -> "src/test/resources/examples/response/load_balancer/change_load_balancer_type.json"
    Route.LOAD_BALANCER_DELETE_SERVICE -> "src/test/resources/examples/response/load_balancer/load_balancer_delete_service.json"
    Route.ENABLE_LOAD_BALANCER_PUBLIC_INTERFACE -> "src/test/resources/examples/response/load_balancer/enable_load_balancer_public_interface.json"
    Route.DISABLE_LOAD_BALANCER_PUBLIC_INTERFACE -> "src/test/resources/examples/response/load_balancer/disable_load_balancer_public_interface.json"
    Route.LOAD_BALANCER_UPDATE_SERVICE -> "src/test/resources/examples/response/load_balancer/load_balancer_update_a_service.json"
    Route.LOAD_BALANCER_REMOVE_TARGET -> "src/test/resources/examples/response/load_balancer/load_balancer_remove_a_target.json"

    Route.GET_ALL_LOAD_BALANCER_TYPES -> "src/test/resources/examples/response/load_balancer/get_all_load_balancer_types.json"
    Route.GET_LOAD_BALANCER_TYPE -> "src/test/resources/examples/response/load_balancer/get_a_load_balancer_type.json"

    Route.GET_ALL_SSH_KEYS -> "src/test/resources/examples/response/ssh_key/get_all_ssh_keys.json"
    Route.GET_SSH_KEY -> "src/test/resources/examples/response/ssh_key/get_an_ssh_key.json"
    Route.CREATE_SSH_KEY -> "src/test/resources/examples/response/ssh_key/create_an_ssh_key.json"
    Route.UPDATE_SSH_KEY -> "src/test/resources/examples/response/ssh_key/update_an_ssh_key.json"
    Route.DELETE_SSH_KEY -> "src/test/resources/examples/response/no_content.json"

    Route.GET_ALL_VOLUMES -> "src/test/resources/examples/response/volume/get_all_volumes.json"
    Route.GET_VOLUME -> "src/test/resources/examples/response/volume/get_a_volume.json"
    Route.GET_VOLUME_ACTIONS -> TODO()
    Route.GET_VOLUME_ACTION -> TODO()
    Route.GET_VOLUME_ACTION_FOR_VOLUME -> TODO()
    Route.GET_ALL_VOLUME_ACTIONS -> TODO()
    Route.CREATE_VOLUME -> "src/test/resources/examples/response/volume/create_a_volume.json"
    Route.UPDATE_VOLUME -> "src/test/resources/examples/response/volume/update_a_volume.json"
    Route.DELETE_VOLUME -> "src/test/resources/examples/response/no_content.json"

    Route.GET_ALL_CERTIFICATES -> "src/test/resources/examples/response/certificate/get_all_certificates.json"
    Route.GET_CERTIFICATE -> "src/test/resources/examples/response/certificate/get_a_certificate.json"
    Route.GET_CERTIFICATE_ACTIONS -> "src/test/resources/examples/response/certificate/get_certificate_actions.json"
    Route.GET_CERTIFICATE_ACTION -> "src/test/resources/examples/response/certificate/get_a_certificate_action.json"
    Route.GET_CERTIFICATE_ACTION_FOR_CERTIFICATE -> "src/test/resources/examples/response/certificate/get_a_certificate_action_for_certificate.json"
    Route.GET_ALL_CERTIFICATE_ACTIONS -> "src/test/resources/examples/response/certificate/get_all_certificate_actions.json"
    Route.CREATE_CERTIFICATE -> "src/test/resources/examples/response/certificate/create_a_managed_certificate.json"
    Route.UPDATE_CERTIFICATE -> "src/test/resources/examples/response/certificate/update_a_certificate.json"
    Route.DELETE_CERTIFICATE -> "src/test/resources/examples/response/no_content.json"
    Route.RETRY_CERTIFICATE_ISSUANCE_OR_RENEWAL -> "src/test/resources/examples/response/certificate/retry_certificate_issuance_or_renewal.json"

    Route.GET_ALL_FIREWALLS -> "src/test/resources/examples/response/firewall/get_all_firewalls.json"
    Route.GET_FIREWALL -> "src/test/resources/examples/response/firewall/get_a_firewall.json"
    Route.GET_FIREWALL_ACTIONS -> TODO()
    Route.GET_FIREWALL_ACTION -> TODO()
    Route.GET_FIREWALL_ACTION_FOR_FIREWALL -> TODO()
    Route.GET_ALL_FIREWALL_ACTIONS -> TODO()
    Route.CREATE_FIREWALL -> "src/test/resources/examples/response/firewall/create_a_firewall.json"
    Route.UPDATE_FIREWALL -> "src/test/resources/examples/response/firewall/update_a_firewall.json"
    Route.DELETE_FIREWALL -> "src/test/resources/examples/response/no_content.json"

    Route.GET_ALL_PRIMARY_IPS -> "src/test/resources/examples/response/primary_ip/get_all_primary_ips.json"
    Route.GET_PRIMARY_IP -> "src/test/resources/examples/response/primary_ip/get_a_primary_ip.json"
    Route.GET_PRIMARY_IP_ACTIONS -> TODO()
    Route.GET_PRIMARY_IP_ACTION -> TODO()
    Route.GET_PRIMARY_IP_ACTION_FOR_PRIMARY_IP -> TODO()
    Route.GET_ALL_PRIMARY_IP_ACTIONS -> TODO()
    Route.CREATE_PRIMARY_IP -> "src/test/resources/examples/response/primary_ip/create_a_primary_ip.json"
    Route.UPDATE_PRIMARY_IP -> "src/test/resources/examples/response/primary_ip/update_a_primary_ip.json"
    Route.DELETE_PRIMARY_IP -> "src/test/resources/examples/response/no_content.json"

    Route.GET_ALL_FLOATING_IPS -> "src/test/resources/examples/response/floating_ip/get_all_floating_ips.json"
    Route.GET_FLOATING_IP -> "src/test/resources/examples/response/floating_ip/get_a_floating_ip.json"
    Route.GET_FLOATING_IP_ACTIONS -> TODO()
    Route.GET_FLOATING_IP_ACTION -> TODO()
    Route.GET_FLOATING_IP_ACTION_FOR_FLOATING_IP -> TODO()
    Route.GET_ALL_FLOATING_IP_ACTIONS -> TODO()
    Route.CREATE_FLOATING_IP -> "src/test/resources/examples/response/floating_ip/create_a_floating_ip.json"
    Route.UPDATE_FLOATING_IP -> "src/test/resources/examples/response/floating_ip/update_a_floating_ip.json"
    Route.DELETE_FLOATING_IP -> "src/test/resources/examples/response/no_content.json"

    Route.GET_ALL_PRICES -> "src/test/resources/examples/response/get_all_prices.json"
}.let {
    File(it).readText(Charsets.UTF_8)
}

@Suppress("CyclomaticComplexMethod")
private fun error(code: ErrorCode): String = when (code) {
    ErrorCode.NOT_FOUND -> "src/test/resources/examples/error/not_found.json"
    ErrorCode.FORBIDDEN -> "src/test/resources/examples/error/forbidden.json"
    ErrorCode.UNAUTHORIZED -> "src/test/resources/examples/error/unauthorized.json"
    ErrorCode.METHOD_NOT_ALLOWED -> "src/test/resources/examples/error/method_not_allowed.json"
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
    ErrorCode.IP_NOT_AVAILABLE -> "src/test/resources/examples/error/ip_not_available.json"
    ErrorCode.SUBNET_NOT_AVAILABLE -> "src/test/resources/examples/error/no_subnet_available.json"
    ErrorCode.SERVER_ALREADY_ATTACHED_TO_NETWORK -> "src/test/resources/examples/error/server_already_attached.json"
    ErrorCode.NETWORKS_OVERLAP -> "src/test/resources/examples/error/networks_overlap.json"
    ErrorCode.INVALID_SERVER_TYPE -> "src/test/resources/examples/error/invalid_server_type.json"
    ErrorCode.CAA_RECORD_DOES_NOT_ALLOW_CA -> TODO()
    ErrorCode.CA_DNS_VALIDATION_FAILED -> TODO()
    ErrorCode.CA_TOO_MANY_AUTHORIZATIONS_FAILED -> TODO()
    ErrorCode.CA_TOO_MANY_CERTIFICATES_ISSUED_FOR_DOMAIN -> TODO()
    ErrorCode.CA_TOO_MANY_DUPLICATE_CERTIFICATES -> TODO()
    ErrorCode.COULD_NOT_VERIFY_DOMAIN_DELEGATE_TO_ZONE -> TODO()
    ErrorCode.DNS_ZONE_NOT_FOUND -> TODO()
    ErrorCode.DNS_ZONE_IS_SECONDARY_ZONE -> TODO()
}.let {
    File(it).readText(Charsets.UTF_8)
}

private fun MockRequestHandleScope.response(route: Route, statusCode: HttpStatusCode, headers: Map<String, String>): HttpResponseData = respond(
    content = ByteReadChannel(content(route)),
    status = statusCode,
    headers = headers {
        headers.forEach {
            append(it.key, it.value)
        }
    },
)

private fun MockRequestHandleScope.errorResponse(error: ErrorCode, statusCode: HttpStatusCode, headers: Map<String, String>): HttpResponseData = respond(
    content = ByteReadChannel(error(error)),
    status = statusCode,
    headers = headers {
        headers.forEach {
            append(it.key, it.value)
        }
    },
)

internal val URI.pathWithoutVersion: String get() = path.replace("/v1", "")
