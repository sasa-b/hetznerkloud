@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.response

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.LoadBalancer

@Serializable
data class LoadBalancerCreated(
    val action: Action,
    @JsonNames("load_balancer")
    val loadBalancer: LoadBalancer,
)
