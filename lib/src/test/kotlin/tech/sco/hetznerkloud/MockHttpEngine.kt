package tech.sco.hetznerkloud

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.request.HttpResponseData
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import io.ktor.utils.io.ByteReadChannel
import java.io.File
import java.nio.file.Paths

internal fun createMockEngine(apiToken: ApiToken) =
    MockEngine { request ->

        if (request.headers["Authorization"] != "Bearer ${apiToken.value}") {
            return@MockEngine respondError(HttpStatusCode.Unauthorized)
        }

        val defaultHeaders =
            mapOf(
                HttpHeaders.ContentType to "application/json",
                HttpHeaders.Authorization to apiToken.toString(),
            )

        val test = HttpMethodAndPath(request.method, "/${request.url.segments.last()}")

        when {
            Route.GET_ALL_SERVERS.value == test ->
                response(
                    Route.GET_ALL_SERVERS,
                    HttpStatusCode.OK,
                    defaultHeaders,
                )
            Route.GET_ALL_DATACENTERS.value == test ->
                response(
                    Route.GET_ALL_DATACENTERS,
                    HttpStatusCode.OK,
                    defaultHeaders,
                )
            Route.GET_ALL_IMAGES.value == test ->
                response(
                    Route.GET_ALL_IMAGES,
                    HttpStatusCode.OK,
                    defaultHeaders,
                )
            Route.GET_ALL_ISOS.value == test ->
                response(
                    Route.GET_ALL_DATACENTERS,
                    HttpStatusCode.OK,
                    defaultHeaders,
                )
            else -> respondError(HttpStatusCode.NotFound)
        }
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

private fun content(route: Route): String =
    mapOf(
        Route.GET_ALL_SERVERS to "src/test/resources/examples/response/get_all_servers.json",
        Route.GET_ALL_DATACENTERS to "src/test/resources/examples/response/get_all_datacenters.json",
        Route.GET_ALL_ISOS to "src/test/resources/examples/response/get_all_servers.json",
        Route.GET_ALL_IMAGES to "src/test/resources/examples/response/get_all_servers.json",
    ).let {
        val filePath = Paths.get(it[route]!!).toAbsolutePath().toString()

        File(filePath).readText(Charsets.UTF_8)
    }
