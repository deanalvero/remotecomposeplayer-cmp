package io.github.deanalvero.remotecomposeplayer.modifier

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.deanalvero.remotecomposeplayer.core.RemoteComposeContext
import io.github.deanalvero.remotecomposeplayer.operation.RcClipRectModifierOperation
import io.github.deanalvero.remotecomposeplayer.ui.RcNode

object ClipRectApplier : RcModifierApplier<RcClipRectModifierOperation> {
    @Composable
    override fun apply(
        operation: RcClipRectModifierOperation,
        currentModifier: Modifier,
        node: RcNode,
        scope: Any?,
        context: RemoteComposeContext
    ): Modifier {
        return currentModifier
    }
}