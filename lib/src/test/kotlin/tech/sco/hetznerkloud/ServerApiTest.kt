package tech.sco.hetznerkloud

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.ActionFailedError
import tech.sco.hetznerkloud.model.Datacenter
import tech.sco.hetznerkloud.model.Image
import tech.sco.hetznerkloud.model.Iso
import tech.sco.hetznerkloud.model.Location
import tech.sco.hetznerkloud.model.Meta
import tech.sco.hetznerkloud.model.Network
import tech.sco.hetznerkloud.model.NetworkZone
import tech.sco.hetznerkloud.model.PlacementGroup
import tech.sco.hetznerkloud.model.Price
import tech.sco.hetznerkloud.model.Protection
import tech.sco.hetznerkloud.model.Resource
import tech.sco.hetznerkloud.model.Server
import tech.sco.hetznerkloud.model.ServerMetrics
import tech.sco.hetznerkloud.model.ServerType
import tech.sco.hetznerkloud.request.AddToPlacementGroup
import tech.sco.hetznerkloud.request.AttachIsoById
import tech.sco.hetznerkloud.request.AttachIsoByName
import tech.sco.hetznerkloud.request.AttachToNetwork
import tech.sco.hetznerkloud.request.CreateServer
import tech.sco.hetznerkloud.request.FilterFields
import tech.sco.hetznerkloud.request.ServerMetricsFilter
import tech.sco.hetznerkloud.request.UpdateResource
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.Items
import tech.sco.hetznerkloud.response.ServerCreated
import tech.sco.hetznerkloud.response.ServerDeleted
import java.time.OffsetDateTime

