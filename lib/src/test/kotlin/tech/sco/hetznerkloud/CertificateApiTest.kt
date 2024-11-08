package tech.sco.hetznerkloud

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.ActionFailedError
import tech.sco.hetznerkloud.model.Certificate
import tech.sco.hetznerkloud.model.Certificate.Id
import tech.sco.hetznerkloud.model.ManagedCertificate
import tech.sco.hetznerkloud.model.Meta
import tech.sco.hetznerkloud.model.Resource
import tech.sco.hetznerkloud.model.UploadedCertificate
import tech.sco.hetznerkloud.request.CreateManagedCertificate
import tech.sco.hetznerkloud.request.CreateUploadedCertificate
import tech.sco.hetznerkloud.request.UpdateResource
import tech.sco.hetznerkloud.response.Item
import tech.sco.hetznerkloud.response.ItemCreated
import tech.sco.hetznerkloud.response.Items
import java.time.OffsetDateTime

class CertificateApiTest :
    ShouldSpec({
        val certificateId = Id(897)
        val apiToken = ApiToken("foo")
        val mockEngine = createMockEngine(apiToken) { certificateId.value }
        val underTest = CloudApiClient.of(apiToken, mockEngine)

        context("Certificate resource read API") {

            should("get all certificates") {

                underTest.certificates.all() shouldBe Items(
                    meta = Meta.of(lastPage = 1, nextPage = null, page = 1, perPage = 25, previousPage = null, totalEntries = 21),
                    items = listOf(
                        UploadedCertificate(
                            id = Id(897),
                            certificate = "-----BEGIN CERTIFICATE-----\n...",
                            created = OffsetDateTime.parse("2019-01-08T12:10:00+00:00"),
                            domainNames = listOf(
                                "example.com",
                                "webmail.example.com",
                                "www.example.com",
                            ),
                            fingerprint = "03:c7:55:9b:2a:d1:04:17:09:f6:d0:7f:18:34:63:d4:3e:5f",
                            labels = mapOf(
                                "env" to "dev",
                            ),
                            name = "my website cert",
                            notValidAfter = OffsetDateTime.parse("2019-07-08T09:59:59+00:00"),
                            notValidBefore = OffsetDateTime.parse("2019-01-08T10:00:00+00:00"),
                            status = null,
                            usedBy = listOf(
                                Resource(id = 4711, type = "load_balancer"),
                            ),
                        ),
                        ManagedCertificate(
                            id = Id(898),
                            certificate = null,
                            created = OffsetDateTime.parse("2019-01-08T12:10:00+00:00"),
                            domainNames = listOf(
                                "example.com",
                                "webmail.example.com",
                                "www.example.com",
                            ),
                            fingerprint = null,
                            name = "my website cert",
                            notValidAfter = null,
                            notValidBefore = null,
                            status = Certificate.Status(
                                error = null,
                                issuance = Certificate.Status.Issuance.PENDING,
                                renewal = Certificate.Status.Renewal.UNAVAILABLE,
                            ),
                            usedBy = listOf(
                                Resource(id = 4711, type = "load_balancer"),
                            ),
                        ),
                    ),
                )
            }

            should("get a certificate by id") {
                underTest.certificates.find(certificateId) shouldBe Item(
                    UploadedCertificate(
                        id = Id(897),
                        certificate = "-----BEGIN CERTIFICATE-----\n...",
                        created = OffsetDateTime.parse("2019-01-08T12:10:00+00:00"),
                        domainNames = listOf(
                            "example.com",
                            "webmail.example.com",
                            "www.example.com",
                        ),
                        fingerprint = "03:c7:55:9b:2a:d1:04:17:09:f6:d0:7f:18:34:63:d4:3e:5f",
                        labels = mapOf(
                            "env" to "dev",
                        ),
                        name = "my website cert",
                        notValidAfter = OffsetDateTime.parse("2019-07-08T09:59:59+00:00"),
                        notValidBefore = OffsetDateTime.parse("2019-01-08T10:00:00+00:00"),
                        status = null,
                        usedBy = listOf(
                            Resource(id = 4711, type = "load_balancer"),
                        ),
                    ),
                )
            }
        }

        context("Certificate resource write API") {

            should("create a managed Certificate") {
                val createRequest = CreateManagedCertificate(
                    domainNames = listOf(
                        "example.com",
                        "webmail.example.com",
                        "www.example.com",
                    ),
                    name = "my website cert",
                )

                underTest.certificates.create(createRequest) shouldBe ItemCreated(
                    action = Action(
                        id = Action.Id(13),
                        command = "create_certificate",
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            Resource(id = 879, type = "certificate"),
                        ),
                        started = OffsetDateTime.parse("2019-01-08T12:10:00+00:00"),
                        status = "running",
                    ),
                    item = ManagedCertificate(
                        id = Id(897),
                        certificate = null,
                        created = OffsetDateTime.parse("2019-01-08T12:10:00+00:00"),
                        domainNames = listOf(
                            "example.com",
                            "webmail.example.com",
                            "www.example.com",
                        ),
                        fingerprint = null,
                        name = "my website cert",
                        notValidAfter = null,
                        notValidBefore = null,
                        status = Certificate.Status(
                            error = null,
                            issuance = Certificate.Status.Issuance.PENDING,
                            renewal = Certificate.Status.Renewal.UNAVAILABLE,
                        ),
                        usedBy = listOf(
                            Resource(id = 4711, type = "load_balancer"),
                        ),
                    ),
                )
            }

            should("create an uploaded Certificate") {
                val createRequest = CreateUploadedCertificate(
                    certificate = "-----BEGIN CERTIFICATE-----\n...",
                    name = "my website cert",
                    privateKey = "-----BEGIN PRIVATE KEY-----\n...",
                )

                underTest.certificates.create(createRequest) shouldBe ItemCreated(
                    action = Action(
                        id = Action.Id(13),
                        command = "create_certificate",
                        error = ActionFailedError(message = "Action failed"),
                        finished = null,
                        progress = 0,
                        resources = listOf(
                            Resource(id = 879, type = "certificate"),
                        ),
                        started = OffsetDateTime.parse("2019-01-08T12:10:00+00:00"),
                        status = "running",
                    ),
                    item = ManagedCertificate(
                        id = Id(897),
                        certificate = null,
                        created = OffsetDateTime.parse("2019-01-08T12:10:00+00:00"),
                        domainNames = listOf(
                            "example.com",
                            "webmail.example.com",
                            "www.example.com",
                        ),
                        fingerprint = null,
                        name = "my website cert",
                        notValidAfter = null,
                        notValidBefore = null,
                        status = Certificate.Status(
                            error = null,
                            issuance = Certificate.Status.Issuance.PENDING,
                            renewal = Certificate.Status.Renewal.UNAVAILABLE,
                        ),
                        usedBy = listOf(
                            Resource(id = 4711, type = "load_balancer"),
                        ),
                    ),
                )
            }

            should("update a Certificate") {
                val updateRequest = UpdateResource(
                    labels = mapOf(
                        "labelkey" to "value",
                    ),
                    name = "my website cert",
                )

                underTest.certificates.update(certificateId, updateRequest) shouldBe Item(
                    UploadedCertificate(
                        id = Id(897),
                        certificate = "-----BEGIN CERTIFICATE-----\n...",
                        created = OffsetDateTime.parse("2019-01-08T12:10:00+00:00"),
                        domainNames = listOf(
                            "example.com",
                            "webmail.example.com",
                            "www.example.com",
                        ),
                        fingerprint = "03:c7:55:9b:2a:d1:04:17:09:f6:d0:7f:18:34:63:d4:3e:5f",
                        labels = mapOf(
                            "labelkey" to "value",
                        ),
                        name = "my website cert",
                        notValidAfter = OffsetDateTime.parse("2019-07-08T09:59:59+00:00"),
                        notValidBefore = OffsetDateTime.parse("2019-01-08T10:00:00+00:00"),
                        status = null,
                        usedBy = listOf(
                            Resource(id = 4711, type = "load_balancer"),
                        ),
                    ),
                )
            }

            should("delete a Certificate") {
                underTest.certificates.delete(certificateId) shouldBe Unit
            }
        }
    })
