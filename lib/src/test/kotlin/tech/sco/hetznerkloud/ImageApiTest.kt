package tech.sco.hetznerkloud

import io.kotest.common.runBlocking
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import tech.sco.hetznerkloud.model.Image
import tech.sco.hetznerkloud.model.Meta
import tech.sco.hetznerkloud.response.ImageList
import java.time.OffsetDateTime

class ImageApiTest :
    ShouldSpec({
        val apiToken = ApiToken("foo")
        val mockEngine = createMockEngine(apiToken)
        val underTest = CloudApiClient.of(apiToken, mockEngine)

        context("Image resource read API") {
            should("get all Image resources") {
                runBlocking {
                    underTest.images() shouldBe
                        ImageList(
                            meta = Meta(pagination = Meta.Pagination(lastPage = 4, nextPage = 4, page = 3, perPage = 25, previousPage = 2, totalEntries = 100)),
                            images =
                            listOf(
                                Image(
                                    id = 42,
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
                                ),
                            ),
                        )
                }
            }
        }

        context("Image resource write API") {
        }
    })
