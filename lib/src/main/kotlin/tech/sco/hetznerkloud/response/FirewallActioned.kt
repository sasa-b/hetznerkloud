package tech.sco.hetznerkloud.response

import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.model.Action

@Serializable
data class FirewallActioned(val actions: List<Action>)
