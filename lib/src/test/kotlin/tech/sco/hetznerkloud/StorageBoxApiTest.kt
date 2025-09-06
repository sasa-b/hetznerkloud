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

class StorageBoxApiTest :
    ShouldSpec({
        val storageBoxId = StorageBox.Id(42)
        val apiToken = ApiToken("foo")
        val mockEngine = createMockEngine(apiToken) { mapOf("id" to storageBoxId.value.toString()) }
        val underTest = ApiClient.of(apiToken, mockEngine)

        val expectedStorageBox = StorageBox(
            id = storageBoxId,
            username = "u12345",
            status = StorageBox.Status.ACTIVE,
            name = "string",
            storageBoxType = StorageBoxType(
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
            accessSettings = StorageBox.AccessSettings(
                reachableExternally = false,
                sambaEnabled = false,
                sshEnabled = false,
                webdavEnabled = false,
                zfsEnabled = false,
            ),
            server = "u1337.your-storagebox.de",
            system = "FSN1-BX355",
            stats = StorageBox.Stats(
                size = 0,
                sizeData = 0,
                sizeSnapshots = 0,
            ),
            labels = mapOf(
                "environment" to "prod",
                "example.com/my" to "label",
                "just-a-key" to "",
            ),
            protection = Protection(delete = false),
            snapshotPlan = StorageBox.SnapshotPlan(
                maxSnapshots = 0,
                minute = null,
                hour = null,
                dayOfWeek = null,
                dayOfMonth = null,
            ),
            created = OffsetDateTime.parse("2016-01-30T23:55:00+00:00"),
        )

        context("Storage Box resource read API") {
            should("get all Storage Boxes") {
                underTest.storageBoxes.all() shouldBe Items(
                    meta = Meta(pagination = Meta.Pagination(lastPage = 4, nextPage = 4, page = 3, perPage = 25, previousPage = 2, totalEntries = 100)),
                    items = listOf(expectedStorageBox),
                )
            }

            should("get a Storage Box by id") {
                underTest.storageBoxes.find(storageBoxId) shouldBe Item(expectedStorageBox)
            }
        }
    })
