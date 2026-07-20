package io.github.deanalvero.remotecomposeplayer.modifier

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.deanalvero.remotecomposeplayer.core.RcOperation
import io.github.deanalvero.remotecomposeplayer.core.RemoteComposeContext
import io.github.deanalvero.remotecomposeplayer.ui.RcNode

interface RcModifierApplier<T : RcOperation> {
    @Composable
    fun apply(
        operation: T,
        currentModifier: Modifier,
        node: RcNode,
        scope: Any?,
        context: RemoteComposeContext
    ): Modifier
}