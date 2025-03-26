package tech.sco.hetznerkloud

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.mpp.env
import kotlinx.coroutines.runBlocking
import kotlinx.io.files.Path
import org.junit.jupiter.api.Assertions.assertDoesNotThrow

class IntegrationTest : AnnotationSpec() {

    private val cloudApiClient = CloudApiClient.of(
        try {
            env("API_TEST_TOKEN")?.let { ApiToken(it) } ?: ApiToken.load(Path("src/test/resources/token.txt"))
        } catch (e: Throwable) {
            error("Missing API token, set API_TEST_TOKEN environment variable or provide it in to src/test/resources/token.txt file")
        },
    )

    @Test fun itGetsServers() {
        assertDoesNotThrow {
            runBlocking {
                cloudApiClient.servers.all().let {
                    println(it)
                    it.items.firstOrNull()?.let { item ->
                        cloudApiClient.servers.find(item.id)
                    }
                }
            }
        }
    }

    @Test fun itGetsImages() {
        assertDoesNotThrow {
            runBlocking {
                cloudApiClient.images.all().let {
                    println(it)
                    cloudApiClient.images.find(it.items.first().id)
                }
            }
        }
    }

    @Test fun itGetsLoadBalancers() {
        assertDoesNotThrow {
            runBlocking {
                cloudApiClient.loadBalancers.all().let {
                    println(it)
                    it.items.firstOrNull()?.let { item ->
                        cloudApiClient.loadBalancers.find(item.id)
                    }
                }
            }
        }
    }

    @Test fun itGetsDatacenters() {
        assertDoesNotThrow {
            runBlocking {
                cloudApiClient.datacenters.all().let {
                    println(it)
                    cloudApiClient.datacenters.find(it.items.first().id)
                }
            }
        }
    }

    @Test fun itGetsNetworks() {
        assertDoesNotThrow {
            runBlocking {
                cloudApiClient.networks.all().let {
                    println(it)
                    it.items.firstOrNull()?.let { item ->
                        cloudApiClient.networks.find(item.id)
                    }
                }
            }
        }
    }

    @Test fun itGetsVolumes() {
        assertDoesNotThrow {
            runBlocking {
                cloudApiClient.volumes.all().let {
                    println(it)
                    it.items.firstOrNull()?.let { item ->
                        cloudApiClient.volumes.find(item.id)
                    }
                }
            }
        }
    }

    @Test fun itGetsCertificates() {
        assertDoesNotThrow {
            runBlocking {
                cloudApiClient.certificates.all().let {
                    println(it)
                    it.items.firstOrNull()?.let { item ->
                        cloudApiClient.certificates.find(item.id)
                    }
                }
            }
        }
    }

    @Test fun itGetsPrimaryIps() {
        assertDoesNotThrow {
            runBlocking {
                cloudApiClient.primaryIps.all().let {
                    println(it)
                    it.items.firstOrNull()?.let { item ->
                        cloudApiClient.primaryIps.find(item.id)
                    }
                }
            }
        }
    }

    @Test fun itGetsPricing() {
        assertDoesNotThrow {
            runBlocking {
                cloudApiClient.pricing.all().let {
                    println(it)
                }
            }
        }
    }
}
