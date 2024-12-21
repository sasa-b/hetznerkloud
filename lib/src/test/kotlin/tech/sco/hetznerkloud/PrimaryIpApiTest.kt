package tech.sco.hetznerkloud

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.encodeToString
import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.ActionFailedError
import tech.sco.hetznerkloud.model.Datacenter
import tech.sco.hetznerkloud.model.DnsPtr
import tech.sco.hetznerkloud.model.IpType
import tech.sco.hetznerkloud.model.Location
import tech.sco.hetznerkloud.model.Meta
import tech.sco.hetznerkloud.model.NetworkZone
import tech.sco.hetznerkloud.model.PrimaryIp
import tech.sco.hetznerkloud.model.PrimaryIpResource
import tech.sco.hetznerkloud.model.Protection
import tech.sco.hetznerkloud.model.ResourceType
import tech.sco.hetznerkloud.model.Server
import tech.sco.hetznerkloud.model.ServerResource
import tech.sco.hetznerkloud.model.ServerType
import tech.sco.hetznerkloud.request.AssignPrimaryIp
import tech.sco.hetznerkloud.request.ChangeDeleteProtection
import tech.sco.hetznerkloud.request.ChangeReverseDns
import tech.sco.hetznerkloud.request.CreatePrimaryIp
import tech.sco.hetznerkloud.request.UpdatePrimaryIp
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

            should("get a primary ip action") {
                underTest.actions.find(ResourceType.PRIMARY_IP, Action.Id(42)) shouldBe Item(
                    value = Action(
                        id = Action.Id(value = 42),
                        command = "start_resource",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        progress = 100,
                        resources = listOf(ServerResource(id = Server.Id(value = 42))),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }

            should("get all primary ip actions") {
                underTest.actions.all(ResourceType.PRIMARY_IP) shouldBe Items(
                    meta = Meta(pagination = Meta.Pagination(lastPage = 4, nextPage = 4, page = 3, perPage = 25, previousPage = 2, totalEntries = 100)),
                    items = listOf(
                        Action(
                            id = Action.Id(42),
                            command = "start_resource",
                            error = ActionFailedError(message = "Action failed"),
                            finished = OffsetDateTime.parse("2016-01-30T23:55Z"),
                            progress = 100,
                            resources = listOf(
                                ServerResource(id = Server.Id(42)),
                            ),
                            started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                            status = Action.Status.RUNNING,
                        ),
                    ),
                )
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
                        "environment" to "prod",
                        "example.com/my" to "label",
                        "just-a-key" to "",
                    ),
                    name = "my-resource",
                    type = IpType.IPV4,
                )

                jsonEncoder().encodeToString(createRequest) shouldBeEqualToRequest "create_a_primary_ip.json"

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
                    assigneeType = PrimaryIp.AssigneeType.SERVER,
                    autoDelete = false,
                    datacenter = "fsn1-dc8",
                    labels = mapOf(
                        "environment" to "prod",
                        "example.com/my" to "label",
                        "just-a-key" to "",
                    ),
                    name = "my-resource",
                    type = IpType.IPV4,
                )

                jsonEncoder().encodeToString(createRequest) shouldBeEqualToRequest "create_a_primary_ip_with_datacenter.json"

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

                val updateRequest = UpdatePrimaryIp(
                    name = "my-resource",
                    labels = mapOf(
                        "environment" to "prod",
                        "example.com/my" to "label",
                        "just-a-key" to "",
                    ),
                    autoDelete = true,
                )

                jsonEncoder().encodeToString(updateRequest) shouldBeEqualToRequest "update_a_primary_ip.json"

                underTest.primaryIps.update(primaryIpId, updateRequest) shouldBe Item(expectedIp)
            }

            should("delete a primary ip") {
                underTest.primaryIps.delete(primaryIpId) shouldBe Unit
            }

            should("assign a primary ip to a resource") {
                val assignToResourceRequest = AssignPrimaryIp(4711)

                jsonEncoder().encodeToString(assignToResourceRequest) shouldBeEqualToRequest "assign_a_primary_ip_to_a_resource.json"

                underTest.primaryIps.assign(primaryIpId, assignToResourceRequest) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "assign_primary_ip",
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            ServerResource(id = Server.Id(42)),
                            PrimaryIpResource(id = PrimaryIp.Id(4711)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50:00+00:00"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }

            should("unassign a primary ip from a resource") {
                underTest.primaryIps.unassign(primaryIpId) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "unassign_primary_ip",
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            ServerResource(id = Server.Id(42)),
                            PrimaryIpResource(id = PrimaryIp.Id(4711)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50:00+00:00"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }

            should("change primary ip protection") {
                val changeProtectionRequest = ChangeDeleteProtection(true)

                jsonEncoder().encodeToString(changeProtectionRequest) shouldBeEqualToRequest "change_primary_ip_protection.json"

                underTest.primaryIps.changeProtection(primaryIpId, changeProtectionRequest) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "change_protection",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                        progress = 100,
                        resources = listOf(
                            PrimaryIpResource(id = PrimaryIp.Id(4711)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.SUCCESS,
                    ),
                )
            }

            should("change primary ip reverse dns") {
                val changeReverseDnsRequest = ChangeReverseDns(
                    "server.example.com",
                    "2001:db8::1",
                )

                jsonEncoder().encodeToString(changeReverseDnsRequest) shouldBeEqualToRequest "change_primary_ip_reverse_dns.json"

                underTest.primaryIps.changeReverseDns(primaryIpId, changeReverseDnsRequest) shouldBe Item(
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
        }
    })
