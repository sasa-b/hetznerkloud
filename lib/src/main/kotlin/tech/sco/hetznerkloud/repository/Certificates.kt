package tech.sco.hetznerkloud.repository

import io.ktor.client.HttpClient
import io.ktor.utils.io.InternalAPI
import tech.sco.hetznerkloud.Route
import tech.sco.hetznerkloud.makeRequest
import tech.sco.hetznerkloud.model.Certificate
import tech.sco.hetznerkloud.model.Certificate.Id
import tech.sco.hetznerkloud.request.CertificateFilter
import tech.sco.hetznerkloud.request.CertificateSorting
import tech.sco.hetznerkloud.request.CreateCertificate
import tech.sco.hetznerkloud.request.Pagination
import tech.sco.hetznerkloud.request.UpdateResource
import tech.sco.hetznerkloud.request.toQueryParams
import tech.sco.hetznerkloud.response.Failure
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.ItemCreated
import tech.sco.hetznerkloud.response.Items

class Certificates @InternalAPI constructor(private val httpClient: HttpClient) {

    @Throws(Failure::class)
    suspend fun all(
        filter: Set<CertificateFilter> = emptySet(),
        sorting: Set<CertificateSorting> = emptySet(),
        pagination: Pagination = Pagination(),
    ): Items<Certificate> = httpClient.makeRequest(Route.GET_ALL_CERTIFICATES, queryParams = filter.toQueryParams() + sorting.toQueryParams() + pagination.toQueryParams())

    @Throws(Failure::class)
    suspend fun find(id: Id): Item<Certificate> = httpClient.makeRequest(Route.GET_CERTIFICATE, id.value)

    @Throws(Failure::class)
    suspend fun create(body: CreateCertificate): ItemCreated<Certificate> = httpClient.makeRequest(Route.CREATE_CERTIFICATE, body = body)

    @Throws(Failure::class)
    suspend fun update(id: Id, body: UpdateResource): Item<Certificate> = httpClient.makeRequest(Route.UPDATE_CERTIFICATE, resourceId = id.value, body = body)

    @Throws(Failure::class)
    suspend fun delete(id: Id): Unit = httpClient.makeRequest(Route.DELETE_CERTIFICATE)
}
