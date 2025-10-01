package tech.sco.hetznerkloud

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.ActionFailedError
import tech.sco.hetznerkloud.model.Deprecation
import tech.sco.hetznerkloud.model.Image.Id
import tech.sco.hetznerkloud.model.ImageResource
import tech.sco.hetznerkloud.model.Location
import tech.sco.hetznerkloud.model.Meta
import tech.sco.hetznerkloud.model.NetworkZone
import tech.sco.hetznerkloud.model.Price
import tech.sco.hetznerkloud.model.Protection
import tech.sco.hetznerkloud.model.ResourceType
import tech.sco.hetznerkloud.model.Server
import tech.sco.hetznerkloud.model.ServerResource
import tech.sco.hetznerkloud.model.Snapshot
import tech.sco.hetznerkloud.model.StorageBox
import tech.sco.hetznerkloud.model.StorageBoxResource
import tech.sco.hetznerkloud.model.StorageBoxType
import tech.sco.hetznerkloud.model.Subaccount
import tech.sco.hetznerkloud.request.ChangeDeleteProtection
import tech.sco.hetznerkloud.request.ChangeStorageBoxType
import tech.sco.hetznerkloud.request.CreateStorageBox
import tech.sco.hetznerkloud.request.ResetStorageBoxPassword
import tech.sco.hetznerkloud.request.UpdateResource
import tech.sco.hetznerkloud.response.Folders
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.ItemCreated
import tech.sco.hetznerkloud.response.ItemDeleted
import tech.sco.hetznerkloud.response.Items
import java.time.OffsetDateTime

