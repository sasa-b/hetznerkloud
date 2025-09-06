package tech.sco.hetznerkloud

import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.ActionFailedError
import tech.sco.hetznerkloud.model.Datacenter
import tech.sco.hetznerkloud.model.Deprecation
import tech.sco.hetznerkloud.model.Error
import tech.sco.hetznerkloud.model.Image
import tech.sco.hetznerkloud.model.Iso
import tech.sco.hetznerkloud.model.Location
import tech.sco.hetznerkloud.model.NetworkZone
import tech.sco.hetznerkloud.model.PlacementGroup
import tech.sco.hetznerkloud.model.Price
import tech.sco.hetznerkloud.model.Protection
import tech.sco.hetznerkloud.model.Resource
import tech.sco.hetznerkloud.model.Server
import tech.sco.hetznerkloud.model.ServerResource
import tech.sco.hetznerkloud.model.ServerType
import java.time.OffsetDateTime

// TODO: Move test object creation from tests to fixtures
object ServerFixture {
    @Suppress("LongMethod")
    fun create(serverId: Server.Id): Server = Server(
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
}

object ActionFixture {
    @Suppress("LongParameterList")
    fun create(
        id: Long = 1,
        error: Error? = null,
        command: String = "create_server",
        finished: OffsetDateTime? = null,
        progress: Int = 0,
        resources: List<Resource> = listOf(ServerResource(id = Server.Id(42))),
        started: OffsetDateTime = OffsetDateTime.parse("2016-01-30T23:50:00+00:00"),
        status: Action.Status = Action.Status.RUNNING,
    ) = Action(
        id = Action.Id(id),
        command = command,
        error ?: ActionFailedError(
            message = "Action failed",
        ),
        finished = finished,
        progress = progress,
        resources = resources,
        started = started,
        status = status,
    )
}
