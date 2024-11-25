package tech.sco.hetznerkloud.api

import io.ktor.client.HttpClient
import tech.sco.hetznerkloud.Failure
import tech.sco.hetznerkloud.Route
import tech.sco.hetznerkloud.makeRequest
import tech.sco.hetznerkloud.model.Pricing
import tech.sco.hetznerkloud.response.Item

class Pricing(private val httpClient: HttpClient) {
    @Throws(Failure::class)
    suspend fun all(): Item<Pricing> = httpClient.makeRequest(Route.GET_ALL_PRICES)
}
