package tech.sco.hetznerkloud

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import tech.sco.hetznerkloud.model.Meta
import tech.sco.hetznerkloud.model.Server
import tech.sco.hetznerkloud.model.ServerType
import tech.sco.hetznerkloud.response.ServerTypeItem
import tech.sco.hetznerkloud.response.ServerTypeList
import java.time.OffsetDateTime

class ServerTypeApiTest :
    ShouldSpec({
        val serverTypeId = ServerType.Id(42)
        val apiToken = ApiToken("foo")
        val mockEngine = createMockEngine(apiToken) { serverTypeId }
        val underTest = CloudApiClient.of(apiToken, mockEngine)

        val expectedServerType = ServerType(
            id = ServerType.Id(1),
            architecture = "x86",
            cores = 2,
            cpuType = "shared",
            deprecated = false,
            deprecation =
            Server.Deprecation(
                announced = OffsetDateTime.parse("2023-06-01T00:00:00+00:00"),
                unavailableAfter = OffsetDateTime.parse("2023-09-01T00:00:00+00:00"),
            ),
            description = "CPX11",
            disk = 40,
            memory = 2,
            name = "cpx11",
            prices =
            listOf(
                ServerType.Price(
                    includedTraffic = 654321,
                    location = "fsn1",
                    priceHourly = ServerType.Price.Amount(gross = "1.1900", net = "1.0000"),
                    priceMonthly = ServerType.Price.Amount(gross = "1.1900", net = "1.0000"),
                    pricePerTbTraffic = ServerType.Price.Amount(gross = "1.1900", net = "1.0000"),
                ),
            ),
            storageType = "local",
        )

        context("ServerType repository read API") {

            should("get all ServerTypes") {

                underTest.serverTypes.all() shouldBe ServerTypeList(
                    Meta.of(
                        lastPage = 4,
                        nextPage = 4,
                        page = 3,
                        perPage = 25,
                        previousPage = 2,
                        totalEntries = 100,
                    ),
                    listOf(expectedServerType),
                )
            }

            should("get a ServerType by id") {
                underTest.serverTypes.find(id = serverTypeId) shouldBe ServerTypeItem(expectedServerType)
            }
        }
    })
