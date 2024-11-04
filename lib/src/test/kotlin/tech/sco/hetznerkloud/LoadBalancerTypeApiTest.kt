package tech.sco.hetznerkloud

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.should
import tech.sco.hetznerkloud.model.LoadBalancerType

class LoadBalancerTypeApiTest :
    ShouldSpec({
        context("Load balancer type read API") {

            val loadBalancerTypeId = LoadBalancerType.Id(42)
            val apiToken = ApiToken("foo")
            val mockEngine = createMockEngine(apiToken) { loadBalancerTypeId }
            val underTest = CloudApiClient.of(apiToken, mockEngine)

            should("get all load balancer types") {
            }

            should("get load balancer type by id") {
            }
        }
    })
