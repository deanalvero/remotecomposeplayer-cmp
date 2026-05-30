package io.github.deanalvero.remotecomposeplayer.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.deanalvero.remotecomposeplayer.core.RemoteComposeContext

@Composable
fun RemoteComposeRenderer(
    nodes: List<RcNode>,
    context: RemoteComposeContext,
    modifier: Modifier = Modifier
) {
    nodes.forEach { node ->
        when (node) {
            is RcNode.Leaf -> RenderLeaf(
                operation = node.operation,
                context = context,
                modifier = modifier
            )
            is RcNode.Layout -> {
                RenderLayoutContainer(
                    operation = node.operation,
                    modifier = modifier
                ) {
                    RemoteComposeRenderer(
                        nodes = node.children,
                        context = context
                    )
                }
            }
        }
    }
}