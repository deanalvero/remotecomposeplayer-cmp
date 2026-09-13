package io.github.deanalvero.remotecomposeplayer.modifier

import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.deanalvero.remotecomposeplayer.core.RemoteComposeContext
import io.github.deanalvero.remotecomposeplayer.operation.RcOffsetModifierOperation
import io.github.deanalvero.remotecomposeplayer.ui.RcNode

object OffsetApplier : RcModifierApplier<RcOffsetModifierOperation> {
    @Composable
    override fun apply(
        operation: RcOffsetModifierOperation,
        currentModifier: Modifier,
        node: RcNode,
        scope: Any?,
        context: RemoteComposeContext
    ): Modifier {
        val resolvedX = context.resolveFloat(operation.x)
        val resolvedY = context.resolveFloat(operation.y)

        return currentModifier.offset(
            x = resolvedX.dp,
            y = resolvedY.dp
        )
    }
}