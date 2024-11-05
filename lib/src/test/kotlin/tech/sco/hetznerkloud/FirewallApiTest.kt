package tech.sco.hetznerkloud

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.ActionFailedError
import tech.sco.hetznerkloud.model.Firewall
import tech.sco.hetznerkloud.model.Meta
import tech.sco.hetznerkloud.model.Resource
import tech.sco.hetznerkloud.model.Server
import tech.sco.hetznerkloud.request.CreateFirewall
import tech.sco.hetznerkloud.request.UpdateResource
import tech.sco.hetznerkloud.response.FirewallCreated
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.Items
import java.time.OffsetDateTime

class FirewallApiTest :
    ShouldSpec({
        val firewallId = Firewall.Id(42)
        val apiToken = ApiToken("foo")
        val mockEngine = createMockEngine(apiToken) { firewallId.value }
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
                    labelSelector = Firewall.AppliedTo.LabelSelector(selector = "env=prod"),
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
                    direction = "in",
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
                            destinationIps = emptyList(),
                            direction = "in",
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

                underTest.firewalls.create(createRequest) shouldBe FirewallCreated(
                    actions = listOf(
                        Action(
                            id = Action.Id(13),
                            command = "set_firewall_rules",
                            error = ActionFailedError(message = "Action failed"),
                            finished = OffsetDateTime.parse("2016-01-30T23:56:00+00:00"),
                            progress = 100,
                            resources = listOf(
                                Resource(
                                    id = 38,
                                    type = "firewall",
                                ),
                            ),
                            started = OffsetDateTime.parse("2016-01-30T23:55:00+00:00"),
                            status = "success",
                        ),
                        Action(
                            id = Action.Id(14),
                            command = "apply_firewall",
                            error = ActionFailedError(message = "Action failed"),
                            finished = OffsetDateTime.parse("2016-01-30T23:56:00+00:00"),
                            progress = 100,
                            resources = listOf(
                                Resource(
                                    id = 42,
                                    type = "server",
                                ),
                                Resource(
                                    id = 38,
                                    type = "firewall",
                                ),
                            ),
                            started = OffsetDateTime.parse("2016-01-30T23:55:00+00:00"),
                            status = "success",
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

                underTest.firewalls.update(firewallId, updateRequest) shouldBe Item(expectedFirewall)
            }

            should("delete a Firewall") {
                underTest.firewalls.delete(firewallId) shouldBe Unit
            }
        }
    })
