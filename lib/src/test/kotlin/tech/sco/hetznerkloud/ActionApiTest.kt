package tech.sco.hetznerkloud

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.Meta
import tech.sco.hetznerkloud.response.ActionItem
import tech.sco.hetznerkloud.response.ActionList
import java.time.OffsetDateTime

class ActionApiTest :
    ShouldSpec({

        context("Action repository read API") {
            val actionId = 42L
            val apiToken = ApiToken("foo")
            val mockEngine = createMockEngine(apiToken) { actionId }
            val underTest = CloudApiClient.of(apiToken, mockEngine)

            val expectedAction = Action(
                id = 42,
                command = "start_resource",
                error = Action.Error(
                    code = "action_failed",
                    message = "Action failed",
                ),
                finished = OffsetDateTime.parse("2016-01-30T23:55:00+00:00"),
                progress = 100,
                resources = listOf(Action.Resource(id = 42, type = "server")),
                started = OffsetDateTime.parse("2016-01-30T23:55:00+00:00"),
                status = "running",
            )

            should("get all Actions") {

                underTest.actions.all() shouldBe ActionList(
                    meta = Meta.of(
                        lastPage = 4,
                        nextPage = 4,
                        page = 3,
                        perPage = 25,
                        previousPage = 2,
                        totalEntries = 100,
                    ),
                    actions = listOf(expectedAction),
                )
            }

            should("get Action by id") {
                underTest.actions.find(actionId) shouldBe ActionItem(expectedAction)
            }
        }
    })
