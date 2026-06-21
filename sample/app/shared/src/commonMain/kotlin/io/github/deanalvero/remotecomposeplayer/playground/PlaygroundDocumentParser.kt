package io.github.deanalvero.remotecomposeplayer.playground

import io.github.deanalvero.remotecomposeplayer.core.RcOperation
import io.github.deanalvero.remotecomposeplayer.core.RemoteComposeContext
import io.github.deanalvero.remotecomposeplayer.core.RemoteComposeEngine
import io.github.deanalvero.remotecomposeplayer.operation.RcBackgroundModifierOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcBoxLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcCanvasContentOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcCanvasLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcColumnLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcContainerEndOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcDrawCircleOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcDrawLineOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcLayoutContentOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcPaddingModifierOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcRootLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcRowLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcTextLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcWidthModifierOperation
import io.github.deanalvero.remotecomposeplayer.ui.RcNode
import io.github.deanalvero.remotecomposeplayer.ui.buildRcTree
import kotlin.math.roundToInt

object PlaygroundDocumentParser {
    fun parse(bytes: ByteArray): PlaygroundDocumentState {
        val operations = RemoteComposeEngine.parseStream(bytes)
        val rcRoot = buildRcTree(operations)
        val context = RemoteComposeContext(operations)

        var nextComponentId = 1

        val nodes = rcRoot.children.flatMap { node ->
            node.toPlaygroundNodes(
                context = context,
                allocateComponentId = { nextComponentId++ }
            )
        }

        return PlaygroundDocumentState(
            nodes = nodes,
            selectedId = null,
            nextComponentId = nextComponentId
        )
    }
}

private fun RcNode.toPlaygroundNodes(
    context: RemoteComposeContext,
    allocateComponentId: () -> Int
): List<PlaygroundNode> {
    return when (val op = operation) {
        is RcRootLayoutOperation,
        is RcLayoutContentOperation,
        is RcCanvasContentOperation -> {
            when (this) {
                is RcNode.Layout -> children.flatMap { it.toPlaygroundNodes(context, allocateComponentId) }
                is RcNode.Leaf -> emptyList()
            }
        }

        is RcCanvasLayoutOperation -> {
            val componentId = allocateComponentId()
            val drawOperations = (this as RcNode.Layout).collectDrawOperations()
            listOf(
                PlaygroundNode.Canvas(
                    id = "node-$componentId",
                    componentId = componentId,
                    modifiers = modifiers.toPlaygroundModifiers(context),
                    drawOperations = drawOperations
                )
            )
        }

        is RcColumnLayoutOperation -> {
            val componentId = allocateComponentId()
            val children = (this as RcNode.Layout).children.flatMap { it.toPlaygroundNodes(context, allocateComponentId) }

            listOf(
                PlaygroundNode.Column(
                    id = "node-$componentId",
                    componentId = componentId,
                    modifiers = modifiers.toPlaygroundModifiers(context),
                    horizontal = op.horizontalPositioning,
                    vertical = op.verticalPositioning,
                    spacedBy = op.spacedBy,
                    children = children
                )
            )
        }

        is RcRowLayoutOperation -> {
            val componentId = allocateComponentId()
            val children = (this as RcNode.Layout).children.flatMap { it.toPlaygroundNodes(context, allocateComponentId) }

            listOf(
                PlaygroundNode.Row(
                    id = "node-$componentId",
                    componentId = componentId,
                    modifiers = modifiers.toPlaygroundModifiers(context),
                    horizontal = op.horizontalPositioning,
                    vertical = op.verticalPositioning,
                    spacedBy = op.spacedBy,
                    children = children
                )
            )
        }

        is RcBoxLayoutOperation -> {
            val componentId = allocateComponentId()
            val children = (this as RcNode.Layout).children.flatMap { it.toPlaygroundNodes(context, allocateComponentId) }

            listOf(
                PlaygroundNode.Box(
                    id = "node-$componentId",
                    componentId = componentId,
                    modifiers = modifiers.toPlaygroundModifiers(context),
                    horizontal = op.horizontalPositioning,
                    vertical = op.verticalPositioning,
                    children = children
                )
            )
        }

        is RcTextLayoutOperation -> {
            val componentId = allocateComponentId()

            listOf(
                PlaygroundNode.Text(
                    id = "node-$componentId",
                    componentId = componentId,
                    modifiers = modifiers.toPlaygroundModifiers(context),
                    text = context.getStaticText(op.textId),
                    color = op.color,
                    fontSize = op.fontSize,
                    fontStyle = op.fontStyle,
                    fontWeight = op.fontWeight,
                    fontFamilyId = op.fontFamilyId,
                    textAlign = op.textAlign,
                    overflow = op.overflow,
                    maxLines = op.maxLines
                )
            )
        }

        is RcContainerEndOperation -> emptyList()

        else -> when (this) {
            is RcNode.Layout -> children.flatMap { it.toPlaygroundNodes(context, allocateComponentId) }
            is RcNode.Leaf -> emptyList()
        }
    }
}

private fun RcNode.Layout.collectDrawOperations(): List<PlaygroundDrawOperation> {
    return children.flatMap { child ->
        when (val op = child.operation) {
            is RcDrawCircleOperation -> listOf(
                PlaygroundDrawOperation.Circle(
                    centerX = op.centerX,
                    centerY = op.centerY,
                    radius = op.radius
                )
            )

            is RcDrawLineOperation -> listOf(
                PlaygroundDrawOperation.Line(
                    startX = op.startX,
                    startY = op.startY,
                    endX = op.endX,
                    endY = op.endY
                )
            )

            is RcLayoutContentOperation,
            is RcCanvasContentOperation -> {
                when (child) {
                    is RcNode.Layout -> child.collectDrawOperations()
                    is RcNode.Leaf -> emptyList()
                }
            }

            else -> {
                when (child) {
                    is RcNode.Layout -> child.collectDrawOperations()
                    is RcNode.Leaf -> emptyList()
                }
            }
        }
    }
}

private fun List<RcOperation>.toPlaygroundModifiers(
    context: RemoteComposeContext
): List<PlaygroundModifier> {
    return mapNotNull { op ->
        when (op) {
            is RcPaddingModifierOperation -> {
                PlaygroundModifier.Padding(
                    left = op.left,
                    top = op.top,
                    right = op.right,
                    bottom = op.bottom
                )
            }

            is RcWidthModifierOperation -> {
                PlaygroundModifier.Width(
                    typeId = op.typeId,
                    value = op.value
                )
            }

            is RcBackgroundModifierOperation -> {
                val argb = if (op.usesColorId) {
                    context.getColor(op.colorId)
                } else {
                    rgbaToArgb(op.r, op.g, op.b, op.a)
                }

                PlaygroundModifier.Background(
                    argb = argb,
                    shapeType = op.shapeType
                )
            }

            else -> null
        }
    }
}

private fun rgbaToArgb(r: Float, g: Float, b: Float, a: Float): Int {
    val alpha = channel(a)
    val red = channel(r)
    val green = channel(g)
    val blue = channel(b)
    return (alpha shl 24) or (red shl 16) or (green shl 8) or blue
}

private fun channel(value: Float): Int = (value * 255f).roundToInt().coerceIn(0, 255)
