package io.github.deanalvero.remotecomposeplayer.playground

import io.github.deanalvero.remotecomposeplayer.core.RcOperation
import io.github.deanalvero.remotecomposeplayer.core.RemoteComposeContext
import io.github.deanalvero.remotecomposeplayer.core.RemoteComposeEngine
import io.github.deanalvero.remotecomposeplayer.operation.*
import io.github.deanalvero.remotecomposeplayer.ui.RcNode
import io.github.deanalvero.remotecomposeplayer.ui.buildRcTree

object PlaygroundDocumentParser {
    fun parse(bytes: ByteArray): PlaygroundDocumentState {
        val operations = RemoteComposeEngine.parseStream(bytes)
        val rcRoot = buildRcTree(operations)
        val context = RemoteComposeContext(operations)

        var maxComponentId = 0

        fun mapModifiers(rcModifiers: List<RcOperation>): List<PlaygroundModifier> {
            return rcModifiers.mapNotNull { op ->
                when (op) {
                    is RcPaddingModifierOperation -> PlaygroundModifier.Padding(op.left, op.top, op.right, op.bottom)
                    is RcWidthModifierOperation -> PlaygroundModifier.Width(op.typeId, op.value)
                    is RcBackgroundModifierOperation -> {
                        val alpha = (op.a * 255).toInt() and 0xFF
                        val red = (op.r * 255).toInt() and 0xFF
                        val green = (op.g * 255).toInt() and 0xFF
                        val blue = (op.b * 255).toInt() and 0xFF
                        val argb = (alpha shl 24) or (red shl 16) or (green shl 8) or blue
                        PlaygroundModifier.Background(argb, op.shapeType)
                    }
                    else -> null
                }
            }
        }

        fun mapNode(rcNode: RcNode): PlaygroundNode? {
            val modifiers = mapModifiers(rcNode.modifiers)

            return when (val op = rcNode.operation) {
                is RcColumnLayoutOperation -> {
                    maxComponentId = maxOf(maxComponentId, op.componentId)
                    PlaygroundNode.Column(
                        id = "node-${op.componentId}",
                        componentId = op.componentId,
                        modifiers = modifiers,
                        horizontal = op.horizontalPositioning,
                        vertical = op.verticalPositioning,
                        spacedBy = op.spacedBy,
                        children = (rcNode as? RcNode.Layout)?.children?.mapNotNull { mapNode(it) } ?: emptyList()
                    )
                }
                is RcRowLayoutOperation -> {
                    maxComponentId = maxOf(maxComponentId, op.componentId)
                    PlaygroundNode.Row(
                        id = "node-${op.componentId}",
                        componentId = op.componentId,
                        modifiers = modifiers,
                        horizontal = op.horizontalPositioning,
                        vertical = op.verticalPositioning,
                        spacedBy = op.spacedBy,
                        children = (rcNode as? RcNode.Layout)?.children?.mapNotNull { mapNode(it) } ?: emptyList()
                    )
                }
                is RcTextLayoutOperation -> {
                    maxComponentId = maxOf(maxComponentId, op.componentId)
                    PlaygroundNode.Text(
                        id = "node-${op.componentId}",
                        componentId = op.componentId,
                        modifiers = modifiers,
                        text = context.getText(op.textId),
                        color = op.color,
                        fontSize = op.fontSize,
                        fontStyle = op.fontStyle,
                        fontWeight = op.fontWeight,
                        fontFamilyId = op.fontFamilyId,
                        textAlign = op.textAlign,
                        overflow = op.overflow,
                        maxLines = op.maxLines
                    )
                }
                else -> null
            }
        }

        val playgroundNodes = rcRoot.children.mapNotNull { mapNode(it) }

        return PlaygroundDocumentState(
            nodes = playgroundNodes,
            selectedId = null,
            nextComponentId = maxComponentId + 1
        )
    }
}