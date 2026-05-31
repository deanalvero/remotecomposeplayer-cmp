package io.github.deanalvero.remotecomposeplayer.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.deanalvero.remotecomposeplayer.core.RemoteComposeContext
import io.github.deanalvero.remotecomposeplayer.operation.RcColumnLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcLayoutContentOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcRootLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcRowLayoutOperation

@Composable
fun RenderLayoutContainer(
    node: RcNode.Layout,
    context: RemoteComposeContext,
    modifier: Modifier
) {
    when (val operation = node.operation) {
        is RcColumnLayoutOperation -> {
            Column(
                modifier = modifier,
                horizontalAlignment = mapColumnAlignment(operation.horizontalPositioning),
                verticalArrangement = mapColumnArrangement(operation.verticalPositioning, operation.spacedBy)
            ) {
                RemoteComposeRenderer(node.children, context, Modifier, this)
            }
        }
        is RcRowLayoutOperation -> {
            Row(
                modifier = modifier,
                horizontalArrangement = mapRowArrangement(operation.horizontalPositioning, operation.spacedBy),
                verticalAlignment = mapRowAlignment(operation.verticalPositioning)
            ) {
                RemoteComposeRenderer(node.children, context, Modifier, this)
            }
        }
        is RcRootLayoutOperation -> {
            Box(modifier = modifier.fillMaxSize()) {
                RemoteComposeRenderer(node.children, context, Modifier, this)
            }
        }
        is RcLayoutContentOperation -> {
            Box(modifier = modifier) {
                RemoteComposeRenderer(node.children, context, Modifier, this)
            }
        }
        else -> {
            Box(modifier = modifier) {
                RemoteComposeRenderer(node.children, context, Modifier, this)
            }
        }
    }
}
