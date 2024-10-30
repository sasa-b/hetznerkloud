package tech.sco.hetznerkloud

import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.Datacenter
import tech.sco.hetznerkloud.model.Image
import tech.sco.hetznerkloud.model.Iso
import tech.sco.hetznerkloud.model.Location
import tech.sco.hetznerkloud.model.Server
import tech.sco.hetznerkloud.model.ServerType
import java.time.OffsetDateTime

object ServerFixture {

    fun create(serverId: Long): Server = Server(
        id = serverId,
        backupWindow = "22-02",
        created = OffsetDateTime.parse("2016-01-30T23:55:00+00:00"),
        datacenter =
        Datacenter(
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
            Datacenter.ServerTypes(
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
        Server.PlacementGroup(
            id = 42,
            created = OffsetDateTime.parse("2016-01-30T23:55:00+00:00"),
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
                announced = OffsetDateTime.parse("2023-06-01T00:00:00+00:00"),
                unavailableAfter = OffsetDateTime.parse("2023-09-01T00:00:00+00:00"),
            ),
            description = "CPX11",
            disk = 40,
            memory = 2,
            name = "cpx11",
            prices =
            listOf(
                ServerType.Price(
                    includedTraffic = 654321,
                    location = "fsn1",
                    priceHourly = ServerType.Price.Amount(gross = "1.1900", net = "1.0000"),
                    priceMonthly = ServerType.Price.Amount(gross = "1.1900", net = "1.0000"),
                    pricePerTbTraffic = ServerType.Price.Amount(gross = "1.1900", net = "1.0000"),
                ),
            ),
            storageType = "local",
        ),
        status = "running",
        volumes = listOf(0),
    )
}

object ActionFixture {
    fun create(
        id: Long = 1,
        error: Action.Error? = null,
        command: String = "create_server",
        finished: OffsetDateTime? = null,
        progress: Int = 0,
        resources: List<Action.Resource> = listOf(Action.Resource(id = 42, type = "server")),
        started: OffsetDateTime = OffsetDateTime.parse("2016-01-30T23:50:00+00:00"),
        status: String = "running",
    ) = Action(
        id = id,
        command = command,
        error ?: Action.Error(
            code = "action_failed",
            message = "Action failed",
        ),
        finished = finished,
        progress = progress,
        resources = resources,
        started = started,
        status = status,
    )
}
