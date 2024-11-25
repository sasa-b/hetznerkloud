package tech.sco.hetznerkloud

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.ktor.http.toURI
import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.ActionFailedError
import tech.sco.hetznerkloud.model.Certificate
import tech.sco.hetznerkloud.model.Certificate.Id
import tech.sco.hetznerkloud.model.CertificateResource
import tech.sco.hetznerkloud.model.LoadBalancer
import tech.sco.hetznerkloud.model.LoadBalancerResource
import tech.sco.hetznerkloud.model.ManagedCertificate
import tech.sco.hetznerkloud.model.Meta
import tech.sco.hetznerkloud.model.ResourceType
import tech.sco.hetznerkloud.model.Server
import tech.sco.hetznerkloud.model.ServerResource
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
        val mockEngine = createMockEngine(apiToken) {
            when {
                Route.GET_CERTIFICATE_ACTION.path.toRegex().matches(it.url.toURI().pathWithoutVersion) -> mapOf("id" to "42")
                else -> mapOf("id" to certificateId.value.toString(), "action_id" to "42")
            }
        }
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
                                LoadBalancerResource(id = LoadBalancer.Id(4711)),
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
                                LoadBalancerResource(id = LoadBalancer.Id(4711)),
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
                            LoadBalancerResource(id = LoadBalancer.Id(4711)),
                        ),
                    ),
                )
            }

            should("get all Certificate actions") {
                underTest.actions.all(ResourceType.CERTIFICATE) shouldBe Items(
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

            should("get Certificate actions") {
                underTest.actions.all(resourceId = certificateId) shouldBe Items(
                    meta = Meta(pagination = Meta.Pagination(lastPage = 4, nextPage = 4, page = 3, perPage = 25, previousPage = 2, totalEntries = 100)),
                    items = listOf(
                        Action(
                            id = Action.Id(14),
                            command = "issue_certificate",
                            error = ActionFailedError(message = "Action failed"),
                            finished = OffsetDateTime.parse("2021-01-30T23:57Z"),
                            progress = 100,
                            resources = listOf(CertificateResource(id = Id(896))),
                            started = OffsetDateTime.parse("2021-01-30T23:55Z"),
                            status = Action.Status.SUCCESS,
                        ),
                    ),
                )
            }

            should("get a Certificate action") {
                underTest.actions.find(ResourceType.CERTIFICATE, actionId = Action.Id(42)) shouldBe Item(
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

            should("get a Certificate action for server") {
                underTest.actions.find(resourceId = certificateId, actionId = Action.Id(42)) shouldBe Item(
                    Action(
                        id = Action.Id(14),
                        command = "issue_certificate",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2021-01-30T23:57Z"),
                        progress = 100,
                        resources = listOf(CertificateResource(id = Id(896))),
                        started = OffsetDateTime.parse("2021-01-30T23:55Z"),
                        status = Action.Status.SUCCESS,
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
                            CertificateResource(id = Id(879)),
                        ),
                        started = OffsetDateTime.parse("2019-01-08T12:10:00+00:00"),
                        status = Action.Status.RUNNING,
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
                            LoadBalancerResource(id = LoadBalancer.Id(4711)),
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
                            CertificateResource(id = Id(879)),
                        ),
                        started = OffsetDateTime.parse("2019-01-08T12:10:00+00:00"),
                        status = Action.Status.RUNNING,
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
                            LoadBalancerResource(id = LoadBalancer.Id(4711)),
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
                            LoadBalancerResource(id = LoadBalancer.Id(4711)),
                        ),
                    ),
                )
            }

            should("delete a Certificate") {
                underTest.certificates.delete(certificateId) shouldBe Unit
            }

            should("retry Certificate issuance or renewal") {
                underTest.certificates.retryIssuanceOrRenewal(certificateId) shouldBe Item(
                    Action(
                        id = Action.Id(14),
                        command = "issue_certificate",
                        error = ActionFailedError(message = "Action failed"),
                        finished = OffsetDateTime.parse("2021-01-30T23:57Z"),
                        progress = 100,
                        resources = listOf(CertificateResource(id = Id(896))),
                        started = OffsetDateTime.parse("2021-01-30T23:55Z"),
                        status = Action.Status.SUCCESS,
                    ),
                )
            }
        }
    })