class StorageBoxApiTest :
    ShouldSpec({
        val storageBoxId = StorageBox.Id(42)
        val subaccountId = Subaccount.Id(42)
        val snapshotId = Snapshot.Id(42)
        val apiToken = ApiToken("foo")
        val mockEngine = createMockEngine(apiToken) {
            mapOf(
                "id" to storageBoxId.value.toString(),
                "action_id" to "42",
                "subaccount_id" to subaccountId.value.toString(),
                "snapshot_id" to snapshotId.value.toString(),
            )
        }
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

            should("get a Storage Box content") {
                underTest.storageBoxes.content(storageBoxId) shouldBe Folders(
                    value = listOf("offsite-backup", "photos"),
                )
            }

            should("get all Storage Box actions") {
                underTest.actions.all(ResourceType.STORAGE_BOX) shouldBe Items(
                    meta = Meta(pagination = Meta.Pagination(lastPage = 4, nextPage = 4, page = 3, perPage = 25, previousPage = 2, totalEntries = 100)),
                    items = listOf(
                        Action(
                            id = Action.Id(42),
                            command = "start_resource",
                            error = ActionFailedError(message = "Action failed"),
                            finished = OffsetDateTime.parse("2016-01-30T23:55Z"),
                            progress = 100,
                            resources = listOf(StorageBoxResource(id = StorageBox.Id(42))),
                            started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                            status = Action.Status.RUNNING,
                        ),
                    ),
                )
            }

            should("get Storage Box actions") {
                underTest.actions.all(resourceId = storageBoxId) shouldBe Items(
                    meta = Meta(pagination = Meta.Pagination(lastPage = 4, nextPage = 4, page = 3, perPage = 25, previousPage = 2, totalEntries = 100)),
                    items = listOf(
                        Action(
                            id = Action.Id(13),
                            command = "create",
                            error = ActionFailedError(message = "Action failed"),
                            finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                            progress = 100,
                            resources = listOf(StorageBoxResource(id = StorageBox.Id(42))),
                            started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                            status = Action.Status.SUCCESS,
                        ),
                    ),
                )
            }

            should("get a Storage Box action") {
                underTest.actions.find(ResourceType.STORAGE_BOX, actionId = Action.Id(42)) shouldBe Item(
                    Action(
                        id = Action.Id(42),
                        command = "start_resource",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        progress = 100,
                        resources = listOf(StorageBoxResource(id = StorageBox.Id(42))),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }

            should("get a Storage Box action for storage box") {
                underTest.actions.find(resourceId = storageBoxId, actionId = Action.Id(42)) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "create",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                        progress = 100,
                        resources = listOf(StorageBoxResource(id = StorageBox.Id(42))),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.SUCCESS,
                    ),
                )
            }

            should("get a Storage Box subaccounts") {
                underTest.storageBoxes.subaccounts(storageBoxId) shouldBe Subaccount.Items(
                    items = listOf(
                        Subaccount(
                            id = Subaccount.Id(42),
                            username = "u1337-sub1",
                            server = "u1337-sub1.your-storagebox.de",
                            homeDirectory = "my_backups/host01.my.company",
                            accessSettings = Subaccount.AccessSettings(
                                sambaEnabled = false,
                                sshEnabled = true,
                                webdavEnabled = false,
                                reachableExternally = true,
                                readonly = false,
                            ),
                            description = "host01 backup",
                            labels = mapOf(
                                "environment" to "prod",
                                "example.com/my" to "label",
                                "just-a-key" to "",
                            ),
                            created = OffsetDateTime.parse("2025-02-22T00:00:02.000Z"),
                            storageBox = StorageBox.Id(42),
                        ),
                    ),
                )
            }

            should("get a Storage Box subaccount") {
                underTest.storageBoxes.subaccount(storageBoxId, subaccountId) shouldBe Item(
                    Subaccount(
                        id = Subaccount.Id(42),
                        username = "u1337-sub1",
                        homeDirectory = "my_backups/host01.my.company",
                        server = "u1337-sub1.your-storagebox.de",
                        accessSettings = Subaccount.AccessSettings(
                            sambaEnabled = false,
                            sshEnabled = false,
                            webdavEnabled = false,
                            reachableExternally = false,
                            readonly = false,
                        ),
                        description = "host01 backup",
                        labels = mapOf(
                            "environment" to "prod",
                            "example.com/my" to "label",
                            "just-a-key" to "",
                        ),
                        created = OffsetDateTime.parse("2016-01-30T23:55:00+00:00"),
                        storageBox = StorageBox.Id(42),
                    ),
                )
            }

            should("get a Storage Box snapshots") {
                underTest.storageBoxes.snapshots(storageBoxId) shouldBe Snapshot.Items(
                    items = listOf(
                        Snapshot(
                            id = Snapshot.Id(1),
                            name = "2025-02-12T11-35-19",
                            description = "my-description",
                            stats = Snapshot.Stats(size = 2097152, sizeFilesystem = 1048576),
                            isAutomatic = false,
                            labels = mapOf(
                                "environment" to "prod",
                                "example.com/my" to "label",
                                "just-a-key" to "",
                            ),
                            created = OffsetDateTime.parse("2025-02-12T11:35:19.000Z"),
                            storageBox = StorageBox.Id(42),
                        ),
                        Snapshot(
                            id = Snapshot.Id(2),
                            name = "snapshot-daily--2025-02-12T22-00-02",
                            description = "",
                            stats = Snapshot.Stats(size = 2097152, sizeFilesystem = 1048576),
                            isAutomatic = true,
                            labels = mapOf(
                                "environment" to "prod",
                                "example.com/my" to "label",
                                "just-a-key" to "",
                            ),
                            created = OffsetDateTime.parse("2025-02-22T00:00:02.000Z"),
                            storageBox = StorageBox.Id(42),
                        ),
                    ),
                )
            }

            should("get a Storage Box snapshot") {
                underTest.storageBoxes.snapshot(storageBoxId, snapshotId) shouldBe Item(
                    Snapshot(
                        id = Snapshot.Id(42),
                        name = "my-resource",
                        description = "This describes my resource",
                        stats = Snapshot.Stats(size = 0, sizeFilesystem = 0),
                        isAutomatic = false,
                        labels = mapOf(
                            "environment" to "prod",
                            "example.com/my" to "label",
                            "just-a-key" to "",
                        ),
                        created = OffsetDateTime.parse("2016-01-30T23:55:00+00:00"),
                        storageBox = StorageBox.Id(42),
                    ),
                )
            }
        }

        context("Storage Box resource write API") {
            should("create a Storage Box") {

                val requestBody = CreateStorageBox(
                    storageBoxType = "bx20",
                    location = "fsn1",
                    name = "my-resource",
                    password = "string",
                    labels = mapOf(
                        "environment" to "prod",
                        "example.com/my" to "label",
                        "just-a-key" to "",
                    ),
                    sshKeys = listOf("ssh-rsa AAAjjk76kgf...Xt"),
                    accessSettings = CreateStorageBox.AccessSettings(
                        reachableExternally = false,
                        sambaEnabled = false,
                        sshEnabled = true,
                        webdavEnabled = false,
                        zfsEnabled = false,
                    ),
                )

                jsonEncoder().encodeToString(requestBody) shouldBeEqualToRequest "create_a_storage_box.json"

                underTest.storageBoxes.create(requestBody) shouldBe ItemCreated(
                    action = Action(
                        id = Action.Id(13),
                        command = "create",
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            StorageBoxResource(
                                id = StorageBox.Id(42),
                            ),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50:00Z"),
                        status = Action.Status.RUNNING,
                    ),
                    item = StorageBox(
                        id = StorageBox.Id(42),
                        status = StorageBox.Status.INITIALIZING,
                        name = "my-resource",
                        storageBoxType = StorageBoxType(
                            id = StorageBoxType.Id(1),
                            name = "bx20",
                            description = "BX20",
                            snapshotLimit = 10,
                            automaticSnapshotLimit = 10,
                            subaccountsLimit = 100,
                            size = 1073741824,
                            prices = listOf(
                                StorageBoxType.Price(
                                    location = "fsn1",
                                    priceHourly = Price.Amount(gross = "0.0061", net = "0.0051"),
                                    priceMonthly = Price.Amount(gross = "3.8080", net = "3.2000"),
                                    setupFee = Price.Amount(gross = "0.0000", net = "0.0000"),
                                ),
                            ),
                        ),
                        location = Location(
                            id = Location.Id(1),
                            city = "Falkenstein",
                            country = "DE",
                            description = "Falkenstein DC Park 1",
                            latitude = 50.476119,
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
                        server = null,
                        system = null,
                        stats = null,
                        labels = mapOf(
                            "environment" to "prod",
                            "example.com/my" to "label",
                            "just-a-key" to "",
                        ),
                        protection = Protection(delete = false),
                        snapshotPlan = null,
                        created = OffsetDateTime.parse("2016-01-30T23:50:00Z"),
                        username = null,
                    ),
                )
            }

            should("update a Storage Box") {

                val requestBody = UpdateResource(
                    labels = mapOf(
                        "environment" to "prod",
                        "example.com/my" to "label",
                        "just-a-key" to "",
                    ),
                    name = "string",
                )

                jsonEncoder().encodeToString(requestBody) shouldBeEqualToRequest "update_a_storage_box.json"

                underTest.storageBoxes.update(storageBoxId, requestBody) shouldBe Item(expectedStorageBox)
            }

            should("delete a Storage Box") {
                underTest.storageBoxes.delete(storageBoxId) shouldBe ItemDeleted(
                    Action(
                        id = Action.Id(13),
                        command = "delete",
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            StorageBoxResource(id = StorageBox.Id(42)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        status = Action.Status.SUCCESS,
                    ),
                )
            }

            should("change a Storage Box protection") {
                val requestBody = ChangeDeleteProtection(true)

                jsonEncoder().encodeToString(requestBody) shouldBeEqualToRequest "change_storage_box_protection.json"

                underTest.storageBoxes.changeProtection(
                    storageBoxId,
                    requestBody,
                ) shouldBe Item(
                    value = Action(
                        id = Action.Id(value = 13),
                        command = "change_protection",
                        error = null,
                        finished = null,
                        progress = 0,
                        resources = listOf(StorageBoxResource(id = StorageBox.Id(value = 42))),
                        started = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        status = Action.Status.SUCCESS,
                    ),
                )
            }

            should("change a Storage Box type") {
                val requestBody = ChangeStorageBoxType("BX21")

                jsonEncoder().encodeToString(requestBody) shouldBeEqualToRequest "change_storage_box_type.json"

                underTest.storageBoxes.changeType(
                    storageBoxId,
                    requestBody,
                ) shouldBe Item(
                    value = Action(
                        id = Action.Id(value = 13),
                        command = "change_type",
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(StorageBoxResource(id = StorageBox.Id(value = 42))),
                        started = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }

            should("reset a Storage Box password") {
                val requestBody = ResetStorageBoxPassword("12345")

                jsonEncoder().encodeToString(requestBody) shouldBeEqualToRequest "reset_storage_box_password.json"

                underTest.storageBoxes.resetPassword(
                    storageBoxId,
                    requestBody,
                ) shouldBe Item(
                    value = Action(
                        id = Action.Id(value = 13),
                        command = "reset_password",
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(StorageBoxResource(id = StorageBox.Id(value = 42))),
                        started = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }
        }
    })
