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
import tech.sco.hetznerkloud.model.ResourceId
import java.io.File

@Suppress("CyclomaticComplexMethod")
internal fun createMockEngine(apiToken: ApiToken, resourceIdProvider: ((HttpRequestData) -> ResourceId)? = null) =
    MockEngine { request ->

        if (request.headers["Authorization"] != "Bearer ${apiToken.value}") {
            return@MockEngine errorResponse(ErrorCode.UNAUTHORIZED, HttpStatusCode.Unauthorized, mapOf(HttpHeaders.ContentType to "application/json"))
        }

        val defaultHeaders =
            mapOf(
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

            else -> respondError(HttpStatusCode.NotFound)
        }
    }

private fun matchRoute(route: Route, test: HttpMethodAndPath, resourceId: ResourceId?) = if (resourceId != null) {
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
        headers =
        headers {
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
        headers =
        headers {
            headers.forEach {
                append(it.key, it.value)
            }
        },
    )
