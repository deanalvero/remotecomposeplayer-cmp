package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcDrawArcOperation(
    override val opCode: Int = OP_CODE,
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float,
    val startAngle: Float,
    val sweepAngle: Float
) : RcOperation, CanvasScopedOperation {

    override val name: String = "DrawArc"

    override fun toString(): String {
        return "$name[left=$left, top=$top, right=$right, bottom=$bottom, startAngle=$startAngle, sweepAngle=$sweepAngle]"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 152
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            return RcDrawArcOperation(
                left = reader.readFloat(),
                top = reader.readFloat(),
                right = reader.readFloat(),
                bottom = reader.readFloat(),
                startAngle = reader.readFloat(),
                sweepAngle = reader.readFloat()
            )
        }
    }
}