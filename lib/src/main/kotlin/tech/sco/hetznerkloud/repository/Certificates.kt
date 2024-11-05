package tech.sco.hetznerkloud.repository

import io.ktor.client.HttpClient
import tech.sco.hetznerkloud.Route
import tech.sco.hetznerkloud.makeRequest
import tech.sco.hetznerkloud.model.Certificate
import tech.sco.hetznerkloud.model.Certificate.Id
import tech.sco.hetznerkloud.model.Error
import tech.sco.hetznerkloud.request.CreateCertificate
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.ItemCreated
import tech.sco.hetznerkloud.response.Items

class Certificates(private val httpClient: HttpClient) {

    @Throws(Error::class)
    suspend fun all(): Items<Certificate> = httpClient.makeRequest(Route.GET_ALL_CERTIFICATES)

    @Throws(Error::class)
    suspend fun find(id: Id): Item<Certificate> = httpClient.makeRequest(Route.GET_CERTIFICATE, id)

    @Throws(Error::class)
    suspend fun create(body: CreateCertificate): ItemCreated<Certificate> = httpClient.makeRequest(Route.CREATE_CERTIFICATE, body = body)
}
