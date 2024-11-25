package tech.sco.hetznerkloud

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import tech.sco.hetznerkloud.model.LoadBalancerType
import tech.sco.hetznerkloud.model.Price
import tech.sco.hetznerkloud.model.Pricing
import tech.sco.hetznerkloud.model.ServerType
import tech.sco.hetznerkloud.response.Item

class PricingApiTest :
    ShouldSpec({
        val apiToken = ApiToken("foo")
        val mockEngine = createMockEngine(apiToken)
        val underTest = CloudApiClient.of(apiToken, mockEngine)

        should("get all prices") {
            underTest.pricing.all() shouldBe Item(
                Pricing(
                    currency = "EUR",
                    floatingIp = Pricing.FloatingIp(priceMonthly = Price.Amount(gross = "1.1900", net = "1.0000")),
                    floatingIps = listOf(
                        Pricing.FloatingIpPrices(
                            prices = listOf(
                                Pricing.FloatingIpPrices.Element(
                                    location = "fsn1",
                                    priceMonthly = Price.Amount(
                                        gross = "1.1900",
                                        net = "1.0000",
                                    ),
                                ),
                            ),
                            type = "ipv4",
                        ),
                    ),
                    image = Pricing.Image(
                        pricePerGbMonth = Price.Amount(
                            gross = "1.1900",
                            net = "1.0000",
                        ),
                    ),
                    loadBalancerTypes = listOf(
                        Pricing.LoadBalancerType(
                            id = LoadBalancerType.Id(1),
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
                    ),
                    primaryIps = listOf(
                        Pricing.PrimaryIpPrices(
                            prices = listOf(
                                Pricing.PrimaryIpPrices.Element(
                                    location = "fsn1",
                                    priceHourly = Price.Amount(
                                        gross = "1.1900",
                                        net = "1.0000",
                                    ),
                                    priceMonthly = Price.Amount(
                                        gross = "1.1900",
                                        net = "1.0000",
                                    ),
                                ),
                            ),
                            type = "ipv4",
                        ),
                    ),
                    serverBackup = Pricing.ServerBackup(
                        percentage = "20.00",
                    ),
                    serverTypes = listOf(
                        Pricing.ServerType(
                            id = ServerType.Id(104),
                            name = "cpx11",
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
                    ),
                    vatRate = "19.00",
                    volume = Pricing.Volume(
                        pricePerGbMonth = Price.Amount(
                            gross = "1.1900",
                            net = "1.0000",
                        ),
                    ),
                ),
            )
        }
    })