class ServerApiTest :
    ShouldSpec({
        val serverId = Server.Id(42)
        val apiToken = ApiToken("foo")
        val mockEngine = createMockEngine(apiToken) { serverId.value }
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
                    networkZone = NetworkZone.EU_CENTRAL,
                ),
                name = "fsn1-dc8",
                serverTypes =
                Datacenter.ServerTypes(
                    available = listOf(ServerType.Id(1), ServerType.Id(2), ServerType.Id(3)),
                    availableForMigration = listOf(ServerType.Id(1), ServerType.Id(2), ServerType.Id(3)),
                    supported = listOf(ServerType.Id(1), ServerType.Id(2), ServerType.Id(3)),
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
                status = Image.Status.AVAILABLE,
                type = Image.Type.SNAPSHOT,
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
                type = Iso.Type.PUBLIC,
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
                type = PlacementGroup.Type.SPREAD,
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
                disk = 40.0,
                memory = 2.0,
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
                storageType = ServerType.StorageType.LOCAL,
            ),
            status = Server.Status.RUNNING,
            volumes = listOf(0),
        )

        context("Server resource read API") {
            should("get all Servers") {
                underTest.servers.all() shouldBe Items(
                    meta = Meta(pagination = Meta.Pagination(lastPage = 4, nextPage = 4, page = 3, perPage = 25, previousPage = 2, totalEntries = 100)),
                    items = listOf(expectedServer),
                )
            }

            should("get a Server by id") {
                underTest.servers.find(serverId) shouldBe Item(expectedServer)
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

                underTest.servers.metrics(Server.Id(42), requiredFilters) shouldBe Item(
                    expectedMetrics,
                )
            }

            should("get all Server actions") {
                underTest.servers.actions() shouldBe Items(
                    meta = Meta(pagination = Meta.Pagination(lastPage = 4, nextPage = 4, page = 3, perPage = 25, previousPage = 2, totalEntries = 100)),
                    items = listOf(
                        Action(
                            id = Action.Id(42),
                            command = "start_resource",
                            error = ActionFailedError(message = "Action failed"),
                            finished = OffsetDateTime.parse("2016-01-30T23:55Z"),
                            progress = 100,
                            resources = listOf(Resource(id = 42, type = "server")),
                            started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                            status = Action.Status.RUNNING,
                        ),
                    ),
                )
            }

            should("get Server actions") {
                underTest.servers.actions(serverId = serverId) shouldBe Items(
                    meta = Meta(pagination = Meta.Pagination(lastPage = 4, nextPage = 4, page = 3, perPage = 25, previousPage = 2, totalEntries = 100)),
                    items = listOf(
                        Action(
                            id = Action.Id(13),
                            command = "start_server",
                            error = ActionFailedError(message = "Action failed"),
                            finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                            progress = 100,
                            resources = listOf(Resource(id = 42, type = "server")),
                            started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                            status = Action.Status.SUCCESS,
                        ),
                    ),
                )
            }

            should("get a Server action") {
                underTest.servers.action(actionId = Action.Id(42)) shouldBe Item(
                    Action(
                        id = Action.Id(42),
                        command = "start_resource",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        progress = 100,
                        resources = listOf(Resource(id = 42, type = "server")),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }

            should("get a Server action for server") {
                underTest.servers.action(serverId = serverId, actionId = Action.Id(42)) shouldBe Item(
                    Action(
                        id = Action.Id(42),
                        command = "start_resource",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        progress = 100,
                        resources = listOf(Resource(id = 42, type = "server")),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.RUNNING,
                    ),
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
                        ActionFailedError(
                            message = "Action failed",
                        ),
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            Resource(
                                id = 42,
                                type = "server",
                            ),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50:00+00:00"),
                        status = Action.Status.RUNNING,
                    ),
                    nextActions = listOf(
                        Action(
                            id = Action.Id(13),
                            command = "start_server",
                            error = ActionFailedError(message = "Action failed"),
                            finished = null,
                            progress = 0,
                            resources = listOf(Resource(id = 42, type = "server")),
                            started = OffsetDateTime.parse("2016-01-30T23:50Z"),
                            status = Action.Status.RUNNING,
                        ),
                    ),
                    rootPassword = "YItygq1v3GYjjMomLaKc",
                    item = Server(
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
                                networkZone = NetworkZone.EU_CENTRAL,
                            ),
                            name = "fsn1-dc8",
                            serverTypes = Datacenter.ServerTypes(
                                available = listOf(ServerType.Id(1), ServerType.Id(2), ServerType.Id(3)),
                                availableForMigration = listOf(ServerType.Id(1), ServerType.Id(2), ServerType.Id(3)),
                                supported = listOf(ServerType.Id(1), ServerType.Id(2), ServerType.Id(3)),
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
                            status = Image.Status.AVAILABLE,
                            type = Image.Type.SNAPSHOT,
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
                            type = Iso.Type.PUBLIC,
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
                            disk = 40.0,
                            memory = 2.0,
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
                            storageType = ServerType.StorageType.LOCAL,
                        ),
                        status = Server.Status.INITIALIZING,
                        volumes = emptyList(),
                    ),
                )
            }

            should("update a Server") {

                val requestBody = UpdateResource(
                    labels = mapOf(
                        "environment" to "prod",
                        "example.com/my" to "label",
                        "just-a-key" to "",
                    ),
                    name = "my-server",
                )

                underTest.servers.update(serverId, requestBody) shouldBe Item(expectedServer)
            }

            should("delete a Server") {
                underTest.servers.delete(serverId) shouldBe ServerDeleted(
                    Action(
                        id = Action.Id(42),
                        command = "start_resource",
                        ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:55:00+00:00"),
                        progress = 100,
                        listOf(
                            Resource(id = 42, type = "server"),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:55:00+00:00"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }

            should("add Server to a Placement Group") {
                val addToPlacementGroupRequest = AddToPlacementGroup(PlacementGroup.Id(1))

                underTest.servers.addToPlacementGroup(serverId, addToPlacementGroupRequest) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "add_to_placement_group",
                        ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        listOf(
                            Resource(id = 42, type = "server"),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }

            should("attach Iso by name to a Server") {
                val attachIso = AttachIsoByName("FreeBSD-11.0-RELEASE-amd64-dvd1")

                underTest.servers.attachIso(serverId, attachIso) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "attach_iso",
                        ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        listOf(
                            Resource(id = 42, type = "server"),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }

            should("attach Iso by id to a Server") {
                val attachIso = AttachIsoById(Iso.Id(1))

                underTest.servers.attachIso(serverId, attachIso) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "attach_iso",
                        ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        listOf(
                            Resource(id = 42, type = "server"),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }

            should("attach Server to a Network") {
                val attachToNetwork = AttachToNetwork(
                    aliasIps = listOf("10.0.1.2"),
                    ip = "10.0.1.1",
                    network = Network.Id(4711),
                )

                underTest.servers.attachToNetwork(serverId, attachToNetwork) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "attach_to_network",
                        ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        listOf(
                            Resource(id = 42, type = "server"),
                            Resource(id = 4711, type = "network"),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }

            should("shutdown a Server") {
                underTest.servers.shutdown(serverId) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "shutdown_server",
                        ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        listOf(
                            Resource(id = 42, type = "server"),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }

            should("restart a Server") {
                underTest.servers.reset(serverId) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "reset_server",
                        ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        listOf(
                            Resource(id = 42, type = "server"),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }

            should("power on a Server") {
                underTest.servers.powerOn(serverId) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "start_server",
                        ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        listOf(
                            Resource(id = 42, type = "server"),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }

            should("power off a Server") {
                underTest.servers.powerOff(serverId) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "stop_server",
                        ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        listOf(
                            Resource(id = 42, type = "server"),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }

            should("soft reboot on a Server") {
                underTest.servers.softReboot(serverId) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "reboot_server",
                        ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        listOf(
                            Resource(id = 42, type = "server"),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }
        }
    })
