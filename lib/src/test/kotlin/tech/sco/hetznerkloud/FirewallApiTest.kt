package tech.sco.hetznerkloud

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import tech.sco.hetznerkloud.model.Firewall
import tech.sco.hetznerkloud.model.Meta
import tech.sco.hetznerkloud.model.Server
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.Items
import java.time.OffsetDateTime

class FirewallApiTest :
    ShouldSpec({
        val firewallId = Firewall.Id(42)
        val apiToken = ApiToken("foo")
        val mockEngine = createMockEngine(apiToken) { firewallId.value }
        val underTest = CloudApiClient.of(apiToken, mockEngine)

        context("Firewall resource read API") {

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
            should("delete a Firewall") {
                underTest.firewalls.delete(firewallId) shouldBe Unit
            }
        }
    })
