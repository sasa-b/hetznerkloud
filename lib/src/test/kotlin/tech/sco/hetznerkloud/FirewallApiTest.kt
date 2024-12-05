package tech.sco.hetznerkloud

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.encodeToString
import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.ActionFailedError
import tech.sco.hetznerkloud.model.Firewall
import tech.sco.hetznerkloud.model.FirewallResource
import tech.sco.hetznerkloud.model.Meta
import tech.sco.hetznerkloud.model.ResourceType
import tech.sco.hetznerkloud.model.Server
import tech.sco.hetznerkloud.model.ServerResource
import tech.sco.hetznerkloud.request.CreateFirewall
import tech.sco.hetznerkloud.request.LabelSelector
import tech.sco.hetznerkloud.request.UpdateResource
import tech.sco.hetznerkloud.response.FirewallCreated
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.Items
import java.time.OffsetDateTime

class FirewallApiTest :
    ShouldSpec({
        val firewallId = Firewall.Id(42)
        val apiToken = ApiToken("foo")
        val mockEngine = createMockEngine(apiToken) { mapOf("id" to firewallId.value.toString(), "action_id" to "42") }
        val underTest = CloudApiClient.of(apiToken, mockEngine)

        val expectedFirewall = Firewall(
            id = firewallId,
            appliedTo = listOf(
                Firewall.AppliedTo(
                    appliedToResources = listOf(
                        Firewall.AppliedTo.Resource(
                            server = Firewall.AppliedTo.ServerResource(id = Server.Id(42)),
                            type = "server",
                        ),
                    ),
                    labelSelector = LabelSelector(selector = "env=prod"),
                    server = Firewall.AppliedTo.ServerResource(id = Server.Id(42)),
                    type = "server",
                ),
            ),
            created = OffsetDateTime.parse("2016-01-30T23:55:00+00:00"),
            labels = mapOf(
                "environment" to "prod",
                "example.com/my" to "label",
                "just-a-key" to "",
            ),
            name = "new-name",
            rules = listOf(
                Firewall.Rule(
                    description = null,
                    destinationIps = listOf(),
                    direction = Firewall.Direction.IN,
                    port = "80",
                    protocol = Firewall.Protocol.TCP,
                    sourceIps = listOf(
                        "28.239.13.1/32",
                        "28.239.14.0/24",
                        "ff21:1eac:9a3b:ee58:5ca:990c:8bc9:c03b/128",
                    ),
                ),
            ),
        )

        context("Firewall resource read API") {

            should("get all firewalls") {

                underTest.firewalls.all() shouldBe Items(
                    meta = Meta.of(lastPage = 4, nextPage = 4, page = 3, perPage = 25, previousPage = 2, totalEntries = 100),
                    items = listOf(expectedFirewall),
                )
            }

            should("get firewall by id") {

                underTest.firewalls.find(firewallId) shouldBe Item(expectedFirewall)
            }

            should("get all Firewall actions") {
                underTest.actions.all(ResourceType.FIREWALL) shouldBe Items(
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

            should("get Firewall actions") {
                underTest.actions.all(resourceId = firewallId) shouldBe Items(
                    meta = Meta(pagination = Meta.Pagination(lastPage = 4, nextPage = 4, page = 3, perPage = 25, previousPage = 2, totalEntries = 100)),
                    items = listOf(
                        Action(
                            id = Action.Id(13),
                            command = "set_firewall_rules",
                            error = ActionFailedError(message = "Action failed"),
                            finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                            progress = 100,
                            resources = listOf(
                                FirewallResource(id = Firewall.Id(38)),
                            ),
                            started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                            status = Action.Status.SUCCESS,
                        ),
                    ),
                )
            }

            should("get a Firewall action") {
                underTest.actions.find(ResourceType.FIREWALL, actionId = Action.Id(42)) shouldBe Item(
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

            should("get a Firewall action for volume") {
                underTest.actions.find(resourceId = firewallId, actionId = Action.Id(42)) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "set_firewall_rules",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                        progress = 100,
                        resources = listOf(FirewallResource(id = Firewall.Id(38))),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.SUCCESS,
                    ),
                )
            }
        }

        context("Firewall resource write API") {

            should("create a Firewall") {
                val createRequest = CreateFirewall(
                    applyTo = listOf(
                        CreateFirewall.ApplyTo(
                            server = Firewall.AppliedTo.ServerResource(id = Server.Id(42)),
                            type = "server",
                        ),
                    ),
                    labels = mapOf(
                        "env" to "dev",
                    ),
                    name = "Corporate Intranet Protection",
                    rules = listOf(
                        Firewall.Rule(
                            description = "Allow port 80",
                            direction = Firewall.Direction.IN,
                            port = "80",
                            protocol = Firewall.Protocol.TCP,
                            sourceIps = listOf(
                                "28.239.13.1/32",
                                "28.239.14.0/24",
                                "ff21:1eac:9a3b:ee58:5ca:990c:8bc9:c03b/128",
                            ),
                        ),
                    ),
                )

                jsonEncoder().encodeToString(createRequest) shouldBeEqualToRequest "create_a_firewall.json"

                underTest.firewalls.create(createRequest) shouldBe FirewallCreated(
                    actions = listOf(
                        Action(
                            id = Action.Id(13),
                            command = "set_firewall_rules",
                            error = ActionFailedError(message = "Action failed"),
                            finished = OffsetDateTime.parse("2016-01-30T23:56:00+00:00"),
                            progress = 100,
                            resources = listOf(
                                FirewallResource(
                                    id = Firewall.Id(38),
                                ),
                            ),
                            started = OffsetDateTime.parse("2016-01-30T23:55:00+00:00"),
                            status = Action.Status.SUCCESS,
                        ),
                        Action(
                            id = Action.Id(14),
                            command = "apply_firewall",
                            error = ActionFailedError(message = "Action failed"),
                            finished = OffsetDateTime.parse("2016-01-30T23:56:00+00:00"),
                            progress = 100,
                            resources = listOf(
                                ServerResource(
                                    id = Server.Id(42),
                                ),
                                FirewallResource(
                                    id = Firewall.Id(38),
                                ),
                            ),
                            started = OffsetDateTime.parse("2016-01-30T23:55:00+00:00"),
                            status = Action.Status.SUCCESS,
                        ),
                    ),
                    item = expectedFirewall,
                )
            }

            should("update a Firewall") {
                val updateRequest = UpdateResource(
                    name = "new-name",
                    labels = mapOf(
                        "environment" to "prod",
                        "example.com/my" to "label",
                        "just-a-key" to "",
                    ),
                )

                jsonEncoder().encodeToString(updateRequest) shouldBeEqualToRequest "update_a_firewall.json"

                underTest.firewalls.update(firewallId, updateRequest) shouldBe Item(expectedFirewall)
            }

            should("delete a Firewall") {
                underTest.firewalls.delete(firewallId) shouldBe Unit
            }
        }
    })
