package io.github.deanalvero.remotecomposeplayer.modifier

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import io.github.deanalvero.remotecomposeplayer.core.RemoteComposeContext
import io.github.deanalvero.remotecomposeplayer.core.executeActions
import io.github.deanalvero.remotecomposeplayer.operation.RcTouchUpModifierOperation
import io.github.deanalvero.remotecomposeplayer.ui.RcNode

object TouchUpApplier : RcModifierApplier<RcTouchUpModifierOperation> {
    @Composable
    override fun apply(
        operation: RcTouchUpModifierOperation,
        currentModifier: Modifier,
        node: RcNode,
        scope: Any?,
        context: RemoteComposeContext
    ): Modifier {
        return currentModifier.pointerInput(operation) {
            awaitEachGesture {
                awaitFirstDown(requireUnconsumed = false)
                val upEvent = waitForUpOrCancellation()
                if (upEvent != null) {
                    context.executeActions(operation.actions)
                }
            }
        }
    }
}