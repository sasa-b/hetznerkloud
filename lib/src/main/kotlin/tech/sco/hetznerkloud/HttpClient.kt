package tech.sco.hetznerkloud

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import io.ktor.http.parameters
import tech.sco.hetznerkloud.model.ResourceId
import tech.sco.hetznerkloud.request.HttpBody

typealias QueryParams = List<Pair<String, String>>

internal suspend inline fun <reified T> HttpClient.makeRequest(route: Route, resourceId: ResourceId? = null, body: HttpBody? = null, queryParams: QueryParams = emptyList()): T =
    route.value.let {
        val (httpMethod, path) = it

        this
            .request(BASE_URL) {
                method = httpMethod
                url {
                    if (resourceId != null) {
                        appendPathSegments(path.withId(resourceId).value)
                    } else {
                        appendPathSegments(path.value)
                    }
                }
                if (body != null) {
                    contentType(ContentType.Application.Json)
                    setBody(body)
                }
                if (queryParams.isNotEmpty()) {
                    parameters {
                        queryParams.forEach { (key, value) -> append(key, value) }
                    }
                }
            }.body()
    }
