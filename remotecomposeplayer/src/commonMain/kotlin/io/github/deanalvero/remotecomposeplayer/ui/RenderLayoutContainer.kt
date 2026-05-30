package io.github.deanalvero.remotecomposeplayer.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.deanalvero.remotecomposeplayer.core.RcOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcColumnLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcRowLayoutOperation

@Composable
fun RenderLayoutContainer(
    operation: RcOperation,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    when (operation) {
        is RcColumnLayoutOperation -> Column(modifier = modifier.padding(8.dp)) { content() }
        is RcRowLayoutOperation -> Row(modifier = modifier.padding(8.dp)) { content() }
        else -> Box(modifier = modifier) { content() }
    }
}
