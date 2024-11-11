package tech.sco.hetznerkloud

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import tech.sco.hetznerkloud.model.DnsPtr
import tech.sco.hetznerkloud.model.FloatingIp
import tech.sco.hetznerkloud.model.IpType
import tech.sco.hetznerkloud.model.Location
import tech.sco.hetznerkloud.model.Meta
import tech.sco.hetznerkloud.model.NetworkZone
import tech.sco.hetznerkloud.model.Protection
import tech.sco.hetznerkloud.model.Server
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.Items
import java.time.OffsetDateTime

class FloatingIpApiTest :
    ShouldSpec({
        val floatingIpId = FloatingIp.Id(42)
        val apiToken = ApiToken("foo")
        val mockEngine = createMockEngine(apiToken) { floatingIpId.value }
        val underTest = CloudApiClient.of(apiToken, mockEngine)

        context("Floating IP resource read API") {

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

            should("get all floating ips") {

                underTest.floatingIps.all() shouldBe Items(
                    meta = Meta.of(lastPage = 4, nextPage = 4, page = 3, perPage = 3, previousPage = 2, totalEntries = 100),
                    items = listOf(expectedIp),
                )
            }

            should("get a floating ip") {

                underTest.floatingIps.find(floatingIpId) shouldBe Item(expectedIp)
            }
        }

        context("Floating IP resource write API") {
        }
    })
