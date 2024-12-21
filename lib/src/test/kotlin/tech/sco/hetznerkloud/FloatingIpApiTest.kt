package tech.sco.hetznerkloud

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.ktor.http.HttpMethod
import kotlinx.serialization.encodeToString
import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.ActionFailedError
import tech.sco.hetznerkloud.model.DnsPtr
import tech.sco.hetznerkloud.model.FloatingIp
import tech.sco.hetznerkloud.model.FloatingIpResource
import tech.sco.hetznerkloud.model.IpType
import tech.sco.hetznerkloud.model.Location
import tech.sco.hetznerkloud.model.Meta
import tech.sco.hetznerkloud.model.NetworkZone
import tech.sco.hetznerkloud.model.Protection
import tech.sco.hetznerkloud.model.ResourceType
import tech.sco.hetznerkloud.model.Server
import tech.sco.hetznerkloud.model.ServerResource
import tech.sco.hetznerkloud.request.AssignFloatingIp
import tech.sco.hetznerkloud.request.ChangeDeleteProtection
import tech.sco.hetznerkloud.request.ChangeReverseDns
import tech.sco.hetznerkloud.request.CreateFloatingIp
import tech.sco.hetznerkloud.request.UpdateFloatingIp
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.ItemCreated
import tech.sco.hetznerkloud.response.Items
import java.time.OffsetDateTime

