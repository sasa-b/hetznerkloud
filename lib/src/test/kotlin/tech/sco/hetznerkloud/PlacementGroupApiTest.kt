package tech.sco.hetznerkloud

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.encodeToString
import tech.sco.hetznerkloud.model.Meta
import tech.sco.hetznerkloud.model.PlacementGroup
import tech.sco.hetznerkloud.model.Server
import tech.sco.hetznerkloud.request.CreatePlacementGroup
import tech.sco.hetznerkloud.request.UpdateResource
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.Items
import java.time.OffsetDateTime

class PlacementGroupApiTest :
    ShouldSpec({
        val placementGroupId = PlacementGroup.Id(897)
        val apiToken = ApiToken("foo")
        val mockEngine = createMockEngine(apiToken) { mapOf("id" to placementGroupId.value.toString()) }
        val underTest = CloudApiClient.of(apiToken, mockEngine)

        context("Placement Group resource read API") {

            val expectedPlacementGroup = PlacementGroup(
                id = placementGroupId,
                created = OffsetDateTime.parse("2019-01-08T12:10:00+00:00"),
                labels = mapOf("key" to "value"),
                name = "my Placement Group",
                servers = listOf(
                    Server.Id(4711),
                    Server.Id(4712),
                ),
                type = PlacementGroup.Type.SPREAD,
            )

            should("get all Placement Groups") {

                underTest.placementGroups.all() shouldBe Items(
                    items = listOf(expectedPlacementGroup),
                    meta = Meta.of(lastPage = 1, nextPage = null, page = 1, perPage = 25, previousPage = null, totalEntries = 21),
                )
            }

            should("get Placement group by id") {
                underTest.placementGroups.find(placementGroupId) shouldBe Item(expectedPlacementGroup)
            }
        }

        context("Placement Group resource write API") {

            should("create a Placement Group") {

                val createRequest = CreatePlacementGroup(
                    "my Placement Group",
                    "spread",
                )

                jsonEncoder().encodeToString(createRequest) shouldBeEqualToRequest "create_a_placement_group.json"

                underTest.placementGroups.create(createRequest) shouldBe Item(
                    PlacementGroup(
                        id = placementGroupId,
                        created = OffsetDateTime.parse("2019-01-08T12:10:00+00:00"),
                        labels = mapOf("key" to "value"),
                        name = "my Placement Group",
                        servers = emptyList(),
                        type = PlacementGroup.Type.SPREAD,
                    ),
                )
            }

            should("update a Placement Group") {

                val updateRequest = UpdateResource(
                    "my Placement Group",
                    mapOf(
                        "environment" to "prod",
                        "example.com/my" to "label",
                        "just-a-key" to "",
                    ),
                )

                jsonEncoder().encodeToString(updateRequest) shouldBeEqualToRequest "update_a_placement_group.json"

                underTest.placementGroups.update(placementGroupId, updateRequest) shouldBe Item(
                    PlacementGroup(
                        id = placementGroupId,
                        created = OffsetDateTime.parse("2019-01-08T12:10:00+00:00"),
                        labels = mapOf("key" to "value"),
                        name = "my Placement Group",
                        servers = listOf(
                            Server.Id(value = 4711),
                            Server.Id(value = 4712),
                        ),
                        type = PlacementGroup.Type.SPREAD,
                    ),
                )
            }

            should("delete a Placement Group") {

                underTest.placementGroups.delete(placementGroupId) shouldBe Unit
            }
        }
    })
