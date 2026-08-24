package io.github.deanalvero.remotecomposeplayer.modifier

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.deanalvero.remotecomposeplayer.core.RemoteComposeContext
import io.github.deanalvero.remotecomposeplayer.operation.RcRoundedClipRectModifierOperation
import io.github.deanalvero.remotecomposeplayer.ui.RcNode

object RoundedClipRectApplier : RcModifierApplier<RcRoundedClipRectModifierOperation> {
    @Composable
    override fun apply(
        operation: RcRoundedClipRectModifierOperation,
        currentModifier: Modifier,
        node: RcNode,
        scope: Any?,
        context: RemoteComposeContext
    ): Modifier {
        val topStart = context.resolveFloat(operation.topStart).coerceAtLeast(0f)
        val topEnd = context.resolveFloat(operation.topEnd).coerceAtLeast(0f)
        val bottomStart = context.resolveFloat(operation.bottomStart).coerceAtLeast(0f)
        val bottomEnd = context.resolveFloat(operation.bottomEnd).coerceAtLeast(0f)

        val shape = RoundedCornerShape(
            topStart = topStart.dp,
            topEnd = topEnd.dp,
            bottomEnd = bottomEnd.dp,
            bottomStart = bottomStart.dp
        )

        return currentModifier.clip(shape)
    }
}