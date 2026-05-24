package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcColumnLayoutOperation(
    override val opCode: Int = OP_CODE,
    val componentId: Int,
    val animationId: Int,
    val horizontalPositioning: Int,
    val verticalPositioning: Int,
    val spacedBy: Float
) : RcOperation {

    override val name: String = "ColumnLayout"

    override fun toString(): String {
        val vPos = getPositioningString(verticalPositioning)
        val hPos = getPositioningString(horizontalPositioning)
        val spacing = if (spacedBy > 0f) " | SpacedBy: ${spacedBy}px/dp" else ""

        return "$name [ID: $componentId, AnimID: $animationId] -> Arrange(Vertical): $vPos, Align(Horizontal): $hPos$spacing"
    }

    private fun getPositioningString(pos: Int): String = when (pos) {
        START -> "START"
        CENTER -> "CENTER"
        END -> "END"
        TOP -> "TOP"
        BOTTOM -> "BOTTOM"
        SPACE_BETWEEN -> "SPACE_BETWEEN"
        SPACE_EVENLY -> "SPACE_EVENLY"
        SPACE_AROUND -> "SPACE_AROUND"
        else -> "UNKNOWN_POS($pos)"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 204
        override val opCode: Int = OP_CODE

        const val START = 1
        const val CENTER = 2
        const val END = 3
        const val TOP = 4
        const val BOTTOM = 5
        const val SPACE_BETWEEN = 6
        const val SPACE_EVENLY = 7
        const val SPACE_AROUND = 8

        override fun decode(reader: RcBufferReader): RcOperation {
            val componentId = reader.readInt()
            val animationId = reader.readInt()
            val horizontalPositioning = reader.readInt()
            val verticalPositioning = reader.readInt()
            val spacedBy = reader.readFloat()

            return RcColumnLayoutOperation(
                componentId = componentId,
                animationId = animationId,
                horizontalPositioning = horizontalPositioning,
                verticalPositioning = verticalPositioning,
                spacedBy = spacedBy
            )
        }
    }
}