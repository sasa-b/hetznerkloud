package tech.sco.hetznerkloud

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.ActionFailedError
import tech.sco.hetznerkloud.model.Datacenter
import tech.sco.hetznerkloud.model.DnsPtr
import tech.sco.hetznerkloud.model.IpType
import tech.sco.hetznerkloud.model.Location
import tech.sco.hetznerkloud.model.Meta
import tech.sco.hetznerkloud.model.NetworkZone
import tech.sco.hetznerkloud.model.PrimaryIp
import tech.sco.hetznerkloud.model.Protection
import tech.sco.hetznerkloud.model.Server
import tech.sco.hetznerkloud.model.ServerResource
import tech.sco.hetznerkloud.model.ServerType
import tech.sco.hetznerkloud.request.CreatePrimaryIp
import tech.sco.hetznerkloud.request.UpdateResource
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.ItemCreated
import tech.sco.hetznerkloud.response.Items
import java.time.OffsetDateTime

class PrimaryIpApiTest :
    ShouldSpec({
        val primaryIpId = PrimaryIp.Id(42)
        val apiToken = ApiToken("foo")
        val mockEngine = createMockEngine(apiToken) { mapOf("id" to primaryIpId.value.toString()) }
        val underTest = CloudApiClient.of(apiToken, mockEngine)

        val expectedIp = PrimaryIp(
            id = PrimaryIp.Id(42),
            assigneeId = 17,
            assigneeType = PrimaryIp.AssigneeType.SERVER,
            autoDelete = true,
            blocked = false,
            created = OffsetDateTime.parse("2016-01-30T23:55:00+00:00"),
            datacenter = Datacenter(
                id = Datacenter.Id(42),
                description = "Falkenstein DC Park 8",
                location = Location(
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
                serverTypes = Datacenter.ServerTypes(
                    available = listOf(ServerType.Id(1), ServerType.Id(2), ServerType.Id(3)),
                    availableForMigration = listOf(ServerType.Id(1), ServerType.Id(2), ServerType.Id(3)),
                    supported = listOf(ServerType.Id(1), ServerType.Id(2), ServerType.Id(3)),
                ),
            ),
            dnsPtr = listOf(
                DnsPtr(dnsPtr = "server.example.com", ip = "2001:db8::1"),
            ),
            ip = "131.232.99.1",
            labels = mapOf(
                "environment" to "prod",
                "example.com/my" to "label",
                "just-a-key" to "",
            ),
            name = "my-resource",
            protection = Protection(delete = false),
            type = IpType.IPV4,
        )

        context("Primary IP resource read API") {

            should("get all primary ips") {
                underTest.primaryIps.all() shouldBe Items(
                    meta = Meta.of(lastPage = 4, nextPage = 4, page = 3, perPage = 3, previousPage = 2, totalEntries = 100),
                    items = listOf(expectedIp),
                )
            }

            should("get all primary ips") {
                underTest.primaryIps.find(primaryIpId) shouldBe Item(expectedIp)
            }
        }

        context("Primary IP resource write API") {

            should("create a primary ip with assignee") {
                val createRequest = CreatePrimaryIp(
                    assigneeId = 17,
                    assigneeType = PrimaryIp.AssigneeType.SERVER,
                    autoDelete = false,
                    datacenter = null,
                    labels = mapOf(
                        "labelkey" to "value",
                    ),
                    name = "my-resource",
                    type = IpType.IPV4,
                )

                underTest.primaryIps.create(createRequest) shouldBe ItemCreated(
                    action = Action(
                        id = Action.Id(13),
                        command = "create_primary_ip",
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            ServerResource(id = Server.Id(17)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50:00+00:00"),
                        status = Action.Status.RUNNING,
                    ),
                    item = expectedIp.copy(
                        labels = mapOf("labelkey" to "value"),
                        created = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        name = "my-ip",
                        datacenter = expectedIp.datacenter.copy(location = expectedIp.datacenter.location.copy(id = Location.Id(1))),
                    ),
                )
            }

            should("create a primary ip with datacenter") {
                val createRequest = CreatePrimaryIp(
                    assigneeId = null,
                    assigneeType = null,
                    autoDelete = false,
                    datacenter = "fsn1-dc8",
                    labels = mapOf(
                        "labelkey" to "value",
                    ),
                    name = "my-resource",
                    type = IpType.IPV4,
                )

                underTest.primaryIps.create(createRequest) shouldBe ItemCreated(
                    action = Action(
                        id = Action.Id(13),
                        command = "create_primary_ip",
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            ServerResource(id = Server.Id(17)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50:00+00:00"),
                        status = Action.Status.RUNNING,
                    ),
                    item = expectedIp.copy(
                        labels = mapOf("labelkey" to "value"),
                        created = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        name = "my-ip",
                        datacenter = expectedIp.datacenter.copy(location = expectedIp.datacenter.location.copy(id = Location.Id(1))),
                    ),
                )
            }

            should("update a primary ip") {

                val updateRequest = UpdateResource(
                    name = "my-resource",
                    labels = mapOf(
                        "environment" to "prod",
                        "example.com/my" to "label",
                        "just-a-key" to "",
                    ),
                )

                underTest.primaryIps.update(primaryIpId, updateRequest) shouldBe Item(expectedIp)
            }

            should("delete a primary ip") {
                underTest.primaryIps.delete(primaryIpId) shouldBe Unit
            }
        }
    })
