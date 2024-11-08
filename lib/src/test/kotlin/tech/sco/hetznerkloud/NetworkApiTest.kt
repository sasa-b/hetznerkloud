package tech.sco.hetznerkloud

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import tech.sco.hetznerkloud.model.LoadBalancer
import tech.sco.hetznerkloud.model.Meta
import tech.sco.hetznerkloud.model.Network
import tech.sco.hetznerkloud.model.NetworkZone
import tech.sco.hetznerkloud.model.Protection
import tech.sco.hetznerkloud.model.Server
import tech.sco.hetznerkloud.request.CreateNetwork
import tech.sco.hetznerkloud.request.UpdateNetwork
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.Items
import java.time.OffsetDateTime

class NetworkApiTest :
    ShouldSpec({
        val networkId = Network.Id(4711)
        val apiToken = ApiToken("foo")
        val mockEngine = createMockEngine(apiToken) { networkId.value }
        val underTest = CloudApiClient.of(apiToken, mockEngine)

        val expectedNetwork = Network(
            id = networkId,
            created = OffsetDateTime.parse("2016-01-30T23:55:00+00:00"),
            exposeRoutesToVSwitch = false,
            ipRange = "10.0.0.0/16",
            labels = mapOf(
                "environment" to "prod",
                "example.com/my" to "label",
                "just-a-key" to "",
            ),
            loadBalancers = listOf(LoadBalancer.Id(42L)),
            name = "mynet",
            protection = Protection(delete = false),
            routes = listOf(
                Network.Route(destination = "10.100.1.0/24", gateway = "10.0.1.1"),
            ),
            servers = listOf(
                Server.Id(42),
            ),
            subnets = listOf(
                Network.Subnet(
                    gateway = "10.0.0.1",
                    ipRange = "10.0.1.0/24",
                    networkZone = NetworkZone.EU_CENTRAL,
                    type = Network.Type.CLOUD,
                    vSwitchId = 1000,
                ),
            ),
        )

        context("Network resource read API") {

            should("get all networks") {

                underTest.networks.all() shouldBe Items(
                    meta = Meta.of(lastPage = 4, nextPage = 4, page = 3, perPage = 25, previousPage = 2, totalEntries = 100),
                    items = listOf(expectedNetwork),
                )
            }

            should("get network by id") {

                underTest.networks.find(networkId) shouldBe Item(expectedNetwork)
            }
        }

        context("Network resource write API") {

            should("create a Network") {
                val createRequest = CreateNetwork(
                    exposeRoutesToVSwitch = false,
                    ipRange = "10.0.0.0/16",
                    labels = mapOf(
                        "environment" to "prod",
                        "example.com/my" to "label",
                        "just-a-key" to "",
                    ),
                    name = "mynet",
                    routes = listOf(
                        Network.Route(
                            destination = "10.100.1.0/24",
                            gateway = "10.0.1.1",
                        ),
                    ),
                    subnets = listOf(
                        Network.Subnet(
                            ipRange = "10.0.1.0/24",
                            networkZone = NetworkZone.EU_CENTRAL,
                            type = Network.Type.CLOUD,
                            vSwitchId = 1000,
                            gateway = "10.0.0.1",
                        ),
                    ),
                )

                underTest.networks.create(createRequest) shouldBe Item(expectedNetwork)
            }

            should("update a Network") {

                val updateRequest = UpdateNetwork(
                    exposeRoutesToVSwitch = true,
                    labels = mapOf(
                        "environment" to "prod",
                        "example.com/my" to "label",
                        "just-a-key" to "",
                    ),
                    name = "new-name",
                )

                underTest.networks.update(networkId, updateRequest) shouldBe Item(
                    Network(
                        id = networkId,
                        created = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        exposeRoutesToVSwitch = true,
                        ipRange = "10.0.0.0/16",
                        labels = mapOf(
                            "labelkey" to "value",
                        ),
                        loadBalancers = listOf(LoadBalancer.Id(42L)),
                        name = "new-name",
                        protection = Protection(delete = false),
                        routes = listOf(
                            Network.Route(destination = "10.100.1.0/24", gateway = "10.0.1.1"),
                        ),
                        servers = listOf(
                            Server.Id(42),
                        ),
                        subnets = listOf(
                            Network.Subnet(
                                gateway = "10.0.0.1",
                                ipRange = "10.0.1.0/24",
                                networkZone = NetworkZone.EU_CENTRAL,
                                type = Network.Type.CLOUD,
                                vSwitchId = null,
                            ),
                        ),
                    ),
                )
            }

            should("delete a Network") {
                underTest.networks.delete(networkId) shouldBe Unit
            }
        }
    })
