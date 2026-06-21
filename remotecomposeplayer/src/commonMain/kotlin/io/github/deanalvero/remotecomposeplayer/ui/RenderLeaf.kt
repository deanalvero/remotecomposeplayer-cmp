package io.github.deanalvero.remotecomposeplayer.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.deanalvero.remotecomposeplayer.core.RcOperation
import io.github.deanalvero.remotecomposeplayer.core.RemoteComposeContext
import io.github.deanalvero.remotecomposeplayer.operation.RcTextLayoutOperation

@Composable
fun RenderLeaf(
    operation: RcOperation,
    context: RemoteComposeContext,
    modifier: Modifier = Modifier
) {
    when (operation) {
        is RcTextLayoutOperation -> RenderText(operation, context, modifier)
        else -> Unit
    }
}