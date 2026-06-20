package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcMatrixSkewOperation(
    override val opCode: Int = OP_CODE,
    val skewX: Float,
    val skewY: Float
) : RcOperation, CanvasScopedOperation {

    override val name: String = "MatrixSkew"

    override fun toString(): String {
        return "$name -> skewX=$skewX, skewY=$skewY"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 128
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            val skewX = reader.readFloat()
            val skewY = reader.readFloat()
            return RcMatrixSkewOperation(
                skewX = skewX,
                skewY = skewY
            )
        }
    }
}