package io.github.deanalvero.remotecomposeplayer.modifier

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.deanalvero.remotecomposeplayer.core.RemoteComposeContext
import io.github.deanalvero.remotecomposeplayer.operation.RcPaddingModifierOperation

object PaddingApplier : RcModifierApplier<RcPaddingModifierOperation> {
    override fun apply(
        operation: RcPaddingModifierOperation,
        currentModifier: Modifier,
        scope: Any?,
        context: RemoteComposeContext
    ): Modifier {
        return currentModifier.padding(
            start = operation.left.dp,
            top = operation.top.dp,
            end = operation.right.dp,
            bottom = operation.bottom.dp
        )
    }
}