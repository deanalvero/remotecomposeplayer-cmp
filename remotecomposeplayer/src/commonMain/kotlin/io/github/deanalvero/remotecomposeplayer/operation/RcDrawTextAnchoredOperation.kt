package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcDrawTextAnchoredOperation(
    override val opCode: Int = OP_CODE,
    val textId: Int,
    val x: Float,
    val y: Float,
    val panX: Float,
    val panY: Float,
    val flags: Int
) : RcOperation, CanvasScopedOperation {

    override val name: String = "DrawTextAnchored"

    override fun toString(): String {
        return "$name -> TextRef: [$textId] | pos: ($x, $y) | pan: ($panX, $panY) | flags: $flags"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 133
        override val opCode: Int = OP_CODE

        const val ANCHOR_TEXT_RTL = 1
        const val ANCHOR_MONOSPACE_MEASURE = 2
        const val MEASURE_EVERY_TIME = 4
        const val BASELINE_RELATIVE = 8

        override fun decode(reader: RcBufferReader): RcOperation {
            return RcDrawTextAnchoredOperation(
                textId = reader.readInt(),
                x = reader.readFloat(),
                y = reader.readFloat(),
                panX = reader.readFloat(),
                panY = reader.readFloat(),
                flags = reader.readInt()
            )
        }
    }
}