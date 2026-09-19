package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcOperation

interface ActionModifierOperation : ModifierOperation {
    val actions: List<RcOperation>

    fun copyWithActions(actions: List<RcOperation>): ActionModifierOperation
}