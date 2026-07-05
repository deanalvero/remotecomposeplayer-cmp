package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcDrawSectorOperation(
    override val opCode: Int = OP_CODE,
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float,
    val startAngle: Float,
    val sweepAngle: Float
) : RcOperation, CanvasScopedOperation {

    override val name: String = "DrawSector"

    override fun toString(): String {
        return "$name[left=$left, top=$top, right=$right, bottom=$bottom, startAngle=$startAngle, sweepAngle=$sweepAngle]"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 52
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcDrawSectorOperation {
            return RcDrawSectorOperation(
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