package tech.sco.hetznerkloud

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import tech.sco.hetznerkloud.model.Datacenter
import tech.sco.hetznerkloud.model.DnsPtr
import tech.sco.hetznerkloud.model.IpType
import tech.sco.hetznerkloud.model.Location
import tech.sco.hetznerkloud.model.Meta
import tech.sco.hetznerkloud.model.NetworkZone
import tech.sco.hetznerkloud.model.PrimaryIp
import tech.sco.hetznerkloud.model.Protection
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.Items
import java.time.OffsetDateTime

class PrimaryIpApiTest :
    ShouldSpec({
        val primaryIp = PrimaryIp.Id(42)
        val apiToken = ApiToken("foo")
        val mockEngine = createMockEngine(apiToken) { primaryIp.value }
        val underTest = CloudApiClient.of(apiToken, mockEngine)

        context("Primary IP resource read API") {

            val expectedIp = PrimaryIp(
                id = PrimaryIp.Id(42),
                assigneeId = 17,
                assigneeType = PrimaryIp.AssigneeType.SERVER,
                autoDelete = true,
                blocked = false,
                created = OffsetDateTime.parse("2016-01-30T23:55:00+00:00"),
                datacenter = Datacenter(
                    id = Datacenter.Id(42),
                    description = "Falkenstein DC Park 8",
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
                    name = "fsn1-dc8",
                    serverTypes = Datacenter.ServerTypes(
                        available = listOf(1, 2, 3),
                        availableForMigration = listOf(1, 2, 3),
                        supported = listOf(1, 2, 3),
                    ),
                ),
                dnsPtr = listOf(
                    DnsPtr(dnsPtr = "server.example.com", ip = "2001:db8::1"),
                ),
                ip = "131.232.99.1",
                labels = mapOf(
                    "environment" to "prod",
                    "example.com/my" to "label",
                    "just-a-key" to "",
                ),
                name = "my-resource",
                protection = Protection(delete = false),
                type = IpType.IPV4,
            )

            should("get all primary ips") {
                underTest.primaryIps.all() shouldBe Items(
                    meta = Meta.of(lastPage = 4, nextPage = 4, page = 3, perPage = 3, previousPage = 2, totalEntries = 100),
                    items = listOf(expectedIp),
                )
            }

            should("get all primary ips") {
                underTest.primaryIps.find(primaryIp) shouldBe Item(expectedIp)
            }
        }

        context("Primary IP resource write API") {
        }
    })
