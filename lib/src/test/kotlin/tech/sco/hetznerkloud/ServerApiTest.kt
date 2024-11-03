package tech.sco.hetznerkloud

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.Datacenter
import tech.sco.hetznerkloud.model.Image
import tech.sco.hetznerkloud.model.Iso
import tech.sco.hetznerkloud.model.Location
import tech.sco.hetznerkloud.model.Meta
import tech.sco.hetznerkloud.model.PlacementGroup
import tech.sco.hetznerkloud.model.Price
import tech.sco.hetznerkloud.model.Protection
import tech.sco.hetznerkloud.model.Server
import tech.sco.hetznerkloud.model.ServerMetrics
import tech.sco.hetznerkloud.model.ServerType
import tech.sco.hetznerkloud.request.CreateServer
import tech.sco.hetznerkloud.request.FilterFields
import tech.sco.hetznerkloud.request.ServerMetricsFilter
import tech.sco.hetznerkloud.request.UpdateServer
import tech.sco.hetznerkloud.response.ServerCreated
import tech.sco.hetznerkloud.response.ServerDeleted
import tech.sco.hetznerkloud.response.ServerItem
import tech.sco.hetznerkloud.response.ServerList
import tech.sco.hetznerkloud.response.ServerUpdated
import java.time.OffsetDateTime
import tech.sco.hetznerkloud.response.ServerMetrics as ServerMetricsResponse

