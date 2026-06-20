package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcMatrixRotateOperation(
    override val opCode: Int = OP_CODE,
    val rotate: Float,
    val pivotX: Float,
    val pivotY: Float
) : RcOperation, CanvasScopedOperation {

    override val name: String = "MatrixRotate"

    override fun toString(): String {
        return "$name -> degrees: $rotate, pivot: ($pivotX, $pivotY)"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 129
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            return RcMatrixRotateOperation(
                rotate = reader.readFloat(),
                pivotX = reader.readFloat(),
                pivotY = reader.readFloat()
            )
        }
    }
}