package io.github.deanalvero.remotecomposeplayer.modifier

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import io.github.deanalvero.remotecomposeplayer.core.RemoteComposeContext
import io.github.deanalvero.remotecomposeplayer.operation.RcColumnLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcRowLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcScrollModifierOperation
import io.github.deanalvero.remotecomposeplayer.ui.RcNode

object ScrollApplier : RcModifierApplier<RcScrollModifierOperation> {
    @Composable
    override fun apply(
        operation: RcScrollModifierOperation,
        currentModifier: Modifier,
        node: RcNode,
        scope: Any?,
        context: RemoteComposeContext
    ): Modifier {
        val scrollState = rememberScrollState()
        return when (operation.direction) {
            0 -> {
                if (node.operation is RcColumnLayoutOperation) {
                    currentModifier.verticalScroll(scrollState)
                } else {
                    currentModifier
                }
            }
            1 -> {
                if (node.operation is RcRowLayoutOperation) {
                    currentModifier.horizontalScroll(scrollState)
                } else {
                    currentModifier
                }
            }
            else -> currentModifier
        }
    }
}