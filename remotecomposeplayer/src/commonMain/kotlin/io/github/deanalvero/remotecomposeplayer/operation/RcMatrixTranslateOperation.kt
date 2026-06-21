package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcMatrixTranslateOperation(
    override val opCode: Int = OP_CODE,
    val dx: Float,
    val dy: Float
) : RcOperation, CanvasScopedOperation {

    override val name: String = "MatrixTranslate"

    override fun toString(): String {
        return "$name -> dx: $dx, dy: $dy"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 127
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            return RcMatrixTranslateOperation(
                dx = reader.readFloat(),
                dy = reader.readFloat()
            )
        }
    }
}