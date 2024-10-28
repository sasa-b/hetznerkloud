package tech.sco.hetznerkloud.response

import kotlinx.serialization.Serializable
import tech.sco.hetznerkloud.model.Action
import tech.sco.hetznerkloud.model.Meta

@Serializable
data class ActionList(val meta: Meta, val actions: List<Action>)
