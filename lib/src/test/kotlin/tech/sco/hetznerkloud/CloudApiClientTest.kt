/*
 * This source file was generated by the Gradle 'init' task
 */
package tech.sco.hetznerkloud

import io.kotest.common.runBlocking
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import tech.sco.hetznerkloud.model.read.DataCenter
import tech.sco.hetznerkloud.model.read.Image
import tech.sco.hetznerkloud.model.read.Iso
import tech.sco.hetznerkloud.model.read.Location
import tech.sco.hetznerkloud.model.read.Meta
import tech.sco.hetznerkloud.model.read.Server
import tech.sco.hetznerkloud.model.read.ServerType
import tech.sco.hetznerkloud.response.DataCenterList
import tech.sco.hetznerkloud.response.ServerList

private const val TEST_TOKEN = "foo"

class CloudApiClientTest : AnnotationSpec() {
    private val apiToken = ApiToken(TEST_TOKEN)
    private val mockEngine = createMockEngine(apiToken)

    @Test
    fun canGetServerList() {
        runBlocking {
            val underTest = CloudApiClient.of(apiToken, mockEngine)

            underTest.servers() shouldBe
                ServerList(
                    meta = Meta(pagination = Meta.Pagination(lastPage = 4, nextPage = 4, page = 3, perPage = 25, previousPage = 2, totalEntries = 100)),
                    servers =
                        listOf(
                            Server(
                                id = 42,
                                backupWindow = "22-02",
                                created = "2016-01-30T23:55:00+00:00",
                                datacenter =
                                    DataCenter(
                                        id = 42,
                                        description = "Falkenstein DC Park 8",
                                        location =
                                            Location(
                                                id = 42,
                                                city = "Falkenstein",
                                                country = "DE",
                                                description = "Falkenstein DC Park 1",
                                                latitude = 50.47612,
                                                longitude = 12.370071,
                                                name = "fsn1",
                                                networkZone = "eu-central",
                                            ),
                                        name = "fsn1-dc8",
                                        serverTypes =
                                            DataCenter.ServerTypes(
                                                available = listOf(1, 2, 3),
                                                availableForMigration = listOf(1, 2, 3),
                                                supported = listOf(1, 2, 3),
                                            ),
                                    ),
                                image =
                                    Image(
                                        id = 42,
                                        architecture = "x86",
                                        boundTo = null,
                                        created = "2016-01-30T23:55:00+00:00",
                                        createdFrom = Image.CreatedFrom(id = 1, name = "Server"),
                                        deleted = null,
                                        deprecated = "2018-02-28T00:00:00+00:00",
                                        description = "Ubuntu 20.04 Standard 64 bit",
                                        diskSize = 10,
                                        imageSize = 2.3,
                                        labels = mapOf("environment" to "prod", "example.com/my" to "label", "just-a-key" to ""),
                                        name = "ubuntu-20.04",
                                        osFlavor = "ubuntu",
                                        osVersion = "20.04",
                                        protection = Image.Protection(delete = false),
                                        rapidDeploy = false,
                                        status = "available",
                                        type = "snapshot",
                                    ),
                                includedTraffic = 654321,
                                ingoingTraffic = 123456,
                                iso =
                                    Iso(
                                        id = 42,
                                        architecture = "x86",
                                        deprecation =
                                            Server.Deprecation(
                                                announced = "2023-06-01T00:00:00+00:00",
                                                unavailableAfter = "2023-09-01T00:00:00+00:00",
                                            ),
                                        description = "FreeBSD 11.0 x64",
                                        name = "FreeBSD-11.0-RELEASE-amd64-dvd1",
                                        type = "public",
                                    ),
                                labels = mapOf("environment" to "prod", "example.com/my" to "label", "just-a-key" to ""),
                                loadBalancers = listOf(0),
                                locked = false,
                                name = "my-resource",
                                outgoingTraffic = 123456,
                                placementGroup =
                                    Server.PlacementGroup(
                                        id = 42,
                                        created = "2016-01-30T23:55:00+00:00",
                                        labels = mapOf("environment" to "prod", "example.com/my" to "label", "just-a-key" to ""),
                                        name = "my-resource",
                                        servers = listOf(42),
                                        type = "spread",
                                    ),
                                primaryDiskSize = 50,
                                privateNet =
                                    listOf(
                                        Server.PrivateNetwork(
                                            aliasIps = listOf("10.0.0.3", "10.0.0.4"),
                                            ip = "10.0.0.2",
                                            macAddress = "86:00:ff:2a:7d:e1",
                                            network = 4711,
                                        ),
                                    ),
                                protection = Server.Protection(delete = false, rebuild = false),
                                publicNet =
                                    Server.PublicNetwork(
                                        firewalls = listOf(Server.PublicNetwork.Firewall(id = 42, status = "applied")),
                                        floatingIps = listOf(478),
                                        ipv4 =
                                            Server.PublicNetwork.Ipv4(
                                                id = 42,
                                                blocked = false,
                                                dnsPtr = "server01.example.com",
                                                ip = "1.2.3.4",
                                            ),
                                        ipv6 =
                                            Server.PublicNetwork.Ipv6(
                                                id = 42,
                                                blocked = false,
                                                dnsPtr = listOf(mapOf("dns_ptr" to "server.example.com", "ip" to "2001:db8::1")),
                                                ip = "2001:db8::/64",
                                            ),
                                    ),
                                rescueEnabled = false,
                                serverType =
                                    ServerType(
                                        id = 1,
                                        architecture = "x86",
                                        cores = 2,
                                        cpuType = "shared",
                                        deprecated = false,
                                        deprecation =
                                            Server.Deprecation(
                                                announced = "2023-06-01T00:00:00+00:00",
                                                unavailableAfter = "2023-09-01T00:00:00+00:00",
                                            ),
                                        description = "CPX11",
                                        disk = 40,
                                        includedTraffic = null,
                                        memory = 2,
                                        name = "cpx11",
                                        prices =
                                            listOf(
                                                ServerType.Price(
                                                    includedTraffic = 654321,
                                                    location = "fsn1",
                                                    priceHourly = ServerType.Price.Amount(gross = 1.19, net = 1.0),
                                                    priceMonthly = ServerType.Price.Amount(gross = 1.19, net = 1.0),
                                                    pricePerTbTraffic = ServerType.Price.Amount(gross = 1.19, net = 1.0),
                                                ),
                                            ),
                                        storageType = "local",
                                    ),
                                status = "running",
                                volumes = listOf(0),
                            ),
                        ),
                )
        }
    }

    @Test
    fun canGetDatacentersList() {
        runBlocking {
            val underTest = CloudApiClient.of(apiToken, mockEngine)

            underTest.datacenters() shouldBe
                DataCenterList(
                    meta = Meta(pagination = Meta.Pagination(lastPage = 4, nextPage = 4, page = 3, perPage = 25, previousPage = 2, totalEntries = 100)),
                    datacenters =
                        listOf(
                            DataCenter(
                                id = 42,
                                description = "Falkenstein DC Park 8",
                                location =
                                    Location(
                                        id = 42,
                                        city = "Falkenstein",
                                        country = "DE",
                                        description = "Falkenstein DC Park 1",
                                        latitude = 50.47612,
                                        longitude = 12.370071,
                                        name = "fsn1",
                                        networkZone = "eu-central",
                                    ),
                                name = "fsn1-dc8",
                                serverTypes =
                                    DataCenter.ServerTypes(
                                        available = listOf(1, 2, 3),
                                        availableForMigration = listOf(1, 2, 3),
                                        supported = listOf(1, 2, 3),
                                    ),
                            ),
                        ),
                    recommendation = 1,
                )
        }
    }
}
