package tech.sco.hetznerkloud

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.encodeToString
import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.ActionFailedError
import tech.sco.hetznerkloud.model.Certificate
import tech.sco.hetznerkloud.model.LoadBalancer
import tech.sco.hetznerkloud.model.LoadBalancerResource
import tech.sco.hetznerkloud.model.LoadBalancerType
import tech.sco.hetznerkloud.model.Location
import tech.sco.hetznerkloud.model.Meta
import tech.sco.hetznerkloud.model.Network
import tech.sco.hetznerkloud.model.NetworkResource
import tech.sco.hetznerkloud.model.NetworkZone
import tech.sco.hetznerkloud.model.Price
import tech.sco.hetznerkloud.model.Protection
import tech.sco.hetznerkloud.model.ResourceType
import tech.sco.hetznerkloud.model.Server
import tech.sco.hetznerkloud.model.ServerResource
import tech.sco.hetznerkloud.request.AddTarget
import tech.sco.hetznerkloud.request.AttachToNetwork
import tech.sco.hetznerkloud.request.ChangeAlgorithm
import tech.sco.hetznerkloud.request.ChangeDeleteProtection
import tech.sco.hetznerkloud.request.ChangeLoadBalancerType
import tech.sco.hetznerkloud.request.ChangeReverseDns
import tech.sco.hetznerkloud.request.CreateLoadBalancer
import tech.sco.hetznerkloud.request.DeleteService
import tech.sco.hetznerkloud.request.DetachFromNetwork
import tech.sco.hetznerkloud.request.LabelSelector
import tech.sco.hetznerkloud.request.RemoveTarget
import tech.sco.hetznerkloud.request.UpdateResource
import tech.sco.hetznerkloud.request.UpsertService
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.ItemCreated
import tech.sco.hetznerkloud.response.Items
import java.time.OffsetDateTime