class FloatingIpApiTest :
    ShouldSpec({
        val floatingIpId = FloatingIp.Id(42)
        val apiToken = ApiToken("foo")
        val mockEngine = createMockEngine(apiToken) { request ->
            when {
                request.method == HttpMethod.Patch -> mapOf("id" to floatingIpId.value.toString())
                else -> mapOf("id" to floatingIpId.value.toString(), "action_id" to "42")
            }
        }
        val underTest = CloudApiClient.of(apiToken, mockEngine)

        val expectedIp = FloatingIp(
            id = floatingIpId,
            blocked = false,
            created = OffsetDateTime.parse("2016-01-30T23:55:00+00:00"),
            description = "This describes my resource",
            dnsPtr = listOf(
                DnsPtr(dnsPtr = "server.example.com", ip = "2001:db8::1"),
            ),
            homeLocation = Location(
                id = Location.Id(42),
                city = "Falkenstein",
                country = "DE",
                description = "Falkenstein DC Park 1",
                latitude = 50.47612,
                longitude = 12.370071,
                name = "fsn1",
                networkZone = NetworkZone.EU_CENTRAL,
            ),
            ip = "131.232.99.1",
            labels = mapOf(
                "environment" to "prod",
                "example.com/my" to "label",
                "just-a-key" to "",
            ),
            name = "my-resource",
            protection = Protection(delete = false),
            server = Server.Id(42),
            type = IpType.IPV4,
        )

        context("Floating IP resource read API") {

            should("get all floating ips") {

                underTest.floatingIps.all() shouldBe Items(
                    meta = Meta.of(lastPage = 4, nextPage = 4, page = 3, perPage = 3, previousPage = 2, totalEntries = 100),
                    items = listOf(expectedIp),
                )
            }

            should("get a floating ip") {

                underTest.floatingIps.find(floatingIpId) shouldBe Item(expectedIp)
            }

            should("get all Floating Ip actions") {
                underTest.actions.all(ResourceType.FLOATING_IP) shouldBe Items(
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

            should("get Floating Ip actions") {
                underTest.actions.all(resourceId = floatingIpId) shouldBe Items(
                    meta = Meta(pagination = Meta.Pagination(lastPage = 4, nextPage = 4, page = 3, perPage = 25, previousPage = 2, totalEntries = 100)),
                    items = listOf(
                        Action(
                            id = Action.Id(13),
                            command = "assign_floating_ip",
                            error = ActionFailedError(message = "Action failed"),
                            finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                            progress = 100,
                            resources = listOf(
                                ServerResource(id = Server.Id(4711)),
                                FloatingIpResource(id = FloatingIp.Id(value = 4712)),
                            ),
                            started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                            status = Action.Status.SUCCESS,
                        ),
                    ),
                )
            }

            should("get a Floating Ip action") {
                underTest.actions.find(ResourceType.FLOATING_IP, actionId = Action.Id(42)) shouldBe Item(
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

            should("get a Floating Ip action for floating ip") {
                underTest.actions.find(resourceId = floatingIpId, actionId = Action.Id(42)) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "assign_floating_ip",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                        progress = 100,
                        resources = listOf(
                            ServerResource(id = Server.Id(value = 42)),
                            FloatingIpResource(id = FloatingIp.Id(value = 4711)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.SUCCESS,
                    ),
                )
            }
        }

        context("Floating IP resource write API") {

            should("create a floating ip") {
                val createRequest = CreateFloatingIp(
                    homeLocation = "fsn1",
                    labels = mapOf(
                        "environment" to "prod",
                        "example.com/my" to "label",
                        "just-a-key" to "",
                    ),
                    name = "my-resource",
                    type = IpType.IPV4,
                    server = Server.Id(42),
                    description = "This describes my resource",
                )

                jsonEncoder().encodeToString(createRequest) shouldBeEqualToRequest "create_a_floating_ip.json"

                underTest.floatingIps.create(createRequest) shouldBe ItemCreated(
                    action = Action(
                        id = Action.Id(13),
                        command = "create_floating_ip",
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            ServerResource(id = Server.Id(42)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50:00+00:00"),
                        status = Action.Status.RUNNING,
                    ),
                    item = expectedIp.copy(
                        id = FloatingIp.Id(4711),
                        name = "Web Frontend",
                        created = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        description = "Web Frontend",
                        labels = mapOf(
                            "env" to "dev",
                        ),
                        homeLocation = expectedIp.homeLocation.copy(id = Location.Id(1)),
                    ),
                )
            }

            should("update a floating ip") {

                val updateRequest = UpdateFloatingIp(
                    name = "my-resource",
                    labels = mapOf(
                        "environment" to "prod",
                        "example.com/my" to "label",
                        "just-a-key" to "",
                    ),
                    description = "This describes my resource",
                )

                jsonEncoder().encodeToString(updateRequest) shouldBeEqualToRequest "update_a_floating_ip.json"

                underTest.floatingIps.update(floatingIpId, updateRequest) shouldBe Item(
                    expectedIp.copy(
                        id = FloatingIp.Id(4711),
                        name = "Web Frontend",
                        created = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        description = "Web Frontend",
                        labels = mapOf(
                            "labelkey" to "value",
                        ),
                        homeLocation = expectedIp.homeLocation.copy(id = Location.Id(1)),
                    ),
                )
            }

            should("delete a floating ip") {
                underTest.floatingIps.delete(floatingIpId) shouldBe Unit
            }

            should("assign a Floating Ip to a server") {
                val assignToServerRequest = AssignFloatingIp(Server.Id(42))

                jsonEncoder().encodeToString(assignToServerRequest) shouldBeEqualToRequest "assign_a_floating_ip_to_a_server.json"

                underTest.floatingIps.assign(floatingIpId, assignToServerRequest) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "assign_floating_ip",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                        progress = 100,
                        resources = listOf(
                            ServerResource(id = Server.Id(42)),
                            FloatingIpResource(id = FloatingIp.Id(4711)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.SUCCESS,
                    ),
                )
            }

            should("unassign a floating ip from a server") {
                underTest.floatingIps.unassign(floatingIpId) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "unassign_floating_ip",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                        progress = 100,
                        resources = listOf(
                            ServerResource(id = Server.Id(42)),
                            FloatingIpResource(id = FloatingIp.Id(4711)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.SUCCESS,
                    ),
                )
            }

            should("change floating ip protection") {
                val changeProtectionRequest = ChangeDeleteProtection(false)

                jsonEncoder().encodeToString(changeProtectionRequest) shouldBeEqualToRequest "change_floating_ip_protection.json"

                underTest.floatingIps.changeProtection(floatingIpId, changeProtectionRequest) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "change_protection",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                        progress = 100,
                        resources = listOf(
                            FloatingIpResource(id = FloatingIp.Id(4711)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.SUCCESS,
                    ),
                )
            }

            should("change floating ip reverse dns") {
                val changeReverseDnsRequest = ChangeReverseDns(
                    "server.example.com",
                    "2001:db8::1",
                )

                jsonEncoder().encodeToString(changeReverseDnsRequest) shouldBeEqualToRequest "change_floating_ip_reverse_dns.json"

                underTest.floatingIps.changeReverseDns(floatingIpId, changeReverseDnsRequest) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "change_dns_ptr",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                        progress = 100,
                        resources = listOf(
                            FloatingIpResource(id = FloatingIp.Id(4711)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.SUCCESS,
                    ),
                )
            }
        }
    })
