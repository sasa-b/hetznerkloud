package tech.sco.hetznerkloud

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.ktor.http.HttpMethod
import tech.sco.hetznerkloud.model.Image
import tech.sco.hetznerkloud.model.Image.Id
import tech.sco.hetznerkloud.model.Meta
import tech.sco.hetznerkloud.request.UpdateImage
import tech.sco.hetznerkloud.response.ImageItem
import tech.sco.hetznerkloud.response.ImageList
import java.time.OffsetDateTime

class ImageApiTest :
    ShouldSpec({
        val imageId = Id(42)
        val updateImageId = Id(4711)
        val apiToken = ApiToken("foo")
        val mockEngine = createMockEngine(apiToken) { request ->
            when {
                request.method == HttpMethod.Patch -> updateImageId
                else -> imageId
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
                protection = Image.Protection(delete = false),
                rapidDeploy = false,
                status = "available",
                type = "snapshot",
            )

            should("get all Images") {
                underTest.images.all() shouldBe
                    ImageList(
                        meta = Meta(pagination = Meta.Pagination(lastPage = 4, nextPage = 4, page = 3, perPage = 25, previousPage = 2, totalEntries = 100)),
                        images =
                        listOf(expectedImage),
                    )
            }

            should("get Image by id") {
                underTest.images.find(id = imageId) shouldBe ImageItem(expectedImage)
            }
        }

        context("Image resource write API") {

            should("update an Image") {
                val updateRequest = UpdateImage(
                    description = "My new Image description",
                    type = "snapshot",
                    labels = mapOf("environment" to "prod", "example.com/my" to "label", "just-a-key" to ""),
                )

                underTest.images.update(updateImageId, updateRequest) shouldBe ImageItem(
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
                        protection = Image.Protection(delete = false),
                        rapidDeploy = false,
                        status = "available",
                        type = "snapshot",
                    ),
                )
            }

            should("delete an Image") {
                underTest.images.delete(imageId) shouldBe Unit
            }
        }
    })
