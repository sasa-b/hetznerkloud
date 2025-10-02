package tech.sco.hetznerkloud

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.ktor.http.toURI
import kotlinx.serialization.encodeToString
import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.ActionFailedError
import tech.sco.hetznerkloud.model.LoadBalancer
import tech.sco.hetznerkloud.model.Meta
import tech.sco.hetznerkloud.model.Network
import tech.sco.hetznerkloud.model.NetworkResource
import tech.sco.hetznerkloud.model.NetworkZone
import tech.sco.hetznerkloud.model.Protection
import tech.sco.hetznerkloud.model.ResourceType
import tech.sco.hetznerkloud.model.Server
import tech.sco.hetznerkloud.model.ServerResource
import tech.sco.hetznerkloud.request.AddRoute
import tech.sco.hetznerkloud.request.AddSubnet
import tech.sco.hetznerkloud.request.ChangeDeleteProtection
import tech.sco.hetznerkloud.request.ChangeIpRange
import tech.sco.hetznerkloud.request.CreateNetwork
import tech.sco.hetznerkloud.request.DeleteRoute
import tech.sco.hetznerkloud.request.DeleteSubnet
import tech.sco.hetznerkloud.request.UpdateNetwork
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.Items
import java.time.OffsetDateTime

class NetworkApiTest :
    ShouldSpec({
        val networkId = Network.Id(4711)
        val apiToken = ApiToken("foo")
        val mockEngine = createMockEngine(apiToken) {
            when {
                Route.GET_NETWORK_ACTION.path.toRegex().matches(it.url.toURI().path) -> mapOf("id" to "42")
                else -> mapOf("id" to networkId.value.toString(), "action_id" to "42")
            }
        }
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

            should("get all Network actions") {
                underTest.actions.all(ResourceType.NETWORK) shouldBe Items(
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

            should("get Network actions") {
                underTest.actions.all(resourceId = networkId) shouldBe Items(
                    meta = Meta(pagination = Meta.Pagination(lastPage = 4, nextPage = 4, page = 3, perPage = 25, previousPage = 2, totalEntries = 100)),
                    items = listOf(
                        Action(
                            id = Action.Id(13),
                            command = "add_subnet",
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

            should("get a Network action") {
                underTest.actions.find(ResourceType.NETWORK, actionId = Action.Id(42)) shouldBe Item(
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

            should("get a Network action for network") {
                underTest.actions.find(resourceId = networkId, actionId = Action.Id(42)) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "add_subnet",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                        progress = 100,
                        resources = listOf(NetworkResource(id = Network.Id(4711))),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.SUCCESS,
                    ),
                )
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
                        CreateNetwork.Subnet(
                            ipRange = "10.0.1.0/24",
                            networkZone = NetworkZone.EU_CENTRAL,
                            type = Network.Type.CLOUD,
                            vSwitchId = 1000,
                        ),
                    ),
                )

                jsonEncoder().encodeToString(createRequest) shouldBeEqualToRequest "create_a_network.json"

                underTest.networks.create(createRequest) shouldBe Item(expectedNetwork)
            }

            should("update a Network") {

                val updateRequest = UpdateNetwork(
                    exposeRoutesToVSwitch = false,
                    labels = mapOf(
                        "environment" to "prod",
                        "example.com/my" to "label",
                        "just-a-key" to "",
                    ),
                    name = "new-name",
                )

                jsonEncoder().encodeToString(updateRequest) shouldBeEqualToRequest "update_a_network.json"

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

            should("change Network protection") {
                val changeProtectionRequest = ChangeDeleteProtection(true)

                jsonEncoder().encodeToString(changeProtectionRequest) shouldBeEqualToRequest "change_network_protection.json"

                underTest.networks.changeProtection(networkId, changeProtectionRequest) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "change_protection",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                        progress = 100,
                        resources = listOf(NetworkResource(id = Network.Id(4711))),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.SUCCESS,
                    ),
                )
            }

            should("change Network ip range") {
                val changeIpRangeRequest = ChangeIpRange(
                    ipRange = "10.0.0.0/16",
                )

                jsonEncoder().encodeToString(changeIpRangeRequest) shouldBeEqualToRequest "change_ip_range_of_network.json"

                underTest.networks.changeIpRange(networkId, changeIpRangeRequest) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "change_ip_range",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                        progress = 100,
                        resources = listOf(NetworkResource(id = Network.Id(4711))),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.SUCCESS,
                    ),
                )
            }

            should("add route to Network") {
                val addRouteRequest = AddRoute(
                    destination = "10.100.1.0/24",
                    gateway = "10.0.1.1",
                )

                jsonEncoder().encodeToString(addRouteRequest) shouldBeEqualToRequest "add_a_route_to_network.json"

                underTest.networks.addRoute(networkId, addRouteRequest) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "add_route",
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(NetworkResource(id = Network.Id(4711))),
                        started = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }

            should("delete route from Network") {
                val deleteRouteRequest = DeleteRoute(
                    destination = "10.100.1.0/24",
                    gateway = "10.0.1.1",
                )

                jsonEncoder().encodeToString(deleteRouteRequest) shouldBeEqualToRequest "delete_a_route_from_network.json"

                underTest.networks.deleteRoute(networkId, deleteRouteRequest) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "delete_route",
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(NetworkResource(id = Network.Id(4711))),
                        started = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }

            should("add subnet to Network") {
                val addSubnetRequest = AddSubnet(
                    ipRange = "10.0.1.0/24",
                    networkZone = NetworkZone.EU_CENTRAL,
                    type = Network.Type.CLOUD,
                    vSwitchId = 1000,
                )

                jsonEncoder().encodeToString(addSubnetRequest) shouldBeEqualToRequest "add_a_subnet_to_network.json"

                underTest.networks.addSubnet(networkId, addSubnetRequest) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "add_subnet",
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(NetworkResource(id = Network.Id(4711))),
                        started = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }

            should("delete subnet from Network") {
                val deleteSubnetRequest = DeleteSubnet(
                    ipRange = "10.0.1.0/24",
                )

                jsonEncoder().encodeToString(deleteSubnetRequest) shouldBeEqualToRequest "delete_a_subnet_from_network.json"

                underTest.networks.deleteSubnet(networkId, deleteSubnetRequest) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "delete_subnet",
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(NetworkResource(id = Network.Id(4711))),
                        started = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }
        }
    })
