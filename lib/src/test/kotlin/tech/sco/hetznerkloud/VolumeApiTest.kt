package tech.sco.hetznerkloud

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.encodeToString
import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.ActionFailedError
import tech.sco.hetznerkloud.model.Location
import tech.sco.hetznerkloud.model.Meta
import tech.sco.hetznerkloud.model.NetworkZone
import tech.sco.hetznerkloud.model.Protection
import tech.sco.hetznerkloud.model.ResourceType
import tech.sco.hetznerkloud.model.Server
import tech.sco.hetznerkloud.model.ServerResource
import tech.sco.hetznerkloud.model.Volume
import tech.sco.hetznerkloud.model.VolumeResource
import tech.sco.hetznerkloud.request.AttachToServer
import tech.sco.hetznerkloud.request.ChangeDeleteProtection
import tech.sco.hetznerkloud.request.CreateVolume
import tech.sco.hetznerkloud.request.ResizeVolume
import tech.sco.hetznerkloud.request.UpdateResource
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.ItemCreated
import tech.sco.hetznerkloud.response.Items
import java.time.OffsetDateTime

class VolumeApiTest :
    ShouldSpec({
        val volumeId = Volume.Id(42)
        val apiToken = ApiToken("foo")
        val mockEngine = createMockEngine(apiToken) {
            mapOf("id" to volumeId.value.toString(), "action_id" to "42")
        }
        val underTest = CloudApiClient.of(apiToken, mockEngine)

        context("Volume resource read API") {

            val expectedVolume = Volume(
                id = volumeId,
                created = OffsetDateTime.parse("2016-01-30T23:55:00+00:00"),
                format = "xfs",
                labels = mapOf(
                    "environment" to "prod",
                    "example.com/my" to "label",
                    "just-a-key" to "",
                ),
                linuxDevice = "/dev/disk/by-id/scsi-0HC_Volume_4711",
                location = Location(
                    id = Location.Id(42),
                    city = "Falkenstein",
                    country = "DE",
                    description = "Falkenstein DC Park 1",
                    longitude = 12.370071,
                    latitude = 50.47612,
                    name = "fsn1",
                    networkZone = NetworkZone.EU_CENTRAL,
                ),
                name = "my-resource",
                protection = Protection(delete = false),
                server = Server.Id(12),
                size = 42,
                status = Volume.Status.AVAILABLE,
            )

            should("get all Volumes") {

                underTest.volumes.all() shouldBe Items(
                    meta = Meta.of(lastPage = 4, nextPage = 4, page = 3, perPage = 25, previousPage = 2, totalEntries = 100),
                    items = listOf(expectedVolume),
                )
            }

            should("get Volume by id") {

                underTest.volumes.find(volumeId) shouldBe Item(expectedVolume)
            }

            should("get all Volume actions") {
                underTest.actions.all(ResourceType.VOLUME) shouldBe Items(
                    meta = Meta(pagination = Meta.Pagination(lastPage = 4, nextPage = 4, page = 3, perPage = 25, previousPage = 2, totalEntries = 100)),
                    items = listOf(
                        Action(
                            id = Action.Id(42),
                            command = "start_resource",
                            error = ActionFailedError(message = "Action failed"),
                            finished = OffsetDateTime.parse("2016-01-30T23:55Z"),
                            progress = 100,
                            resources = listOf(ServerResource(id = Server.Id(42))),
                            started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                            status = Action.Status.RUNNING,
                        ),
                    ),
                )
            }

            should("get Volume actions") {
                underTest.actions.all(resourceId = volumeId) shouldBe Items(
                    meta = Meta(pagination = Meta.Pagination(lastPage = 4, nextPage = 4, page = 3, perPage = 25, previousPage = 2, totalEntries = 100)),
                    items = listOf(
                        Action(
                            id = Action.Id(13),
                            command = "attach_volume",
                            error = ActionFailedError(message = "Action failed"),
                            finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                            progress = 100,
                            resources = listOf(
                                ServerResource(id = Server.Id(42)),
                                VolumeResource(id = Volume.Id(13)),
                            ),
                            started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                            status = Action.Status.SUCCESS,
                        ),
                    ),
                )
            }

            should("get a Volume action") {
                underTest.actions.find(ResourceType.VOLUME, actionId = Action.Id(42)) shouldBe Item(
                    Action(
                        id = Action.Id(42),
                        command = "start_resource",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        progress = 100,
                        resources = listOf(ServerResource(id = Server.Id(42))),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }

            should("get a Volume action for volume") {
                underTest.actions.find(resourceId = volumeId, actionId = Action.Id(42)) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "attach_volume",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                        progress = 100,
                        resources = listOf(ServerResource(id = Server.Id(42))),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.SUCCESS,
                    ),
                )
            }
        }

        context("Volume resource write API") {

            val expectedVolume = Volume(
                id = Volume.Id(4711),
                created = OffsetDateTime.parse("2016-01-30T23:50:11+00:00"),
                format = "xfs",
                labels = mapOf(
                    "env" to "dev",
                ),
                linuxDevice = "/dev/disk/by-id/scsi-0HC_Volume_4711",
                location = Location(
                    id = Location.Id(1),
                    city = "Falkenstein",
                    country = "DE",
                    description = "Falkenstein DC Park 1",
                    latitude = 50.47612,
                    longitude = 12.370071,
                    name = "fsn1",
                    networkZone = NetworkZone.EU_CENTRAL,
                ),
                name = "database-storage",
                protection = Protection(delete = false),
                server = Server.Id(12),
                size = 42,
                status = Volume.Status.AVAILABLE,
            )

            should("create a Volume") {
                val createRequest = CreateVolume(
                    automount = false,
                    format = "xfs",
                    labels = mapOf(
                        "labelkey" to "value",
                    ),
                    location = "nbg1",
                    name = "test-database",
                    size = 42,
                )

                jsonEncoder().encodeToString(createRequest) shouldBeEqualToRequest "create_a_volume.json"

                underTest.volumes.create(createRequest) shouldBe ItemCreated(
                    action = Action(
                        id = Action.Id(13),
                        command = "create_volume",
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            ServerResource(id = Server.Id(42)),
                            VolumeResource(id = Volume.Id(554)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50:00+00:00"),
                        status = Action.Status.RUNNING,
                    ),
                    nextActions = listOf(
                        Action(
                            id = Action.Id(13),
                            command = "attach_volume",
                            error = ActionFailedError(message = "Action failed"),
                            finished = null,
                            progress = 0,
                            resources = listOf(
                                ServerResource(id = Server.Id(42)),
                                VolumeResource(id = Volume.Id(554)),
                            ),
                            started = OffsetDateTime.parse("2016-01-30T23:50:00+00:00"),
                            status = Action.Status.RUNNING,
                        ),
                    ),
                    item = expectedVolume,
                )
            }

            should("update a Volume") {

                val updateRequest = UpdateResource(
                    labels = mapOf(
                        "environment" to "prod",
                        "example.com/my" to "label",
                        "just-a-key" to "",
                    ),
                    name = "database-storage",
                )

                jsonEncoder().encodeToString(updateRequest) shouldBeEqualToRequest "update_a_volume.json"

                underTest.volumes.update(volumeId, updateRequest) shouldBe Item(expectedVolume.copy(labels = mapOf("labelkey" to "value")))
            }

            should("delete a Volume") {
                underTest.volumes.delete(volumeId) shouldBe Unit
            }

            should("change Volume protection") {
                val changeProtectionRequest = ChangeDeleteProtection(true)

                jsonEncoder().encodeToString(changeProtectionRequest) shouldBeEqualToRequest "change_volume_protection.json"

                underTest.volumes.changeProtection(volumeId, changeProtectionRequest) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "change_protection",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                        progress = 100,
                        resources = listOf(
                            ServerResource(id = Server.Id(42)),
                            VolumeResource(id = Volume.Id(554)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.SUCCESS,
                    ),
                )
            }

            should("resize a Volume") {
                val resizeRequest = ResizeVolume(50)

                jsonEncoder().encodeToString(resizeRequest) shouldBeEqualToRequest "resize_a_volume.json"

                underTest.volumes.resize(volumeId, resizeRequest) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "resize_volume",
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            VolumeResource(id = Volume.Id(554)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50:00+00:00"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }

            should("attach a Volume to server") {
                val attachVolumeRequest = AttachToServer(false, Server.Id(43))

                jsonEncoder().encodeToString(attachVolumeRequest) shouldBeEqualToRequest "attach_a_volume_to_Server.json"

                underTest.volumes.attachToServer(volumeId, attachVolumeRequest) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "attach_volume",
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            ServerResource(id = Server.Id(43)),
                            VolumeResource(id = Volume.Id(554)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50:00+00:00"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }

            should("detach a Volume from server") {

                underTest.volumes.detachFromServer(volumeId) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "detach_volume",
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            ServerResource(id = Server.Id(42)),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50:00+00:00"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }
        }
    })
