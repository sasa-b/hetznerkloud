package tech.sco.hetznerkloud

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.http.parameters
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.valuesOf
import tech.sco.hetznerkloud.model.RateLimitExceededError
import tech.sco.hetznerkloud.request.HttpBody
import tech.sco.hetznerkloud.response.Error

typealias QueryParams = List<Pair<String, String>>
typealias RouteParams = Map<String, String>

internal suspend inline fun <reified T> HttpClient.makeRequest(route: Route, routeParams: RouteParams, body: HttpBody? = null, queryParams: QueryParams = emptyList()): T =
    route.value.let {
        val (httpMethod, path) = it

        this.request {
            method = httpMethod
            url {
                appendPathSegments(path.withParams(routeParams).toSegments())
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

internal suspend inline fun <reified T> HttpClient.makeRequest(route: Route, resourceId: Long? = null, body: HttpBody? = null, queryParams: QueryParams = emptyList()): T =
    route.value.let {
        val (httpMethod, path) = it

        this.request {
            method = httpMethod
            url {
                if (resourceId != null) {
                    appendPathSegments(path.withParams(mapOf("id" to resourceId.toString())).toSegments())
                } else {
                    appendPathSegments(path.toSegments())
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

internal fun configureHttpClient(token: ApiToken, httpEngine: HttpClientEngine, block: HttpClientConfig<*>.() -> Unit = {}) = HttpClient(httpEngine) {
    // throws RedirectResponseException, ClientRequestException, ServerResponseException
    // on 3xx, 4xx and 5xx status codes
    expectSuccess = true

    install(ContentNegotiation) {
        json(jsonEncoder())
    }

    install(Auth) {
        bearer {
            loadTokens {
                BearerTokens(accessToken = token.value, refreshToken = null)
            }
        }
    }

    HttpResponseValidator {
        handleResponseExceptionWithRequest { exception, request ->
            when (exception) {
                is ResponseException -> {
                    val errorResponse: Error = exception.response.body()

                    val error = when (errorResponse.error) {
                        is RateLimitExceededError -> {
                            val headers = exception.response.headers

                            errorResponse.error.copy(
                                hourlyRateLimit = headers["RateLimit-Limit"]?.toIntOrNull() ?: 3600,
                                hourlyRateLimitRemaining = headers["RateLimit-Remaining"]?.toIntOrNull(),
                                hourlyRateLimitResetTimestamp = headers["RateLimit-Reset"]?.toLongOrNull(),
                            )
                        }

                        else -> errorResponse.error
                    }

                    throw Failure(error, request)
                }
            }
        }
    }

    block()
}
