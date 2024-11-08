package tech.sco.hetznerkloud

import io.kotest.core.spec.style.AnnotationSpec
import kotlinx.coroutines.runBlocking
import kotlinx.io.files.Path
import org.junit.jupiter.api.Assertions.assertDoesNotThrow

class IntegrationTest : AnnotationSpec() {

    private val cloudApiClient = CloudApiClient.of(
        ApiToken.load(Path("src/test/resources/token.txt")),
    )

    @Test
    fun itGetsServers() {
        assertDoesNotThrow {
            runBlocking {
                cloudApiClient.servers.all().let {
                    println(it)
                    cloudApiClient.servers.find(it.items.first().id)
                }
            }
        }
    }

    @Test
    fun itGetsImages() {
        assertDoesNotThrow {
            runBlocking {
                cloudApiClient.images.all().let {
                    println(it)
                    cloudApiClient.images.find(it.items.first().id)
                }
            }
        }
    }

    @Test
    fun itGetsLoadBalancers() {
        assertDoesNotThrow {
            runBlocking {
                cloudApiClient.loadBalancers.all().let {
                    println(it)
                    cloudApiClient.loadBalancers.find(it.items.first().id)
                }
            }
        }
    }

    @Test
    fun itGetsDatacenters() {
        assertDoesNotThrow {
            runBlocking {
                cloudApiClient.datacenters.all().let {
                    println(it)
                    cloudApiClient.datacenters.find(it.items.first().id)
                }
            }
        }
    }

    @Test
    fun itGetsNetworks() {
        assertDoesNotThrow {
            runBlocking {
                cloudApiClient.networks.all().let {
                    println(it)
                    cloudApiClient.networks.find(it.items.first().id)
                }
            }
        }
    }

    @Test
    fun itGetsVolumes() {
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

    @Test
    fun itGetsCertificates() {
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
}
