package tech.sco.hetznerkloud

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.ActionFailedError
import tech.sco.hetznerkloud.model.Location
import tech.sco.hetznerkloud.model.Meta
import tech.sco.hetznerkloud.model.NetworkZone
import tech.sco.hetznerkloud.model.Protection
import tech.sco.hetznerkloud.model.Resource
import tech.sco.hetznerkloud.model.Server
import tech.sco.hetznerkloud.model.Volume
import tech.sco.hetznerkloud.request.CreateVolume
import tech.sco.hetznerkloud.request.UpdateVolume
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.ItemCreated
import tech.sco.hetznerkloud.response.Items
import java.time.OffsetDateTime

class VolumeApiTest :
    ShouldSpec({
        val volumeId = Volume.Id(42)
        val apiToken = ApiToken("foo")
        val mockEngine = createMockEngine(apiToken) { volumeId }
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
                status = "available",
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
                status = "available",
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

                underTest.volumes.create(createRequest) shouldBe ItemCreated(
                    action = Action(
                        id = Action.Id(13),
                        command = "create_volume",
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            Resource(id = 42, type = "server"),
                            Resource(id = 554, type = "volume"),
                        ),
                        started = OffsetDateTime.parse("2016-01-30T23:50:00+00:00"),
                        status = "running",
                    ),
                    nextActions = listOf(
                        Action(
                            id = Action.Id(13),
                            command = "attach_volume",
                            error = ActionFailedError(message = "Action failed"),
                            finished = null,
                            progress = 0,
                            resources = listOf(
                                Resource(id = 42, type = "server"),
                                Resource(id = 554, type = "volume"),
                            ),
                            started = OffsetDateTime.parse("2016-01-30T23:50:00+00:00"),
                            status = "running",
                        ),
                    ),
                    item = expectedVolume,
                )
            }

            should("update a Volume") {

                val updateRequest = UpdateVolume(
                    labels = mapOf(
                        "labelkey" to "value",
                        "example.com/my" to "label",
                        "just-a-key" to "",
                    ),
                    name = "database-storage",
                )

                underTest.volumes.update(volumeId, updateRequest) shouldBe Item(expectedVolume.copy(labels = mapOf("labelkey" to "value")))
            }

            should("delete a Volume") {
                underTest.volumes.delete(volumeId) shouldBe Unit
            }
        }
    })
