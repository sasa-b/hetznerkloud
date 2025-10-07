package tech.sco.hetznerkloud

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.ActionFailedError
import tech.sco.hetznerkloud.model.Datacenter
import tech.sco.hetznerkloud.model.Deprecation
import tech.sco.hetznerkloud.model.Image
import tech.sco.hetznerkloud.model.Iso
import tech.sco.hetznerkloud.model.Location
import tech.sco.hetznerkloud.model.Meta
import tech.sco.hetznerkloud.model.Network
import tech.sco.hetznerkloud.model.NetworkResource
import tech.sco.hetznerkloud.model.NetworkZone
import tech.sco.hetznerkloud.model.PlacementGroup
import tech.sco.hetznerkloud.model.Price
import tech.sco.hetznerkloud.model.Protection
import tech.sco.hetznerkloud.model.ResourceType
import tech.sco.hetznerkloud.model.SSHKey
import tech.sco.hetznerkloud.model.Server
import tech.sco.hetznerkloud.model.ServerMetrics
import tech.sco.hetznerkloud.model.ServerResource
import tech.sco.hetznerkloud.model.ServerType
import tech.sco.hetznerkloud.model.Volume
import tech.sco.hetznerkloud.request.AddToPlacementGroup
import tech.sco.hetznerkloud.request.AttachIsoById
import tech.sco.hetznerkloud.request.AttachIsoByName
import tech.sco.hetznerkloud.request.AttachToNetwork
import tech.sco.hetznerkloud.request.ChangeAliasIps
import tech.sco.hetznerkloud.request.ChangeReverseDns
import tech.sco.hetznerkloud.request.ChangeServerProtections
import tech.sco.hetznerkloud.request.ChangeServerType
import tech.sco.hetznerkloud.request.CreateImageFromServer
import tech.sco.hetznerkloud.request.CreateServer
import tech.sco.hetznerkloud.request.DetachFromNetwork
import tech.sco.hetznerkloud.request.EnableRescueMode
import tech.sco.hetznerkloud.request.FilterFields
import tech.sco.hetznerkloud.request.RebuildFromImageById
import tech.sco.hetznerkloud.request.RebuildFromImageByName
import tech.sco.hetznerkloud.request.ServerMetricsFilter
import tech.sco.hetznerkloud.request.UpdateResource
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.ItemDeleted
import tech.sco.hetznerkloud.response.Items
import tech.sco.hetznerkloud.response.ServerActionWithImage
import tech.sco.hetznerkloud.response.ServerActionWithRootPassword
import tech.sco.hetznerkloud.response.ServerConsoleRequestedAction
import tech.sco.hetznerkloud.response.ServerCreated
import java.time.OffsetDateTime

