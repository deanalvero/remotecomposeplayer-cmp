package io.github.deanalvero.remotecomposeplayer.core

import io.github.deanalvero.remotecomposeplayer.operation.RcValueFloatChangeActionOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcValueIntegerChangeActionOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcValueIntegerExpressionChangeActionOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcValueStringChangeActionOperation

fun RemoteComposeContext.executeActions(actions: List<RcOperation>) {
    actions.forEach { action ->
        when (action) {
            is RcValueIntegerChangeActionOperation -> {
                updateIntegerVariable(action.targetValueId, action.value)
            }
            is RcValueIntegerExpressionChangeActionOperation -> {
                val targetId = action.targetValueId.toInt()
                val exprId = action.valueExpressionId.toInt()
                val result = evaluateIntegerExpression(exprId)
                updateIntegerVariable(targetId, result)
            }
            is RcValueStringChangeActionOperation -> {
                overrideText(action.targetValueId, action.valueId)
            }
            is RcValueFloatChangeActionOperation -> {
                updateFloatVariable(action.targetValueId, action.value)
            }
        }
    }
}