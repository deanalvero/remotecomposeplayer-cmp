package io.github.deanalvero.remotecomposeplayer.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.deanalvero.remotecomposeplayer.core.RemoteComposeContext
import io.github.deanalvero.remotecomposeplayer.modifier.RcModifierRegistry

@Composable
fun RemoteComposeRenderer(
    nodes: List<RcNode>,
    context: RemoteComposeContext,
    modifier: Modifier = Modifier,
    scope: Any? = null
) {
    nodes.forEach { node ->
        val resolvedModifier = RcModifierRegistry.applyAll(
            operations = node.modifiers,
            initial = modifier,
            scope = scope,
            context = context
        )

        when (node) {
            is RcNode.Leaf -> RenderLeaf(
                operation = node.operation,
                context = context,
                modifier = resolvedModifier
            )
            is RcNode.Layout -> {
                RenderLayoutContainer(
                    node = node,
                    context = context,
                    modifier = resolvedModifier,
                    scope = scope
                )
            }
        }
    }
}