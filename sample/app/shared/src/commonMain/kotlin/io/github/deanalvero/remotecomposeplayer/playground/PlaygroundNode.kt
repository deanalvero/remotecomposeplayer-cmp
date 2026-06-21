package io.github.deanalvero.remotecomposeplayer.playground

import io.github.deanalvero.remotecomposeplayer.operation.RcBoxLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcRowLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcTextLayoutOperation

sealed class PlaygroundNode {
    abstract val id: String
    abstract val componentId: Int
    abstract val modifiers: List<PlaygroundModifier>

    data class Column(
        override val id: String,
        override val componentId: Int,
        override val modifiers: List<PlaygroundModifier> = emptyList(),
        val horizontal: Int = RcRowLayoutOperation.START,
        val vertical: Int = RcRowLayoutOperation.TOP,
        val spacedBy: Float = 0f,
        val children: List<PlaygroundNode> = emptyList()
    ) : PlaygroundNode(), PlaygroundParentNode

    data class Row(
        override val id: String,
        override val componentId: Int,
        override val modifiers: List<PlaygroundModifier> = emptyList(),
        val horizontal: Int = RcRowLayoutOperation.START,
        val vertical: Int = RcRowLayoutOperation.TOP,
        val spacedBy: Float = 0f,
        val children: List<PlaygroundNode> = emptyList()
    ) : PlaygroundNode(), PlaygroundParentNode

    data class Box(
        override val id: String,
        override val componentId: Int,
        override val modifiers: List<PlaygroundModifier> = emptyList(),
        val horizontal: Int = RcBoxLayoutOperation.START,
        val vertical: Int = RcBoxLayoutOperation.TOP,
        val children: List<PlaygroundNode> = emptyList()
    ) : PlaygroundNode(), PlaygroundParentNode

    data class Canvas(
        override val id: String,
        override val componentId: Int,
        override val modifiers: List<PlaygroundModifier> = emptyList(),
        val drawOperations: List<PlaygroundDrawOperation> = emptyList()
    ) : PlaygroundNode()

    data class Text(
        override val id: String,
        override val componentId: Int,
        override val modifiers: List<PlaygroundModifier> = emptyList(),
        val text: String = "Hello, Remote Compose!",
        val color: Int = 0xFF000000.toInt(),
        val fontSize: Float = 16f,
        val fontStyle: Int = 0,
        val fontWeight: Float = 400f,
        val fontFamilyId: Int = 0,
        val textAlign: Int = RcTextLayoutOperation.TEXT_ALIGN_START,
        val overflow: Int = RcTextLayoutOperation.OVERFLOW_CLIP,
        val maxLines: Int = 1
    ) : PlaygroundNode()

    data class Spacer(
        override val id: String,
        override val componentId: Int,
        override val modifiers: List<PlaygroundModifier> = emptyList()
    ) : PlaygroundNode()
}
