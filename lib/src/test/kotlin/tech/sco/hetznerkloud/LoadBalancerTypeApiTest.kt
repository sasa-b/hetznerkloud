package tech.sco.hetznerkloud

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import tech.sco.hetznerkloud.model.LoadBalancerType
import tech.sco.hetznerkloud.model.Meta
import tech.sco.hetznerkloud.model.Price
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.Items
import java.time.OffsetDateTime

class LoadBalancerTypeApiTest :
    ShouldSpec({
        context("Load balancer type resoruce read API") {

            val loadBalancerTypeId = LoadBalancerType.Id(42)
            val apiToken = ApiToken("foo")
            val mockEngine = createMockEngine(apiToken) { loadBalancerTypeId.value }
            val underTest = CloudApiClient.of(apiToken, mockEngine)

            val expectedLoadBalancerType = LoadBalancerType(
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
            )

            should("get all load balancer types") {
                underTest.loadBalancerTypes.all() shouldBe Items(
                    meta = Meta.of(lastPage = 4, nextPage = 4, page = 3, perPage = 25, previousPage = 2, totalEntries = 100),
                    items = listOf(expectedLoadBalancerType),
                )
            }

            should("get load balancer type by id") {
                underTest.loadBalancerTypes.find(loadBalancerTypeId) shouldBe Item(expectedLoadBalancerType)
            }
        }
    })
