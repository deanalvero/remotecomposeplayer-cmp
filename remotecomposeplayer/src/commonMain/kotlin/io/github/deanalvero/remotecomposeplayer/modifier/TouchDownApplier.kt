package io.github.deanalvero.remotecomposeplayer.modifier

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import io.github.deanalvero.remotecomposeplayer.core.RemoteComposeContext
import io.github.deanalvero.remotecomposeplayer.core.executeActions
import io.github.deanalvero.remotecomposeplayer.operation.RcTouchDownModifierOperation
import io.github.deanalvero.remotecomposeplayer.ui.RcNode

object TouchDownApplier : RcModifierApplier<RcTouchDownModifierOperation> {
    @Composable
    override fun apply(
        operation: RcTouchDownModifierOperation,
        currentModifier: Modifier,
        node: RcNode,
        scope: Any?,
        context: RemoteComposeContext
    ): Modifier {
        return currentModifier.pointerInput(operation) {
            awaitEachGesture {
                awaitFirstDown(requireUnconsumed = false)
                context.executeActions(operation.actions)
            }
        }
    }
}