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
import java.io.File
import java.nio.file.Paths

internal fun createMockEngine(apiToken: ApiToken, resourceIdProvider: ((HttpRequestData) -> Long)? = null) =
    MockEngine { request ->

        if (request.headers["Authorization"] != "Bearer ${apiToken.value}") {
            return@MockEngine respondError(HttpStatusCode.Unauthorized)
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

private fun matchRoute(route: Route, test: HttpMethodAndPath, resourceId: Long?) = if (resourceId != null) {
    val (httpMethod, path) = route.value
    httpMethod == test.first && path.withId(resourceId).value == test.second.value
} else {
    route.value == test
}

private fun content(route: Route): String =
    mapOf(
        Route.GET_ALL_SERVERS to "src/test/resources/examples/response/get_all_servers.json",
        Route.GET_SERVER to "src/test/resources/examples/response/get_a_server.json",
        Route.GET_SERVER_METRICS to "src/test/resources/examples/response/get_server_metrics.json",
        Route.CREATE_SERVER to "src/test/resources/examples/response/create_a_server.json",
        Route.UPDATE_SERVER to "src/test/resources/examples/response/update_a_server.json",
        Route.DELETE_SERVER to "src/test/resources/examples/response/delete_a_server.json",

        Route.GET_ALL_SERVER_TYPES to "src/test/resources/examples/response/get_all_server_types.json",
        Route.GET_SERVER_TYPE to "src/test/resources/examples/response/get_a_server_type.json",

        Route.GET_ALL_DATACENTERS to "src/test/resources/examples/response/get_all_datacenters.json",
        Route.GET_DATACENTER to "src/test/resources/examples/response/get_a_datacenter.json",

        Route.GET_ALL_ISOS to "src/test/resources/examples/response/get_all_isos.json",
        Route.GET_ISO to "src/test/resources/examples/response/get_an_iso.json",

        Route.GET_ALL_IMAGES to "src/test/resources/examples/response/get_all_images.json",
        Route.GET_IMAGE to "src/test/resources/examples/response/get_an_image.json",
        Route.UPDATE_IMAGE to "src/test/resources/examples/response/update_an_image.json",
        Route.DELETE_IMAGE to "src/test/resources/examples/response/no_content.json",
    ).let {
        val filePath = Paths.get(it[route]!!).toAbsolutePath().toString()

        File(filePath).readText(Charsets.UTF_8)
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
