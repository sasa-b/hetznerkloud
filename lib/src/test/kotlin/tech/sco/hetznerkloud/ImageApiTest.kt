package tech.sco.hetznerkloud

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.ktor.http.HttpMethod
import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.ActionFailedError
import tech.sco.hetznerkloud.model.Image
import tech.sco.hetznerkloud.model.Image.Id
import tech.sco.hetznerkloud.model.Meta
import tech.sco.hetznerkloud.model.Protection
import tech.sco.hetznerkloud.model.Resource
import tech.sco.hetznerkloud.model.ResourceType
import tech.sco.hetznerkloud.request.UpdateImage
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.Items
import java.time.OffsetDateTime

class ImageApiTest :
    ShouldSpec({
        val imageId = Id(42)
        val updateImageId = Id(4711)
        val apiToken = ApiToken("foo")
        val mockEngine = createMockEngine(apiToken) { request ->
            when {
                request.method == HttpMethod.Patch -> mapOf("id" to updateImageId.value.toString())
                else -> mapOf("id" to imageId.value.toString(), "action_id" to "42")
            }
        }
        val underTest = CloudApiClient.of(apiToken, mockEngine)

        context("Image resource read API") {
            val expectedImage = Image(
                id = Id(42),
                architecture = "x86",
                boundTo = null,
                created = OffsetDateTime.parse("2016-01-30T23:55:00+00:00"),
                createdFrom = Image.CreatedFrom(id = 1, name = "Server"),
                deleted = null,
                deprecated = OffsetDateTime.parse("2018-02-28T00:00:00+00:00"),
                description = "Ubuntu 20.04 Standard 64 bit",
                diskSize = 10,
                imageSize = 2.3,
                labels = mapOf("environment" to "prod", "example.com/my" to "label", "just-a-key" to ""),
                name = "ubuntu-20.04",
                osFlavor = "ubuntu",
                osVersion = "20.04",
                protection = Protection(delete = false),
                rapidDeploy = false,
                status = Image.Status.AVAILABLE,
                type = Image.Type.SNAPSHOT,
            )

            should("get all Images") {
                underTest.images.all() shouldBe
                    Items(
                        meta = Meta(pagination = Meta.Pagination(lastPage = 4, nextPage = 4, page = 3, perPage = 25, previousPage = 2, totalEntries = 100)),
                        items = listOf(expectedImage),
                    )
            }

            should("get Image by id") {
                underTest.images.find(id = imageId) shouldBe Item(expectedImage)
            }

            should("get all Image actions") {
                underTest.actions.all(ResourceType.IMAGE) shouldBe Items(
                    meta = Meta(pagination = Meta.Pagination(lastPage = 4, nextPage = 4, page = 3, perPage = 25, previousPage = 2, totalEntries = 100)),
                    items = listOf(
                        Action(
                            id = Action.Id(42),
                            command = "start_resource",
                            error = ActionFailedError(message = "Action failed"),
                            finished = OffsetDateTime.parse("2016-01-30T23:55Z"),
                            progress = 100,
                            resources = listOf(Resource(id = 42, type = "server")),
                            started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                            status = Action.Status.RUNNING,
                        ),
                    ),
                )
            }

            should("get Image actions") {
                underTest.actions.all(resourceId = imageId) shouldBe Items(
                    meta = Meta(pagination = Meta.Pagination(lastPage = 4, nextPage = 4, page = 3, perPage = 25, previousPage = 2, totalEntries = 100)),
                    items = listOf(
                        Action(
                            id = Action.Id(13),
                            command = "change_protection",
                            error = ActionFailedError(message = "Action failed"),
                            finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                            progress = 100,
                            resources = listOf(Resource(id = 4711, type = "image")),
                            started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                            status = Action.Status.SUCCESS,
                        ),
                    ),
                )
            }

            should("get a Image action") {
                underTest.actions.find(ResourceType.IMAGE, actionId = Action.Id(42)) shouldBe Item(
                    Action(
                        id = Action.Id(42),
                        command = "start_resource",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        progress = 100,
                        resources = listOf(Resource(id = 42, type = "server")),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.RUNNING,
                    ),
                )
            }

            should("get a Image action for server") {
                underTest.actions.find(resourceId = imageId, actionId = Action.Id(42)) shouldBe Item(
                    Action(
                        id = Action.Id(13),
                        command = "change_protection",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2016-01-30T23:56Z"),
                        progress = 100,
                        resources = listOf(Resource(id = 4711, type = "image")),
                        started = OffsetDateTime.parse("2016-01-30T23:55Z"),
                        status = Action.Status.SUCCESS,
                    ),
                )
            }
        }

        context("Image resource write API") {

            should("update an Image") {
                val updateRequest = UpdateImage(
                    description = "My new Image description",
                    type = "snapshot",
                    labels = mapOf("environment" to "prod", "example.com/my" to "label", "just-a-key" to ""),
                )

                underTest.images.update(updateImageId, updateRequest) shouldBe Item(
                    Image(
                        id = Id(4711),
                        architecture = "x86",
                        boundTo = null,
                        created = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        createdFrom = Image.CreatedFrom(id = 1, name = "Server"),
                        deleted = null,
                        deprecated = OffsetDateTime.parse("2018-02-28T00:00Z"),
                        description = "My new Image description",
                        diskSize = 10,
                        imageSize = 2.3,
                        labels = mapOf("labelkey" to "value"),
                        name = null,
                        osFlavor = "ubuntu",
                        osVersion = "20.04",
                        protection = Protection(delete = false),
                        rapidDeploy = false,
                        status = Image.Status.AVAILABLE,
                        type = Image.Type.SNAPSHOT,
                    ),
                )
            }

            should("delete an Image") {
                underTest.images.delete(imageId) shouldBe Unit
            }
        }
    })
