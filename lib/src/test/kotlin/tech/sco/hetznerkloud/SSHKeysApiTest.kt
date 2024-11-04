package tech.sco.hetznerkloud

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import tech.sco.hetznerkloud.model.Meta
import tech.sco.hetznerkloud.model.SSHKey
import tech.sco.hetznerkloud.request.CreateSSHKey
import tech.sco.hetznerkloud.request.UpdateSSHKey
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.Items
import java.time.OffsetDateTime

class SSHKeysApiTest :
    ShouldSpec({

        val sshKeyId = SSHKey.Id(42)
        val apiToken = ApiToken("foo")
        val mockEngine = createMockEngine(apiToken) { sshKeyId }
        val underTest = CloudApiClient.of(apiToken, mockEngine)

        val expectedSSHKey = SSHKey(
            id = sshKeyId,
            created = OffsetDateTime.parse("2016-01-30T23:55:00+00:00"),
            fingerprint = "b7:2f:30:a0:2f:6c:58:6c:21:04:58:61:ba:06:3b:2f",
            labels = mapOf(
                "environment" to "prod",
                "example.com/my" to "label",
                "just-a-key" to "",
            ),
            name = "my-resource",
            publicKey = "ssh-rsa AAAjjk76kgf...Xt",
        )

        context("SSH key resource read API") {

            should("get all ssh keys") {
                underTest.sshKeys.all() shouldBe Items(
                    meta = Meta.of(lastPage = 4, nextPage = 4, page = 3, perPage = 25, previousPage = 2, totalEntries = 100),
                    items = listOf(expectedSSHKey),
                )
            }

            should("get an ssh key by id") {
                underTest.sshKeys.find(sshKeyId) shouldBe Item(expectedSSHKey)
            }
        }

        context("SSH key resource write API") {

            should("create an ssh key") {

                val createRequest = CreateSSHKey(
                    labels = mapOf(
                        "environment" to "prod",
                        "example.com/my" to "label",
                        "just-a-key" to "",
                    ),
                    name = "My ssh key",
                    publicKey = "ssh-rsa AAAjjk76kgf...Xt",
                )

                underTest.sshKeys.create(createRequest) shouldBe Item(expectedSSHKey)
            }

            should("update an ssh key") {

                val updateRequest = UpdateSSHKey(
                    labels = mapOf(),
                    name = "My ssh key",
                )

                underTest.sshKeys.update(sshKeyId, updateRequest) shouldBe Item(
                    SSHKey(
                        id = SSHKey.Id(value = 2323),
                        created = OffsetDateTime.parse("2016-01-30T23:50Z"),
                        fingerprint = "b7:2f:30:a0:2f:6c:58:6c:21:04:58:61:ba:06:3b:2f",
                        labels = mapOf("labelkey" to "value"),
                        name = "My ssh key",
                        publicKey = "ssh-rsa AAAjjk76kgf...Xt",
                    ),
                )
            }

            should("delete an ssh key") {
                underTest.sshKeys.delete(sshKeyId) shouldBe Unit
            }
        }
    })
