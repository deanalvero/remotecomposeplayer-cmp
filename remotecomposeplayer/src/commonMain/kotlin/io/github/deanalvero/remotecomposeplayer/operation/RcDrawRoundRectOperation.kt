package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcDrawRoundRectOperation(
    override val opCode: Int = OP_CODE,
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float,
    val rx: Float,
    val ry: Float
) : RcOperation, CanvasScopedOperation {

    override val name: String = "DrawRoundRect"

    override fun toString(): String {
        return "$name[left=$left, top=$top, right=$right, bottom=$bottom, rx=$rx, ry=$ry]"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 51
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcDrawRoundRectOperation {
            return RcDrawRoundRectOperation(
                left = reader.readFloat(),
                top = reader.readFloat(),
                right = reader.readFloat(),
                bottom = reader.readFloat(),
                rx = reader.readFloat(),
                ry = reader.readFloat()
            )
        }
    }
}