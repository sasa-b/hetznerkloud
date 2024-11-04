@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.response

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.model.LoadBalancer

@Serializable
data class LoadBalancerItem(
    @JsonNames("load_balancer")
    val loadBalancer: LoadBalancer,
)