class ServerApiTest :
    ShouldSpec({
        val serverId = Server.Id(42)
        val apiToken = ApiToken("foo")
        val mockEngine = createMockEngine(apiToken) { serverId }
        val underTest = CloudApiClient.of(apiToken, mockEngine)

        val expectedServer = Server(
            id = serverId,
            backupWindow = "22-02",
            created = OffsetDateTime.parse("2016-01-30T23:55:00+00:00"),
            datacenter =
            Datacenter(
                id = Datacenter.Id(42),
                description = "Falkenstein DC Park 8",
                location =
                Location(
                    id = Location.Id(42),
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
                Datacenter.ServerTypes(
                    available = listOf(1, 2, 3),
                    availableForMigration = listOf(1, 2, 3),
                    supported = listOf(1, 2, 3),
                ),
            ),
            image =
            Image(
                id = Image.Id(42),
                architecture = "x86",
                boundTo = null,
                created = OffsetDateTime.parse("2016-01-30T23:55:00+00:00"),
                createdFrom = Image.CreatedFrom(id = 1, name = "Server"),
                deleted = null,
                deprecated = OffsetDateTime.parse("2018-02-28T00:00:00+00:00"),
                description = "Ubuntu 20.04 Standard 64 bit",
                diskSize = 10,
                imageSize = 2.3,
                labels = mapOf("environment" to "prod", "example.com/my" to "label", "just-a-key" to ""),
                name = "ubuntu-20.04",
                osFlavor = "ubuntu",
                osVersion = "20.04",
                protection = Protection(delete = false),
                rapidDeploy = false,
                status = "available",
                type = "snapshot",
            ),
            includedTraffic = 654321,
            ingoingTraffic = 123456,
            iso =
            Iso(
                id = Iso.Id(42),
                architecture = "x86",
                deprecation =
                Server.Deprecation(
                    announced = OffsetDateTime.parse("2023-06-01T00:00:00+00:00"),
                    unavailableAfter = OffsetDateTime.parse("2023-09-01T00:00:00+00:00"),
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
            PlacementGroup(
                id = PlacementGroup.Id(42),
                created = OffsetDateTime.parse("2016-01-30T23:55:00+00:00"),
                labels = mapOf("environment" to "prod", "example.com/my" to "label", "just-a-key" to ""),
                name = "my-resource",
                servers = listOf(Server.Id(42)),
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
                id = ServerType.Id(1),
                architecture = "x86",
                cores = 2,
                cpuType = "shared",
                deprecated = false,
                deprecation =
                Server.Deprecation(
                    announced = OffsetDateTime.parse("2023-06-01T00:00:00+00:00"),
                    unavailableAfter = OffsetDateTime.parse("2023-09-01T00:00:00+00:00"),
                ),
                description = "CPX11",
                disk = 40,
                memory = 2,
                name = "cpx11",
                prices =
                listOf(
                    Price(
                        includedTraffic = 654321,
                        location = "fsn1",
                        priceHourly = Price.Amount(gross = "1.1900", net = "1.0000"),
                        priceMonthly = Price.Amount(gross = "1.1900", net = "1.0000"),
                        pricePerTbTraffic = Price.Amount(gross = "1.1900", net = "1.0000"),
                    ),
                ),
                storageType = "local",
            ),
            status = "running",
            volumes = listOf(0),
        )

        context("Server resource read API") {
            should("get all Servers") {
                underTest.servers.all() shouldBe ServerList(
                    meta = Meta(pagination = Meta.Pagination(lastPage = 4, nextPage = 4, page = 3, perPage = 25, previousPage = 2, totalEntries = 100)),
                    servers =
                    listOf(expectedServer),
                )
            }

            should("get a Server by id") {
                underTest.servers.find(serverId) shouldBe ServerItem(expectedServer)
            }

            should("get Server metrics") {

                val expectedMetrics = ServerMetrics(
                    end = OffsetDateTime.parse("2017-01-01T23:00:00+00:00"),
                    start = OffsetDateTime.parse("2017-01-01T00:00:00+00:00"),
                    // API reference says this field should be a string but in the example it's a number
                    step = 60,
                    timeSeries = ServerMetrics.TimeSeries(
                        cpu = ServerMetrics.TimeSeries.Element(
                            values = listOf(
                                ServerMetrics.TimeSeries.ValuePair(first = 1.435781470622E9, second = "42"),
                                ServerMetrics.TimeSeries.ValuePair(first = 1.435781471622E9, second = "43"),
                            ),
                        ),
                        diskIoPerSecondsRead = ServerMetrics.TimeSeries.Element(
                            values = listOf(
                                ServerMetrics.TimeSeries.ValuePair(first = 1.435781470622E9, second = "42"),
                                ServerMetrics.TimeSeries.ValuePair(first = 1.435781471622E9, second = "43"),
                            ),
                        ),
                        diskIoPerSecondsWrite = ServerMetrics.TimeSeries.Element(
                            values = listOf(
                                ServerMetrics.TimeSeries.ValuePair(first = 1.435781470622E9, second = "42"),
                                ServerMetrics.TimeSeries.ValuePair(first = 1.435781471622E9, second = "43"),
                            ),
                        ),
                        diskBandwidthRead = ServerMetrics.TimeSeries.Element(
                            values = listOf(
                                ServerMetrics.TimeSeries.ValuePair(first = 1.435781470622E9, second = "42"),
                                ServerMetrics.TimeSeries.ValuePair(first = 1.435781471622E9, second = "43"),
                            ),
                        ),
                        diskBandwidthWrite = ServerMetrics.TimeSeries.Element(
                            values = listOf(
                                ServerMetrics.TimeSeries.ValuePair(first = 1.435781470622E9, second = "42"),
                                ServerMetrics.TimeSeries.ValuePair(first = 1.435781471622E9, second = "43"),
                            ),
                        ),
                        networkPacketsPerSecondIn = ServerMetrics.TimeSeries.Element(
                            values = listOf(
                                ServerMetrics.TimeSeries.ValuePair(first = 1.435781470622E9, second = "42"),
                                ServerMetrics.TimeSeries.ValuePair(first = 1.435781471622E9, second = "43"),
                            ),
                        ),
                        networkPacketsPerSecondOut = ServerMetrics.TimeSeries.Element(
                            values = listOf(
                                ServerMetrics.TimeSeries.ValuePair(first = 1.435781470622E9, second = "42"),
                                ServerMetrics.TimeSeries.ValuePair(first = 1.435781471622E9, second = "43"),
                            ),
                        ),
                        networkBandwidthIn = ServerMetrics.TimeSeries.Element(
                            values = listOf(
                                ServerMetrics.TimeSeries.ValuePair(first = 1.435781470622E9, second = "42"),
                                ServerMetrics.TimeSeries.ValuePair(first = 1.435781471622E9, second = "43"),
                            ),
                        ),
                        networkBandwidthOut = ServerMetrics.TimeSeries.Element(
                            values = listOf(
                                ServerMetrics.TimeSeries.ValuePair(first = 1.435781470622E9, second = "42"),
                                ServerMetrics.TimeSeries.ValuePair(first = 1.435781471622E9, second = "43"),
                            ),
                        ),
                    ),
                )

                val requiredFilters = setOf(
                    ServerMetricsFilter(
                        FilterFields.ServerMetrics.TYPE,
                        ServerMetrics.Type.CPU.value,
                    ),
                    ServerMetricsFilter(
                        FilterFields.ServerMetrics.TYPE,
                        ServerMetrics.Type.DISK.value,
                    ),
                    ServerMetricsFilter(
                        FilterFields.ServerMetrics.TYPE,
                        ServerMetrics.Type.NETWORK.value,
                    ),
                    ServerMetricsFilter(
                        FilterFields.ServerMetrics.START,
                        "2017-01-01T00:00:00+00:00",
                    ),
                    ServerMetricsFilter(
                        FilterFields.ServerMetrics.END,
                        "2017-01-01T23:00:00+00:00",
                    ),
                )

                underTest.servers.metrics(Server.Id(42), requiredFilters) shouldBe ServerMetricsResponse(
                    expectedMetrics,
                )
            }
        }

        context("Server resource write API") {
            should("create a Server") {

                val requestBody = CreateServer(
                    automount = false,
                    datacenter = "nbg1-dc3",
                    firewalls = listOf(CreateServer.Firewall(38)),
                    image = "ubuntu-20.04",
                    labels = mapOf(
                        "environment" to "prod",
                        "example.com/my" to "label",
                        "just-a-key" to "",
                    ),
                    location = "nbg1",
                    name = "my-server",
                    networks = listOf(456),
                    placementGroup = PlacementGroup.Id(1),
                    publicNetwork = CreateServer.PublicNetwork(
                        enableIpv4 = false,
                        enableIpv6 = false,
                        ipv4 = null,
                        ipv6 = null,
                    ),
                    serverType = "cpx11",
                    sshKeys = listOf("my-ssh-key"),
                    startAfterCreate = true,
                    userData = "#cloud-config\nruncmd:\n- [touch, /root/cloud-init-worked]\n",
                    volumes = listOf(123),
                )

                underTest.servers.create(requestBody) shouldBe ServerCreated(
                    action = Action(
                        id = Action.Id(1),
                        command = "create_server",
                        Action.Error(
                            code = "action_failed",
                            message = "Action failed",
                        ),
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            Action.Resource(
                                id = 42,
                                type = "server",
                            ),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50:00+00:00"),
                        status = "running",
                    ),
                    nextActions = listOf(
                        Action(
                            id = Action.Id(13),
                            command = "start_server",
                            error = Action.Error(code = "action_failed", message = "Action failed"),
                            finished = null,
                            progress = 0,
                            resources = listOf(Action.Resource(id = 42, type = "server")),
                            started = OffsetDateTime.parse("2016-01-30T23:50Z"),
                            status = "running",
                        ),
                    ),
                    rootPassword = "YItygq1v3GYjjMomLaKc",
                    server = Server(
                        id = Server.Id(42),
                        backupWindow = "22-02",
                        created = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        datacenter = Datacenter(
                            id = Datacenter.Id(1),
                            description = "Falkenstein 1 DC 8",
                            location = Location(
                                id = Location.Id(1),
                                city = "Falkenstein",
                                country = "DE",
                                description = "Falkenstein DC Park 1",
                                latitude = 50.47612,
                                longitude = 12.370071,
                                name = "fsn1",
                                networkZone = "eu-central",
                            ),
                            name = "fsn1-dc8",
                            serverTypes = Datacenter.ServerTypes(
                                available = listOf(1, 2, 3),
                                availableForMigration = listOf(1, 2, 3),
                                supported = listOf(1, 2, 3),
                            ),
                        ),
                        image = Image(
                            id = Image.Id(4711),
                            architecture = "x86",
                            boundTo = null,
                            created = OffsetDateTime.parse("2016-01-30T23:50Z"),
                            createdFrom = Image.CreatedFrom(id = 1, name = "Server"),
                            deleted = null,
                            deprecated = OffsetDateTime.parse("2018-02-28T00:00Z"),
                            description = "Ubuntu 20.04 Standard 64 bit",
                            diskSize = 10,
                            imageSize = 2.3,
                            labels = mapOf("env" to "dev"),
                            name = "ubuntu-20.04",
                            osFlavor = "ubuntu",
                            osVersion = "20.04",
                            protection = Protection(delete = false),
                            rapidDeploy = false,
                            status = "available",
                            type = "snapshot",
                        ),
                        includedTraffic = 654321,
                        ingoingTraffic = 123456,
                        iso = Iso(
                            id = Iso.Id(4711),
                            architecture = "x86",
                            deprecation = Server.Deprecation(
                                announced = OffsetDateTime.parse("2018-02-28T00:00Z"),
                                unavailableAfter = OffsetDateTime.parse("2018-05-31T00:00Z"),
                            ),
                            description = "FreeBSD 11.0 x64",
                            name = "FreeBSD-11.0-RELEASE-amd64-dvd1",
                            type = "public",
                        ),
                        labels = mapOf("env" to "dev"),
                        loadBalancers = emptyList(),
                        locked = false,
                        name = "my-server",
                        outgoingTraffic = 123456,
                        placementGroup = null,
                        primaryDiskSize = 50,
                        privateNet = listOf(
                            Server.PrivateNetwork(aliasIps = emptyList(), ip = "10.0.0.2", macAddress = "86:00:ff:2a:7d:e1", network = 4711),
                        ),
                        protection = Server.Protection(delete = false, rebuild = false),
                        publicNet = Server.PublicNetwork(
                            firewalls = listOf(Server.PublicNetwork.Firewall(id = 38, status = "applied")),
                            floatingIps = listOf(478),
                            ipv4 = Server.PublicNetwork.Ipv4(
                                id = null,
                                blocked = false,
                                dnsPtr = "server01.example.com",
                                ip = "1.2.3.4",
                            ),
                            ipv6 = Server.PublicNetwork.Ipv6(
                                id = null,
                                blocked = false,
                                dnsPtr = listOf(mapOf("dns_ptr" to "server.example.com", "ip" to "2001:db8::1")),
                                ip = "2001:db8::/64",
                            ),
                        ),
                        rescueEnabled = false,
                        serverType = ServerType(
                            id = ServerType.Id(1),
                            architecture = "x86",
                            cores = 2,
                            cpuType = "shared",
                            deprecated = true,
                            deprecation = null,
                            description = "CPX11",
                            disk = 40,
                            memory = 2,
                            name = "cpx11",
                            prices = listOf(
                                Price(
                                    includedTraffic = 21990232555520,
                                    location = "fsn1",
                                    priceHourly = Price.Amount(gross = "1.1900000000000000", net = "1.0000000000"),
                                    priceMonthly = Price.Amount(gross = "1.1900000000000000", net = "1.0000000000"),
                                    pricePerTbTraffic = Price.Amount(gross = "1.1900000000000000", net = "1.0000000000"),
                                ),
                            ),
                            storageType = "local",
                        ),
                        status = "initializing",
                        volumes = emptyList(),
                    ),
                )
            }

            should("update a Server") {

                val requestBody = UpdateServer(
                    labels = mapOf(
                        "environment" to "prod",
                        "example.com/my" to "label",
                        "just-a-key" to "",
                    ),
                    name = "my-server",
                )

                underTest.servers.update(serverId, requestBody) shouldBe ServerUpdated(expectedServer)
            }

            should("delete a Server") {
                underTest.servers.delete(serverId) shouldBe ServerDeleted(
                    Action(
                        id = Action.Id(42),
                        command = "start_resource",
                        Action.Error(
                            code = "action_failed",
                            message = "Action failed",
                        ),
                        finished = OffsetDateTime.parse("2016-01-30T23:55:00+00:00"),
                        progress = 100,
                        listOf(
                            Action.Resource(
                                id = 42,
                                type = "server",
                            ),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:55:00+00:00"),
                        status = "running",
                    ),
                )
            }
        }
    })