@Suppress("LargeClass")
class LoadBalancerApiTest :
    ShouldSpec({

        val loadBalancerId = LoadBalancer.Id(42)
        val apiToken = ApiToken("foo")
        val mockEngine = createMockEngine(apiToken) { mapOf("id" to loadBalancerId.value.toString(), "action_id" to "42") }
        val underTest = CloudApiClient.of(apiToken, mockEngine)

        context("Load balancer resource read API") {

            val expectedLoadBalancer = LoadBalancer(
                id = loadBalancerId,
                algorithm = LoadBalancer.Algorithm(
                    type = LoadBalancer.Algorithm.Type.ROUND_ROBIN,
                ),
                created = OffsetDateTime.parse("2016-01-30T23:55:00+00:00"),
                includedTraffic = 10000L,
                ingoingTraffic = null,
                labels = mapOf(
                    "environment" to "prod",
                    "example.com/my" to "label",
                    "just-a-key" to "",
                ),
                loadBalancerType = LoadBalancerType(
                    id = LoadBalancerType.Id(1),
                    deprecated = OffsetDateTime.parse("2016-01-30T23:50:00+00:00"),
                    description = "LB11",
                    maxAssignedCertificates = 10,
                    maxConnections = 20000L,
                    maxServices = 5,
                    maxTargets = 25,
                    name = "lb11",
                    prices = listOf(
                        Price(
                            includedTraffic = 654321L,
                            location = "fsn1",
                            priceHourly = Price.Amount(
                                gross = "1.1900",
                                net = "1.0000",
                            ),
                            priceMonthly = Price.Amount(
                                gross = "1.1900",
                                net = "1.0000",
                            ),
                            pricePerTbTraffic = Price.Amount(
                                gross = "1.1900",
                                net = "1.0000",
                            ),
                        ),
                    ),
                ),
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
                name = "my-resource",
                outgoingTraffic = null,
                privateNet = listOf(
                    LoadBalancer.PrivateNetwork(
                        ip = "10.0.0.2",
                        network = Network.Id(4711),
                    ),
                ),
                protection = Protection(delete = false),
                publicNet = LoadBalancer.PublicNetwork(
                    enabled = false,
                    ipv4 = LoadBalancer.PublicNetwork.Ip(
                        dnsPtr = "lb1.example.com",
                        ip = "1.2.3.4",
                    ),
                    ipv6 = LoadBalancer.PublicNetwork.Ip(
                        dnsPtr = "lb1.example.com",
                        ip = "2001:db8::1",
                    ),
                ),
                services = listOf(
                    LoadBalancer.Service(
                        destinationPort = 80,
                        healthCheck = LoadBalancer.Service.HealthCheck(
                            http = LoadBalancer.Service.HealthCheck.Http(
                                domain = "example.com",
                                path = "/",
                                response = "{\"status\": \"ok\"}",
                                statusCodes = listOf(
                                    "2??",
                                    "3??",
                                ),
                                tls = false,
                            ),
                            interval = 15,
                            port = 4711,
                            protocol = LoadBalancer.Service.Protocol.HTTP,
                            retries = 3,
                            timeout = 10,
                        ),
                        http = LoadBalancer.Service.Http(
                            certificates = listOf(
                                Certificate.Id(897),
                            ),
                            cookieLifetime = 300,
                            cookieName = "HCLBSTICKY",
                            redirectHttp = true,
                            stickySessions = true,
                        ),
                        listenPort = 443,
                        protocol = LoadBalancer.Service.Protocol.HTTPS,
                        proxyProtocol = false,
                    ),
                ),
                targets = listOf(
                    LoadBalancer.Target(
                        healthStatus = listOf(
                            LoadBalancer.Target.HealthStatus(
                                listenPort = 443,
                                status = "healthy",
                            ),
                        ),
                        ip = LoadBalancer.Target.Ip(
                            ip = "203.0.113.1",
                        ),
                        labelSelector = mapOf(
                            "selector" to "env=prod",
                        ),
                        server = LoadBalancer.Target.Server(
                            id = Server.Id(80),
                        ),
                        type = LoadBalancer.Target.Type.SERVER,
                        usePrivateIp = false,
                        targets = listOf(
                            LoadBalancer.Target(
                                healthStatus = listOf(
                                    LoadBalancer.Target.HealthStatus(
                                        listenPort = 443,
                                        status = "healthy",
                                    ),
                                ),
                                server = LoadBalancer.Target.Server(
                                    id = Server.Id(80),
                                ),
                                type = LoadBalancer.Target.Type.SERVER,
                                usePrivateIp = false,
                            ),
                        ),
                    ),
                ),
            )

            should("get all Load balancers") {

                underTest.loadBalancers.all() shouldBe Items(
                    meta = Meta.of(lastPage = 4, nextPage = 4, page = 3, perPage = 25, previousPage = 2, totalEntries = 100),
                    items = listOf(expectedLoadBalancer),
                )
            }

            should("get Load balancer by id") {

                underTest.loadBalancers.find(loadBalancerId) shouldBe Item(expectedLoadBalancer)
            }

            should("get all Load Balancer actions") {
                underTest.actions.all(ResourceType.LOAD_BALANCER) shouldBe Items(
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

            should("get Load Balancer actions") {
                underTest.actions.all(resourceId = loadBalancerId) shouldBe Items(
                    meta = Meta(pagination = Meta.Pagination(lastPage = 4, nextPage = 4, page = 3, perPage = 25, previousPage = 2, totalEntries = 100)),
                    items = listOf(
                        Action(
                            id = Action.Id(13),
                            command = "add_service",
                            error = ActionFailedError(message = "Action failed"),
                            finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                            progress = 100,
                            resources = listOf(LoadBalancerResource(id = LoadBalancer.Id(4711))),
                            started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                            status = Action.Status.SUCCESS,
                        ),
                    ),
                )
            }

            should("get a Load Balancer action") {
                underTest.actions.find(ResourceType.LOAD_BALANCER, actionId = Action.Id(42)) shouldBe Item(
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

            should("get a Load Balancer action for server") {
                underTest.actions.find(resourceId = loadBalancerId, actionId = Action.Id(42)) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "change_protection",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                        progress = 100,
                        resources = listOf(LoadBalancerResource(id = LoadBalancer.Id(4711))),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.SUCCESS,
                    ),
                )
            }
        }

        context("Load Balancer resource write API") {

            val expectedLoadBalancer = LoadBalancer(
                id = LoadBalancer.Id(4711),
                algorithm = LoadBalancer.Algorithm(
                    type = LoadBalancer.Algorithm.Type.ROUND_ROBIN,
                ),
                created = OffsetDateTime.parse("2016-01-30T23:50Z"),
                includedTraffic = 654321L,
                ingoingTraffic = 123456L,
                labels = mapOf(
                    "env" to "dev",
                ),
                loadBalancerType = LoadBalancerType(
                    id = LoadBalancerType.Id(1),
                    deprecated = OffsetDateTime.parse("2016-01-30T23:50:00+00:00"),
                    description = "LB11",
                    maxAssignedCertificates = 10,
                    maxConnections = 20000L,
                    maxServices = 5,
                    maxTargets = 25,
                    name = "lb11",
                    prices = listOf(
                        Price(
                            includedTraffic = 654321L,
                            location = "fsn1",
                            priceHourly = Price.Amount(
                                gross = "1.1900000000000000",
                                net = "1.0000000000",
                            ),
                            priceMonthly = Price.Amount(
                                gross = "1.1900000000000000",
                                net = "1.0000000000",
                            ),
                            pricePerTbTraffic = Price.Amount(
                                gross = "1.1900000000000000",
                                net = "1.0000000000",
                            ),
                        ),
                    ),
                ),
                location = Location(
                    id = Location.Id(1),
                    city = "Falkenstein",
                    country = "DE",
                    description = "Falkenstein DC Park 1",
                    latitude = 50.47612,
                    longitude = 12.370071,
                    name = "fsn1",
                    networkZone = NetworkZone.EU_CENTRAL,
                ),
                name = "Web Frontend",
                outgoingTraffic = null,
                privateNet = listOf(
                    LoadBalancer.PrivateNetwork(
                        ip = "10.0.0.2",
                        network = Network.Id(4711),
                    ),
                ),
                protection = Protection(delete = false),
                publicNet = LoadBalancer.PublicNetwork(
                    enabled = false,
                    ipv4 = LoadBalancer.PublicNetwork.Ip(
                        ip = "1.2.3.4",
                    ),
                    ipv6 = LoadBalancer.PublicNetwork.Ip(
                        ip = "2001:db8::1",
                    ),
                ),
                services = listOf(
                    LoadBalancer.Service(
                        destinationPort = 80,
                        healthCheck = LoadBalancer.Service.HealthCheck(
                            http = LoadBalancer.Service.HealthCheck.Http(
                                domain = "example.com",
                                path = "/",
                                response = "{\"status\": \"ok\"}",
                                statusCodes = listOf("2??,3??"),
                                tls = false,
                            ),
                            interval = 15,
                            port = 4711,
                            protocol = LoadBalancer.Service.Protocol.HTTP,
                            retries = 3,
                            timeout = 10,
                        ),
                        http = LoadBalancer.Service.Http(
                            certificates = listOf(
                                Certificate.Id(897),
                            ),
                            cookieLifetime = 300,
                            cookieName = "HCLBSTICKY",
                            redirectHttp = true,
                            stickySessions = true,
                        ),
                        listenPort = 443,
                        protocol = LoadBalancer.Service.Protocol.HTTP,
                        proxyProtocol = false,
                    ),
                ),
                targets = listOf(
                    LoadBalancer.Target(
                        healthStatus = listOf(
                            LoadBalancer.Target.HealthStatus(
                                listenPort = 443,
                                status = "healthy",
                            ),
                        ),
                        ip = null,
                        labelSelector = emptyMap(),
                        server = LoadBalancer.Target.Server(
                            id = Server.Id(80),
                        ),
                        type = LoadBalancer.Target.Type.SERVER,
                        usePrivateIp = true,
                        targets = listOf(
                            LoadBalancer.Target(
                                healthStatus = listOf(
                                    LoadBalancer.Target.HealthStatus(
                                        listenPort = 443,
                                        status = "healthy",
                                    ),
                                ),
                                server = LoadBalancer.Target.Server(
                                    id = Server.Id(80),
                                ),
                                type = LoadBalancer.Target.Type.SERVER,
                                usePrivateIp = true,
                            ),
                        ),
                    ),
                ),
            )

            should("create a Load balancer") {

                val createRequest = CreateLoadBalancer(
                    algorithm = LoadBalancer.Algorithm(type = LoadBalancer.Algorithm.Type.ROUND_ROBIN),
                    labels = mapOf(
                        "environment" to "prod",
                        "example.com/my" to "label",
                        "just-a-key" to "",
                    ),
                    loadBalancerType = "lb11",
                    location = "fsn1",
                    name = "Web Frontend",
                    network = Network.Id(123),
                    networkZone = NetworkZone.EU_CENTRAL,
                    publicInterface = true,
                    services = listOf(
                        LoadBalancer.Service(
                            destinationPort = 80,
                            healthCheck = LoadBalancer.Service.HealthCheck(
                                http = LoadBalancer.Service.HealthCheck.Http(
                                    domain = "example.com",
                                    path = "/",
                                    response = "{\"status\": \"ok\"}",
                                    statusCodes = listOf("2??", "3??"),
                                    tls = false,
                                ),
                                interval = 15,
                                port = 4711,
                                protocol = LoadBalancer.Service.Protocol.HTTP,
                                retries = 3,
                                timeout = 10,
                            ),
                            http = LoadBalancer.Service.Http(
                                certificates = listOf(
                                    Certificate.Id(value = 897),
                                ),
                                cookieLifetime = 300,
                                cookieName = "HCLBSTICKY",
                                redirectHttp = true,
                                stickySessions = true,
                            ),
                            listenPort = 443,
                            protocol = LoadBalancer.Service.Protocol.HTTPS,
                            proxyProtocol = false,
                        ),
                    ),
                    targets = listOf(
                        LoadBalancer.Target(
                            ip = LoadBalancer.Target.Ip(ip = "203.0.113.1"),
                            labelSelector = mapOf("selector" to "env=prod"),
                            server = LoadBalancer.Target.Server(Server.Id(80)),
                            type = LoadBalancer.Target.Type.SERVER,
                            usePrivateIp = true,
                        ),
                    ),
                )

                jsonEncoder().encodeToString(createRequest) shouldBeEqualToRequest "create_a_load_balancer.json"

                underTest.loadBalancers.create(createRequest) shouldBe ItemCreated(
                    action = Action(
                        id = Action.Id(13),
                        command = "create_load_balancer",
                        error = ActionFailedError(
                            message = "Action failed",
                        ),
                        finished = OffsetDateTime.parse("2016-01-30T23:56:00+00:00"),
                        progress = 100,
                        resources = listOf(
                            LoadBalancerResource(id = LoadBalancer.Id(4711)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.SUCCESS,
                    ),
                    item = expectedLoadBalancer,
                )
            }

            should("update a Load balancer") {

                val updateRequest = UpdateResource(
                    labels = mapOf(
                        "environment" to "prod",
                        "example.com/my" to "label",
                        "just-a-key" to "",
                    ),
                    name = "new-name",
                )

                jsonEncoder().encodeToString(updateRequest) shouldBeEqualToRequest "update_a_load_balancer.json"

                underTest.loadBalancers.update(loadBalancerId, updateRequest) shouldBe Item(
                    expectedLoadBalancer.copy(
                        labels = mapOf("labelkey" to "value"),
                        name = "new-name",
                        targets = listOf(
                            expectedLoadBalancer.targets[0].copy(
                                ip = LoadBalancer.Target.Ip(ip = "203.0.113.1"),
                                labelSelector = mapOf("selector" to "env=prod"),
                            ),
                        ),
                    ),
                )
            }

            should("delete a Load balancer") {
                underTest.loadBalancers.delete(loadBalancerId) shouldBe Unit
            }

            should("add Service to Load balancer") {
                val addServiceRequest = UpsertService(
                    destinationPort = 80,
                    LoadBalancer.Service.HealthCheck(
                        http = LoadBalancer.Service.HealthCheck.Http(
                            domain = "example.com",
                            path = "/",
                            response = "{\"status\": \"ok\"}",
                            statusCodes = listOf("2??", "3??"),
                            tls = false,
                        ),
                        interval = 15,
                        port = 4711,
                        protocol = LoadBalancer.Service.Protocol.HTTP,
                        retries = 3,
                        timeout = 10,
                    ),
                    http = LoadBalancer.Service.Http(
                        certificates = listOf(Certificate.Id(897)),
                        redirectHttp = true,
                        stickySessions = true,
                    ),
                    listenPort = 443,
                    protocol = LoadBalancer.Service.Protocol.HTTPS,
                    proxyProtocol = false,
                )

                jsonEncoder().encodeToString(addServiceRequest) shouldBeEqualToRequest "load_balancer_add_service.json"

                underTest.loadBalancers.addService(loadBalancerId, addServiceRequest) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "add_service",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                        progress = 100,
                        resources = listOf(LoadBalancerResource(id = LoadBalancer.Id(4711))),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.SUCCESS,
                    ),
                )
            }

            should("update a Load balancer service") {
                val updateServiceRequest = UpsertService(
                    destinationPort = 80,
                    LoadBalancer.Service.HealthCheck(
                        http = LoadBalancer.Service.HealthCheck.Http(
                            domain = "example.com",
                            path = "/",
                            response = "{\"status\": \"ok\"}",
                            statusCodes = listOf("2??", "3??"),
                            tls = false,
                        ),
                        interval = 15,
                        port = 4711,
                        protocol = LoadBalancer.Service.Protocol.HTTP,
                        retries = 3,
                        timeout = 10,
                    ),
                    http = LoadBalancer.Service.Http(
                        certificates = listOf(Certificate.Id(897)),
                        redirectHttp = true,
                        stickySessions = true,
                    ),
                    listenPort = 443,
                    protocol = LoadBalancer.Service.Protocol.HTTPS,
                    proxyProtocol = false,
                )

                jsonEncoder().encodeToString(updateServiceRequest) shouldBeEqualToRequest "load_balancer_update_a_service.json"

                underTest.loadBalancers.updateService(loadBalancerId, updateServiceRequest) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "update_service",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                        progress = 100,
                        resources = listOf(LoadBalancerResource(id = LoadBalancer.Id(4711))),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.SUCCESS,
                    ),
                )
            }

            should("delete service from Load Balancer") {
                val deleteServiceRequest = DeleteService(listenPort = 443)

                jsonEncoder().encodeToString(deleteServiceRequest) shouldBeEqualToRequest "load_balancer_delete_service.json"

                underTest.loadBalancers.deleteService(loadBalancerId, deleteServiceRequest) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "delete_service",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                        progress = 100,
                        resources = listOf(
                            LoadBalancerResource(id = LoadBalancer.Id(4711)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.SUCCESS,
                    ),
                )
            }

            should("add Target to Load balancer") {
                val addTargetRequest = AddTarget(
                    ip = LoadBalancer.Target.Ip(
                        "203.0.113.1",
                    ),
                    labelSelector = LabelSelector(
                        selector = "env=prod",
                    ),
                    server = LoadBalancer.Target.Server(
                        id = Server.Id(80),
                    ),
                    type = LoadBalancer.Target.Type.SERVER,
                    usePrivateIp = true,
                )

                jsonEncoder().encodeToString(addTargetRequest) shouldBeEqualToRequest "load_balancer_add_target.json"

                underTest.loadBalancers.addTarget(loadBalancerId, addTargetRequest) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "add_target",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                        progress = 100,
                        resources = listOf(LoadBalancerResource(id = LoadBalancer.Id(4711))),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.SUCCESS,
                    ),
                )
            }

            should("remove Target to Load balancer") {
                val removeTargetRequest = RemoveTarget(
                    ip = LoadBalancer.Target.Ip(
                        ip = "203.0.113.1",
                    ),
                    labelSelector = LabelSelector(
                        selector = "env=prod",
                    ),
                    server = LoadBalancer.Target.Server(
                        id = Server.Id(80),
                    ),
                )

                jsonEncoder().encodeToString(removeTargetRequest) shouldBeEqualToRequest "load_balancer_remove_a_target.json"

                underTest.loadBalancers.removeTarget(loadBalancerId, removeTargetRequest) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "remove_target",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                        progress = 100,
                        resources = listOf(LoadBalancerResource(id = LoadBalancer.Id(4711))),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.SUCCESS,
                    ),
                )
            }

            should("attach Load Balancer to network") {
                val attachToNetworkRequest = AttachToNetwork.LoadBalancer(
                    ip = "10.0.1.1",
                    network = Network.Id(4711),
                )

                jsonEncoder().encodeToString(attachToNetworkRequest) shouldBeEqualToRequest "attach_a_load_balancer_to_network.json"

                underTest.loadBalancers.attachToNetwork(loadBalancerId, attachToNetworkRequest) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "attach_to_network",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                        progress = 100,
                        resources = listOf(LoadBalancerResource(id = LoadBalancer.Id(4711))),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.SUCCESS,
                    ),
                )
            }

            should("detach Load Balancer from network") {
                val detachFromNetworkRequest = DetachFromNetwork(Network.Id(4711))

                jsonEncoder().encodeToString(detachFromNetworkRequest) shouldBeEqualToRequest "detach_a_load_balancer_from_network.json"

                underTest.loadBalancers.detachFromNetwork(loadBalancerId, detachFromNetworkRequest) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "detach_from_network",
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            ServerResource(id = Server.Id(42)),
                            NetworkResource(id = Network.Id(4711)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }

            should("change Load Balancer algorithm") {
                val changeAlgorithmRequest = ChangeAlgorithm(LoadBalancer.Algorithm.Type.ROUND_ROBIN)

                jsonEncoder().encodeToString(changeAlgorithmRequest) shouldBeEqualToRequest "change_load_balancer_algorithm.json"

                underTest.loadBalancers.changeAlgorithm(loadBalancerId, changeAlgorithmRequest) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "change_algorithm",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                        progress = 100,
                        resources = listOf(LoadBalancerResource(id = LoadBalancer.Id(4711))),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.SUCCESS,
                    ),
                )
            }

            should("change Load Balancer reverse dns") {
                val changeReverseDnsRequest = ChangeReverseDns(
                    dnsPtr = "lb1.example.com",
                    ip = "1.2.3.4",
                )

                jsonEncoder().encodeToString(changeReverseDnsRequest) shouldBeEqualToRequest "change_load_balancer_reverse_dns.json"

                underTest.loadBalancers.changeReverseDns(loadBalancerId, changeReverseDnsRequest) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "change_dns_ptr",
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            LoadBalancerResource(id = LoadBalancer.Id(42)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }

            should("change Load Balancer protection") {
                val changeProtection = ChangeDeleteProtection(delete = true)

                jsonEncoder().encodeToString(changeProtection) shouldBeEqualToRequest "change_load_balancer_protection.json"

                underTest.loadBalancers.changeProtection(loadBalancerId, changeProtection) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "change_protection",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                        progress = 100,
                        resources = listOf(
                            LoadBalancerResource(id = LoadBalancer.Id(4711)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.SUCCESS,
                    ),
                )
            }

            should("change Load Balancer type") {
                val changeTypeRequest = ChangeLoadBalancerType(loadBalancerType = "lb21")

                jsonEncoder().encodeToString(changeTypeRequest) shouldBeEqualToRequest "change_load_balancer_type.json"

                underTest.loadBalancers.changeType(loadBalancerId, changeTypeRequest) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "change_load_balancer_type",
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

            should("enable Load Balancer public interface") {
                underTest.loadBalancers.enablePublicInterface(loadBalancerId) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "enable_public_interface",
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            ServerResource(id = Server.Id(42)),
                            NetworkResource(id = Network.Id(4711)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }

            should("disable Load Balancer public interface") {
                underTest.loadBalancers.disablePublicInterface(loadBalancerId) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "disable_public_interface",
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            ServerResource(id = Server.Id(42)),
                            NetworkResource(id = Network.Id(4711)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }
        }
    })
