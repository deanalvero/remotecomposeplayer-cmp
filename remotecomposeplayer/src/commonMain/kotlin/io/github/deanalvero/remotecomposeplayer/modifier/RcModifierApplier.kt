package io.github.deanalvero.remotecomposeplayer.modifier

import androidx.compose.ui.Modifier
import io.github.deanalvero.remotecomposeplayer.core.RcOperation
import io.github.deanalvero.remotecomposeplayer.core.RemoteComposeContext

interface RcModifierApplier<T : RcOperation> {
    fun apply(
        operation: T,
        currentModifier: Modifier,
        scope: Any?,
        context: RemoteComposeContext
    ): Modifier
}