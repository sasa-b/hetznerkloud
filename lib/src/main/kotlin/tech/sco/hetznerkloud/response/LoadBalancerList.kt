@file:OptIn(ExperimentalSerializationApi::class)

package tech.sco.hetznerkloud.response

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import tech.sco.hetznerkloud.model.LoadBalancer
import tech.sco.hetznerkloud.model.Meta

@Serializable
data class LoadBalancerList(
    val meta: Meta,
    @JsonNames("load_balancers")
    val loadBalancers: List<LoadBalancer>,
)
