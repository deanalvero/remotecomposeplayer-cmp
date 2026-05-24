package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcTextLayoutOperation(
    override val opCode: Int = OP_CODE,
    val componentId: Int,
    val animationId: Int,
    val textId: Int,
    val color: Int,
    val fontSize: Float,
    val fontStyle: Int,
    val fontWeight: Float,
    val fontFamilyId: Int,
    val textAlign: Int,
    val overflow: Int,
    val maxLines: Int
) : RcOperation {

    override val name: String = "TextLayout"

    override fun toString(): String {
        val hexColor = color.toUInt().toString(16).padStart(8, '0').uppercase()

        val alignStr = getTextAlignString(textAlign and 0xFFFF)
        val styleStr = if (fontStyle == 1) "Italic" else "Normal"
        val overflowStr = getOverflowString(overflow)

        return "$name [ID: $componentId] -> TextRef: [$textId] | Size: ${fontSize}px | Weight: ${fontWeight.toInt()} | Style: $styleStr | Align: $alignStr | Overflow: $overflowStr | Color: #$hexColor"
    }

    private fun getTextAlignString(align: Int): String = when (align) {
        TEXT_ALIGN_LEFT -> "Left"
        TEXT_ALIGN_RIGHT -> "Right"
        TEXT_ALIGN_CENTER -> "Center"
        TEXT_ALIGN_JUSTIFY -> "Justify"
        TEXT_ALIGN_START -> "Start"
        TEXT_ALIGN_END -> "End"
        else -> "Default($align)"
    }

    private fun getOverflowString(of: Int): String = when (of) {
        OVERFLOW_CLIP -> "Clip"
        OVERFLOW_VISIBLE -> "Visible"
        OVERFLOW_ELLIPSIS -> "Ellipsis"
        OVERFLOW_START_ELLIPSIS -> "StartEllipsis"
        OVERFLOW_MIDDLE_ELLIPSIS -> "MiddleEllipsis"
        else -> "Default($of)"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 208
        override val opCode: Int = OP_CODE

        const val TEXT_ALIGN_LEFT = 1
        const val TEXT_ALIGN_RIGHT = 2
        const val TEXT_ALIGN_CENTER = 3
        const val TEXT_ALIGN_JUSTIFY = 4
        const val TEXT_ALIGN_START = 5
        const val TEXT_ALIGN_END = 6

        const val OVERFLOW_CLIP = 1
        const val OVERFLOW_VISIBLE = 2
        const val OVERFLOW_ELLIPSIS = 3
        const val OVERFLOW_START_ELLIPSIS = 4
        const val OVERFLOW_MIDDLE_ELLIPSIS = 5

        override fun decode(reader: RcBufferReader): RcOperation {
            val componentId = reader.readInt()
            val animationId = reader.readInt()
            val textId = reader.readInt()
            val color = reader.readInt()
            val fontSize = reader.readFloat()
            val fontStyle = reader.readInt()
            val fontWeight = reader.readFloat()
            val fontFamilyId = reader.readInt()
            val textAlign = reader.readInt()
            val overflow = reader.readInt()
            val maxLines = reader.readInt()

            return RcTextLayoutOperation(
                componentId = componentId,
                animationId = animationId,
                textId = textId,
                color = color,
                fontSize = fontSize,
                fontStyle = fontStyle,
                fontWeight = fontWeight,
                fontFamilyId = fontFamilyId,
                textAlign = textAlign,
                overflow = overflow,
                maxLines = maxLines
            )
        }
    }
}