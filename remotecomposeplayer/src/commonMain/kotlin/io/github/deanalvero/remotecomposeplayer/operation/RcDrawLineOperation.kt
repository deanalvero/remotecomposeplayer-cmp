package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcDrawLineOperation(
    override val opCode: Int = OP_CODE,
    val startX: Float,
    val startY: Float,
    val endX: Float,
    val endY: Float
) : RcOperation, CanvasScopedOperation {

    override val name: String = "DrawLine"

    override fun toString(): String {
        return "$name -> start=($startX, $startY), end=($endX, $endY)"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 47
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            val startX = reader.readFloat()
            val startY = reader.readFloat()
            val endX = reader.readFloat()
            val endY = reader.readFloat()

            return RcDrawLineOperation(
                startX = startX,
                startY = startY,
                endX = endX,
                endY = endY
            )
        }
    }
}
