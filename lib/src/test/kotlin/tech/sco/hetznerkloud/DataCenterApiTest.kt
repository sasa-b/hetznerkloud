package tech.sco.hetznerkloud

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import tech.sco.hetznerkloud.model.Datacenter
import tech.sco.hetznerkloud.model.Location
import tech.sco.hetznerkloud.model.Meta
import tech.sco.hetznerkloud.response.DatacenterItem
import tech.sco.hetznerkloud.response.DatacenterList

class DataCenterApiTest :
    ShouldSpec({
        val datacenterId = 42L
        val apiToken = ApiToken("foo")
        val mockEngine = createMockEngine(apiToken) { datacenterId }
        val underTest = CloudApiClient.of(apiToken, mockEngine)

        context("Datacenter resource read API") {

            val expectedDatacenter = Datacenter(
                id = 42,
                description = "Falkenstein DC Park 8",
                location =
                Location(
                    id = 42,
                    city = "Falkenstein",
                    country = "DE",
                    description = "Falkenstein DC Park 1",
                    latitude = 50.47612,
                    longitude = 12.370071,
                    name = "fsn1",
                    networkZone = "eu-central",
                ),
                name = "fsn1-dc8",
                serverTypes =
                Datacenter.ServerTypes(
                    available = listOf(1, 2, 3),
                    availableForMigration = listOf(1, 2, 3),
                    supported = listOf(1, 2, 3),
                ),
            )

            should("get all Datacenters") {
                underTest.datacenters() shouldBe
                    DatacenterList(
                        meta = Meta(pagination = Meta.Pagination(lastPage = 4, nextPage = 4, page = 3, perPage = 25, previousPage = 2, totalEntries = 100)),
                        datacenters =
                        listOf(expectedDatacenter),
                        recommendation = 1,
                    )
            }

            should("get Datacenter by id") {
                underTest.datacenters(datacenterId) shouldBe DatacenterItem(expectedDatacenter)
            }
        }
    })
