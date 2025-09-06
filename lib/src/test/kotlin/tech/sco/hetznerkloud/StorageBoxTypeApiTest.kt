package tech.sco.hetznerkloud

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import tech.sco.hetznerkloud.model.Deprecation
import tech.sco.hetznerkloud.model.Location
import tech.sco.hetznerkloud.model.Meta
import tech.sco.hetznerkloud.model.NetworkZone
import tech.sco.hetznerkloud.model.Price
import tech.sco.hetznerkloud.model.Protection
import tech.sco.hetznerkloud.model.StorageBox
import tech.sco.hetznerkloud.model.StorageBoxType
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.Items
import java.time.OffsetDateTime

class StorageBoxTypeApiTest :
    ShouldSpec({
        val storageBoxTypeId = StorageBoxType.Id(42)
        val apiToken = ApiToken("foo")
        val mockEngine = createMockEngine(apiToken) { mapOf("id" to storageBoxTypeId.value.toString()) }
        val underTest = ApiClient.of(apiToken, mockEngine)

        val expectedStorageBoxType = StorageBoxType(
            id = StorageBoxType.Id(42),
            name = "bx11",
            description = "BX11",
            snapshotLimit = 10,
            automaticSnapshotLimit = 10,
            subaccountsLimit = 200,
            size = 1073741824,
            prices = listOf(
                StorageBoxType.Price(
                    location = "fsn1",
                    priceHourly = Price.Amount(net = "1.0000", gross = "1.1900"),
                    priceMonthly = Price.Amount(net = "1.0000", gross = "1.1900"),
                    setupFee = Price.Amount(net = "1.0000", gross = "1.1900"),
                ),
            ),
            deprecation = Deprecation(
                announced = OffsetDateTime.parse("2023-06-01T00:00:00+00:00"),
                unavailableAfter = OffsetDateTime.parse("2023-09-01T00:00:00+00:00"),
            ),
        )

        context("Storage Box Type resource read API") {
            should("get all Storage Box Types") {
                underTest.storageBoxTypes.all() shouldBe Items(
                    meta = Meta(pagination = Meta.Pagination(lastPage = 4, nextPage = 4, page = 3, perPage = 25, previousPage = 2, totalEntries = 100)),
                    items = listOf(expectedStorageBoxType),
                )
            }

            should("get a Storage Box Type by id") {
                underTest.storageBoxTypes.find(storageBoxTypeId) shouldBe Item(expectedStorageBoxType)
            }
        }
    })