@Suppress("LargeClass")
class ServerApiTest :
    ShouldSpec({
        val serverId = Server.Id(42)
        val apiToken = ApiToken("foo")
        val mockEngine = createMockEngine(apiToken) { mapOf("id" to serverId.value.toString(), "action_id" to "42") }
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
                deprecation = Deprecation(
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
                deprecation = Deprecation(
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
                underTest.actions.all(ResourceType.SERVER) shouldBe Items(
                    meta = Meta(pagination = Meta.Pagination(lastPage = 4, nextPage = 4, page = 3, perPage = 25, previousPage = 2, totalEntries = 100)),
                    items = listOf(
                        Action(
                            id = Action.Id(42),
                            command = "start_resource",
                            error = ActionFailedError(message = "Action failed"),
                            finished = OffsetDateTime.parse("2016-01-30T23:55Z"),
                            progress = 100,
                            resources = listOf(ServerResource(id = Server.Id(42))),
                            started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                            status = Action.Status.RUNNING,
                        ),
                    ),
                )
            }

            should("get Server actions") {
                underTest.actions.all(resourceId = serverId) shouldBe Items(
                    meta = Meta(pagination = Meta.Pagination(lastPage = 4, nextPage = 4, page = 3, perPage = 25, previousPage = 2, totalEntries = 100)),
                    items = listOf(
                        Action(
                            id = Action.Id(13),
                            command = "start_server",
                            error = ActionFailedError(message = "Action failed"),
                            finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                            progress = 100,
                            resources = listOf(ServerResource(id = Server.Id(42))),
                            started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                            status = Action.Status.SUCCESS,
                        ),
                    ),
                )
            }

            should("get a Server action") {
                underTest.actions.find(ResourceType.SERVER, actionId = Action.Id(42)) shouldBe Item(
                    Action(
                        id = Action.Id(42),
                        command = "start_resource",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        progress = 100,
                        resources = listOf(ServerResource(id = Server.Id(42))),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }

            should("get a Server action for server") {
                underTest.actions.find(resourceId = serverId, actionId = Action.Id(42)) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "start_server",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                        progress = 100,
                        resources = listOf(ServerResource(id = Server.Id(42))),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.SUCCESS,
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
                    networks = listOf(Network.Id(456)),
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
                    volumes = listOf(Volume.Id(123)),
                )

                jsonEncoder().encodeToString(requestBody) shouldBeEqualToRequest "create_a_server.json"

                underTest.servers.create(requestBody) shouldBe ServerCreated(
                    action = Action(
                        id = Action.Id(1),
                        command = "create_server",
                        error = ActionFailedError(
                            message = "Action failed",
                        ),
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            ServerResource(
                                id = Server.Id(42),
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
                            resources = listOf(ServerResource(id = Server.Id(42))),
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
                            deprecation = Deprecation(
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

                jsonEncoder().encodeToString(requestBody) shouldBeEqualToRequest "update_a_server.json"

                underTest.servers.update(serverId, requestBody) shouldBe Item(expectedServer)
            }

            should("delete a Server") {
                underTest.servers.delete(serverId) shouldBe ItemDeleted(
                    Action(
                        id = Action.Id(42),
                        command = "start_resource",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:55:00+00:00"),
                        progress = 100,
                        resources = listOf(
                            ServerResource(id = Server.Id(42)),
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
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            ServerResource(id = Server.Id(42)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }

            should("remove Server from a Placement Group") {
                underTest.servers.removeFromPlacementGroup(serverId) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "remove_from_placement_group",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                        progress = 100,
                        resources = listOf(
                            ServerResource(id = Server.Id(42)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.SUCCESS,
                    ),
                )
            }

            should("attach Iso by name to a Server") {
                val attachIso = AttachIsoByName("FreeBSD-11.0-RELEASE-amd64-dvd1")

                underTest.servers.attachIso(serverId, attachIso) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "attach_iso",
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            ServerResource(id = Server.Id(42)),
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
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            ServerResource(id = Server.Id(42)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }

            should("detach Iso from Server") {
                underTest.servers.detachIso(serverId) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "detach_iso",
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            ServerResource(id = Server.Id(42)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }

            should("attach Server to a Network") {
                val attachToNetworkRequest = AttachToNetwork.Server(
                    aliasIps = listOf("10.0.1.2"),
                    ip = "10.0.1.1",
                    network = Network.Id(4711),
                )

                jsonEncoder().encodeToString(attachToNetworkRequest) shouldBeEqualToRequest "attach_a_server_to_network.json"

                underTest.servers.attachToNetwork(serverId, attachToNetworkRequest) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "attach_to_network",
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            ServerResource(id = Server.Id(42)),
                            NetworkResource(id = Network.Id(4711)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }

            should("detach Server from Network") {
                val detachFromNetwork = DetachFromNetwork(
                    network = Network.Id(4711),
                )

                underTest.servers.detachFromNetwork(serverId, detachFromNetwork) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "detach_from_network",
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            ServerResource(id = Server.Id(42)),
                            NetworkResource(id = Network.Id(4711)),
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
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            ServerResource(id = Server.Id(42)),
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
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            ServerResource(id = Server.Id(42)),
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
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            ServerResource(id = Server.Id(42)),
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
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            ServerResource(id = Server.Id(42)),
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
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            ServerResource(id = Server.Id(42)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }

            should("reset Server root password") {
                underTest.servers.resetRootPassword(serverId) shouldBe ServerActionWithRootPassword(
                    action = Action(
                        id = Action.Id(13),
                        command = "reset_password",
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            ServerResource(id = Server.Id(42)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        status = Action.Status.RUNNING,
                    ),
                    rootPassword = "zCWbFhnu950dUTko5f40",
                )
            }

            should("request Server console") {
                underTest.servers.requestConsole(serverId) shouldBe ServerConsoleRequestedAction(
                    action = Action(
                        id = Action.Id(13),
                        command = "request_console",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                        progress = 100,
                        resources = listOf(
                            ServerResource(id = Server.Id(42)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.SUCCESS,
                    ),
                    password = "9MQaTg2VAGI0FIpc10k3UpRXcHj2wQ6x",
                    wssUrl = "wss://console.hetzner.cloud/?server_id=1&token=3db32d15-af2f-459c-8bf8-dee1fd05f49c",
                )
            }

            should("rebuild Server from an image by name") {
                underTest.servers.rebuildFromImage(serverId, RebuildFromImageByName("ubuntu-20.04")) shouldBe ServerActionWithRootPassword(
                    action = Action(
                        id = Action.Id(13),
                        command = "rebuild_server",
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            ServerResource(id = Server.Id(42)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        status = Action.Status.RUNNING,
                    ),
                    rootPassword = null,
                )
            }

            should("rebuild Server from an image by id") {
                underTest.servers.rebuildFromImage(serverId, RebuildFromImageById(Image.Id(1))) shouldBe ServerActionWithRootPassword(
                    action = Action(
                        id = Action.Id(13),
                        command = "rebuild_server",
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            ServerResource(id = Server.Id(42)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        status = Action.Status.RUNNING,
                    ),
                    rootPassword = null,
                )
            }

            should("enable Server backup") {
                underTest.servers.enableBackup(serverId) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "enable_backup",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                        progress = 100,
                        resources = listOf(
                            ServerResource(id = Server.Id(42)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.SUCCESS,
                    ),
                )
            }

            should("disable Server backup") {
                underTest.servers.disableBackup(serverId) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "disable_backup",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                        progress = 100,
                        resources = listOf(
                            ServerResource(id = Server.Id(42)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.SUCCESS,
                    ),
                )
            }

            should("enable rescue mode for a Server") {

                val enableRescueModeRequest = EnableRescueMode(
                    sshKeys = listOf(SSHKey.Id(2323)),
                )

                jsonEncoder().encodeToString(enableRescueModeRequest) shouldBeEqualToRequest "enable_rescue_mode_for_server.json"

                underTest.servers.enableRescueMode(
                    serverId,
                    enableRescueModeRequest,
                ) shouldBe ServerActionWithRootPassword(
                    Action(
                        id = Action.Id(13),
                        command = "enable_rescue",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                        progress = 100,
                        resources = listOf(
                            ServerResource(id = Server.Id(42)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.SUCCESS,
                    ),
                    rootPassword = "zCWbFhnu950dUTko5f40",
                )
            }

            should("disable rescue mode for a Server") {
                underTest.servers.disableRescueMode(serverId) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "disable_rescue",
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            ServerResource(id = Server.Id(42)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }

            should("create an Image from Server") {
                val createImageRequest = CreateImageFromServer(
                    description = "my image",
                    labels = mapOf(
                        "environment" to "prod",
                        "example.com/my" to "label",
                        "just-a-key" to "",
                    ),
                    type = CreateImageFromServer.Type.SNAPSHOT,
                )

                jsonEncoder().encodeToString(createImageRequest) shouldBeEqualToRequest "create_an_image_from_server.json"

                underTest.servers.createImage(serverId, createImageRequest) shouldBe ServerActionWithImage(
                    action = Action(
                        id = Action.Id(13),
                        command = "create_image",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:56:00+00:00"),
                        progress = 100,
                        resources = listOf(
                            ServerResource(id = Server.Id(42)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:55:00+00:00"),
                        status = Action.Status.SUCCESS,
                    ),
                    image = Image(
                        id = Image.Id(4711),
                        architecture = "x86",
                        boundTo = null,
                        created = OffsetDateTime.parse("2016-01-30T23:50:00+00:00"),
                        createdFrom = Image.CreatedFrom(
                            id = 1,
                            name = "Server",
                        ),
                        deleted = null,
                        deprecated = OffsetDateTime.parse("2018-02-28T00:00:00+00:00"),
                        description = "my image",
                        diskSize = 10,
                        imageSize = 2.3,
                        labels = mapOf("env" to "dev"),
                        name = null,
                        osFlavor = "ubuntu",
                        osVersion = "20.04",
                        protection = Protection(delete = false),
                        rapidDeploy = false,
                        status = Image.Status.CREATING,
                        type = Image.Type.SNAPSHOT,
                    ),
                )
            }

            should("change Server type") {
                val changeServerTypeRequest = ChangeServerType(
                    serverType = "cpx11",
                    upgradeDisk = true,
                )

                jsonEncoder().encodeToString(changeServerTypeRequest) shouldBeEqualToRequest "change_server_type.json"

                underTest.servers.changeType(serverId, changeServerTypeRequest) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "change_server_type",
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            ServerResource(id = Server.Id(42)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }

            should("change Server protection") {
                val changeServerProtection = ChangeServerProtections(
                    delete = true,
                    rebuild = true,
                )

                jsonEncoder().encodeToString(changeServerProtection) shouldBeEqualToRequest "change_server_protection.json"

                underTest.servers.changeProtection(serverId, changeServerProtection) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "change_protection",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                        progress = 100,
                        resources = listOf(
                            ServerResource(id = Server.Id(42)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.SUCCESS,
                    ),
                )
            }

            should("change Server reverse DNS ptr") {
                val changeReverseDnsPtrRequest = ChangeReverseDns(
                    dnsPtr = "server01.example.com",
                    ip = "1.2.3.4",
                )

                jsonEncoder().encodeToString(changeReverseDnsPtrRequest) shouldBeEqualToRequest "change_server_reverse_dns.json"

                underTest.servers.changeReverseDns(serverId, changeReverseDnsPtrRequest) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "change_dns_ptr",
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            ServerResource(id = Server.Id(42)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }

            should("change Server alias ips for network") {
                val changeReverseDnsPtrRequest = ChangeAliasIps(
                    aliasIps = listOf("10.0.1.2"),
                    network = Network.Id(4711),
                )

                jsonEncoder().encodeToString(changeReverseDnsPtrRequest) shouldBeEqualToRequest "change_alias_ips_of_a_network.json"

                underTest.servers.changeAliasIps(serverId, changeReverseDnsPtrRequest) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "change_alias_ips",
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            ServerResource(id = Server.Id(42)),
                            NetworkResource(id = Network.Id(4711)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }
        }
    })
