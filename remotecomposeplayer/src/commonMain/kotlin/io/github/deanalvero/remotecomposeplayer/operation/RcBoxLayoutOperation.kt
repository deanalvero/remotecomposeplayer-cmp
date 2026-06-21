package io.github.deanalvero.remotecomposeplayer.operation

import androidx.compose.ui.Alignment
import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcBoxLayoutOperation(
    override val opCode: Int = OP_CODE,
    val componentId: Int,
    val animationId: Int,
    val horizontalPositioning: Int,
    val verticalPositioning: Int
) : RcOperation {

    override val name: String = "BoxLayout"

    override fun toString(): String {
        return "$name [ID: $componentId, AnimID: $animationId] -> " +
                "Arrange(Horizontal): ${getPositioningString(horizontalPositioning)}, " +
                "Align(Vertical): ${getPositioningString(verticalPositioning)}"
    }

    private fun getPositioningString(pos: Int): String = when (pos) {
        START -> "START"
        CENTER -> "CENTER"
        END -> "END"
        TOP -> "TOP"
        BOTTOM -> "BOTTOM"
        else -> "UNKNOWN_POS($pos)"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 202
        override val opCode: Int = OP_CODE

        const val START = 1
        const val CENTER = 2
        const val END = 3
        const val TOP = 4
        const val BOTTOM = 5

        override fun decode(reader: RcBufferReader): RcOperation {
            val componentId = reader.readInt()
            val animationId = reader.readInt()
            val horizontalPositioning = reader.readInt()
            val verticalPositioning = reader.readInt()

            return RcBoxLayoutOperation(
                componentId = componentId,
                animationId = animationId,
                horizontalPositioning = horizontalPositioning,
                verticalPositioning = verticalPositioning
            )
        }
    }
}

fun mapBoxAlignment(horizontalPos: Int, verticalPos: Int): Alignment {
    return when (verticalPos) {
        RcBoxLayoutOperation.CENTER -> when (horizontalPos) {
            RcBoxLayoutOperation.CENTER -> Alignment.Center
            RcBoxLayoutOperation.END -> Alignment.CenterEnd
            else -> Alignment.CenterStart
        }
        RcBoxLayoutOperation.BOTTOM -> when (horizontalPos) {
            RcBoxLayoutOperation.CENTER -> Alignment.BottomCenter
            RcBoxLayoutOperation.END -> Alignment.BottomEnd
            else -> Alignment.BottomStart
        }
        else -> when (horizontalPos) {
            RcBoxLayoutOperation.CENTER -> Alignment.TopCenter
            RcBoxLayoutOperation.END -> Alignment.TopEnd
            else -> Alignment.TopStart
        }
    }
}