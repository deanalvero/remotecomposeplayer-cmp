package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcDrawCircleOperation(
    override val opCode: Int = OP_CODE,
    val centerX: Float,
    val centerY: Float,
    val radius: Float
) : RcOperation, CanvasScopedOperation {

    override val name: String = "DrawCircle"

    override fun toString(): String {
        return "$name -> center=($centerX, $centerY), radius=$radius"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 46
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            val centerX = reader.readFloat()
            val centerY = reader.readFloat()
            val radius = reader.readFloat()

            return RcDrawCircleOperation(
                centerX = centerX,
                centerY = centerY,
                radius = radius
            )
        }
    }
}
