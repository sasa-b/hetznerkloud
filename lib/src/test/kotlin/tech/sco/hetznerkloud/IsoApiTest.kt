package tech.sco.hetznerkloud

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import tech.sco.hetznerkloud.model.Iso
import tech.sco.hetznerkloud.model.Iso.Id
import tech.sco.hetznerkloud.model.Meta
import tech.sco.hetznerkloud.model.Server
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.Items
import java.time.OffsetDateTime

class IsoApiTest :
    ShouldSpec({
        val isoId = Id(42)
        val apiToken = ApiToken("foo")
        val mockEngine = createMockEngine(apiToken) { isoId }
        val underTest = CloudApiClient.of(apiToken, mockEngine)

        context("Iso resource read API") {

            val expectedIso = Iso(
                id = Id(42),
                architecture = "x86",
                deprecation =
                Server.Deprecation(
                    announced = OffsetDateTime.parse("2023-06-01T00:00:00+00:00"),
                    unavailableAfter = OffsetDateTime.parse("2023-09-01T00:00:00+00:00"),
                ),
                description = "FreeBSD 11.0 x64",
                name = "FreeBSD-11.0-RELEASE-amd64-dvd1",
                type = "public",
            )

            should("get all Isos") {
                underTest.isos.all() shouldBe
                    Items(
                        meta = Meta(pagination = Meta.Pagination(lastPage = 4, nextPage = 4, page = 3, perPage = 25, previousPage = 2, totalEntries = 100)),
                        items = listOf(expectedIso),
                    )
            }

            should("get Iso by id") {
                underTest.isos.find(isoId) shouldBe Item(expectedIso)
            }
        }
    })
