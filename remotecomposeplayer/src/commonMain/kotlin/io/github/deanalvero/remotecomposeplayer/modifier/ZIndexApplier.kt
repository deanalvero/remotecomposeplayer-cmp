package io.github.deanalvero.remotecomposeplayer.modifier

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import io.github.deanalvero.remotecomposeplayer.core.RemoteComposeContext
import io.github.deanalvero.remotecomposeplayer.operation.RcZIndexModifierOperation
import io.github.deanalvero.remotecomposeplayer.ui.RcNode

object ZIndexApplier : RcModifierApplier<RcZIndexModifierOperation> {
    @Composable
    override fun apply(
        operation: RcZIndexModifierOperation,
        currentModifier: Modifier,
        node: RcNode,
        scope: Any?,
        context: RemoteComposeContext
    ): Modifier {
        val resolvedZIndex: Float = context.resolveFloat(operation.zIndexValue)
        return currentModifier.zIndex(resolvedZIndex)
    }
}