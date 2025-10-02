package tech.sco.hetznerkloud

import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.URLBuilder
import io.ktor.http.Url
import io.ktor.http.appendPathSegments
import io.ktor.http.encodedPath
import io.ktor.http.parseUrl
import io.ktor.http.path
import io.ktor.http.takeFrom
import tech.sco.hetznerkloud.api.Actions
import tech.sco.hetznerkloud.api.Certificates
import tech.sco.hetznerkloud.api.Datacenters
import tech.sco.hetznerkloud.api.Firewalls
import tech.sco.hetznerkloud.api.FloatingIps
import tech.sco.hetznerkloud.api.Images
import tech.sco.hetznerkloud.api.Isos
import tech.sco.hetznerkloud.api.LoadBalancerTypes
import tech.sco.hetznerkloud.api.LoadBalancers
import tech.sco.hetznerkloud.api.Networks
import tech.sco.hetznerkloud.api.PlacementGroups
import tech.sco.hetznerkloud.api.Pricing
import tech.sco.hetznerkloud.api.PrimaryIps
import tech.sco.hetznerkloud.api.SSHKeys
import tech.sco.hetznerkloud.api.ServerTypes
import tech.sco.hetznerkloud.api.Servers
import tech.sco.hetznerkloud.api.Volumes
import java.net.URL

@Suppress("LongParameterList")
@OptIn(InternalAPI::class)
class CloudApiClient private constructor(
    val actions: Actions,
    val servers: Servers,
    val serverTypes: ServerTypes,
    val images: Images,
    val isos: Isos,
    val datacenters: Datacenters,
    val placementGroups: PlacementGroups,
    val networks: Networks,
    val loadBalancers: LoadBalancers,
    val loadBalancerTypes: LoadBalancerTypes,
    val sshKeys: SSHKeys,
    val volumes: Volumes,
    val certificates: Certificates,
    val firewalls: Firewalls,
    val primaryIps: PrimaryIps,
    val floatingIps: FloatingIps,
    val pricing: Pricing,
) {
    companion object {
        const val BASE_URL = "https://api.hetzner.cloud"

        @Suppress("LongMethod")
        fun of(token: ApiToken, httpEngine: HttpClientEngine = CIO.create(), block: HttpClientConfig<*>.() -> Unit = {}): CloudApiClient = configureHttpClient(token, httpEngine, {
            block()
            defaultRequest {
                // Only sets scheme and host, disregards path
                url(BASE_URL)
            }
        }).let { httpClient ->
            CloudApiClient(
                actions = Actions(httpClient),
                servers = Servers(httpClient),
                serverTypes = ServerTypes(httpClient),
                images = Images(httpClient),
                isos = Isos(httpClient),
                datacenters = Datacenters(httpClient),
                placementGroups = PlacementGroups(httpClient),
                networks = Networks(httpClient),
                loadBalancers = LoadBalancers(httpClient),
                loadBalancerTypes = LoadBalancerTypes(httpClient),
                sshKeys = SSHKeys(httpClient),
                volumes = Volumes(httpClient),
                certificates = Certificates(httpClient),
                firewalls = Firewalls(httpClient),
                primaryIps = PrimaryIps(httpClient),
                floatingIps = FloatingIps(httpClient),
                pricing = Pricing(httpClient),
            )
        }
    }
}
