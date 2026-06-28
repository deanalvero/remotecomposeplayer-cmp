package io.github.deanalvero.remotecomposeplayer.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInRoot
import io.github.deanalvero.remotecomposeplayer.core.RcOperation
import io.github.deanalvero.remotecomposeplayer.core.RemoteComposeContext
import io.github.deanalvero.remotecomposeplayer.modifier.RcModifierRegistry
import io.github.deanalvero.remotecomposeplayer.operation.RcBoxLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcCanvasContentOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcCanvasLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcColumnLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcComponentValueOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcLayoutContentOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcRootLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcRowLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcTextLayoutOperation

@Composable
fun RemoteComposeRenderer(
    nodes: List<RcNode>,
    context: RemoteComposeContext,
    modifier: Modifier = Modifier,
    scope: Any? = null
) {
    nodes.forEach { node ->
        var resolvedModifier = RcModifierRegistry.applyAll(
            operations = node.modifiers,
            initial = modifier,
            scope = scope,
            context = context
        )

        val componentId = node.operation.getComponentIdOrNull()
        val componentBindings = componentId?.let { context.componentValueBindings[it] }

        if (!componentBindings.isNullOrEmpty()) {
            resolvedModifier = resolvedModifier
                .onSizeChanged { size ->
                    componentBindings.forEach { binding ->
                        when (binding.type) {
                            RcComponentValueOperation.WIDTH ->
                                context.updateFloatVariable(binding.valueId, size.width.toFloat())
                            RcComponentValueOperation.HEIGHT ->
                                context.updateFloatVariable(binding.valueId, size.height.toFloat())
                        }
                    }
                }
                .onGloballyPositioned { coordinates ->
                    componentBindings.forEach { binding ->
                        when (binding.type) {
                            RcComponentValueOperation.POS_ROOT_X ->
                                context.updateFloatVariable(binding.valueId, coordinates.positionInRoot().x)
                            RcComponentValueOperation.POS_ROOT_Y ->
                                context.updateFloatVariable(binding.valueId, coordinates.positionInRoot().y)
                        }
                    }
                }
        }

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

private fun RcOperation.getComponentIdOrNull(): Int? {
    return when (this) {
        is RcRootLayoutOperation -> componentId
        is RcColumnLayoutOperation -> componentId
        is RcRowLayoutOperation -> componentId
        is RcCanvasLayoutOperation -> componentId
        is RcLayoutContentOperation -> componentId
        is RcCanvasContentOperation -> componentId
        is RcTextLayoutOperation -> componentId
        is RcBoxLayoutOperation -> componentId
        is RcComponentValueOperation -> componentId
        else -> null
    }
}