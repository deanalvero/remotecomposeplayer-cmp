package io.github.deanalvero.remotecomposeplayer.modifier

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.deanalvero.remotecomposeplayer.core.RemoteComposeContext
import io.github.deanalvero.remotecomposeplayer.operation.RcClickModifierOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcValueIntegerChangeActionOperation
import io.github.deanalvero.remotecomposeplayer.ui.RcNode

object ClickApplier : RcModifierApplier<RcClickModifierOperation> {
    @Composable
    override fun apply(
        operation: RcClickModifierOperation,
        currentModifier: Modifier,
        node: RcNode,
        scope: Any?,
        context: RemoteComposeContext
    ): Modifier {
        return currentModifier.clickable {
            operation.actions.forEach { action ->
                when (action) {
                    is RcValueIntegerChangeActionOperation -> {
                        context.updateIntegerVariable(action.targetValueId, action.value)
                    }
                }
            }
        }
    }
}