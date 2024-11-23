package tech.sco.hetznerkloud

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.Action.Id
import tech.sco.hetznerkloud.model.ActionFailedError
import tech.sco.hetznerkloud.model.Meta
import tech.sco.hetznerkloud.model.Resource
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.Items
import java.time.OffsetDateTime

class ActionApiTest :
    ShouldSpec({

        context("Action resource read API") {
            val actionId = Id(42)
            val apiToken = ApiToken("foo")
            val mockEngine = createMockEngine(apiToken) { mapOf("id" to actionId.value.toString()) }
            val underTest = CloudApiClient.of(apiToken, mockEngine)

            val expectedAction = Action(
                id = Id(42),
                command = "start_resource",
                error = ActionFailedError(
                    message = "Action failed",
                ),
                finished = OffsetDateTime.parse("2016-01-30T23:55:00+00:00"),
                progress = 100,
                resources = listOf(Resource(id = 42, type = "server")),
                started = OffsetDateTime.parse("2016-01-30T23:55:00+00:00"),
                status = Action.Status.RUNNING,
            )

            should("get all Actions") {

                underTest.actions.all() shouldBe Items(
                    meta = Meta.of(
                        lastPage = 4,
                        nextPage = 4,
                        page = 3,
                        perPage = 25,
                        previousPage = 2,
                        totalEntries = 100,
                    ),
                    items = listOf(expectedAction),
                )
            }

            should("get Action by id") {
                underTest.actions.find(actionId) shouldBe Item(expectedAction)
            }
        }
    })
