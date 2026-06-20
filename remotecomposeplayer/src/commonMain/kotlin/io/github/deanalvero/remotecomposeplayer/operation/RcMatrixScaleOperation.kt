package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcMatrixScaleOperation(
    override val opCode: Int = OP_CODE,
    val scaleX: Float,
    val scaleY: Float,
    val pivotX: Float,
    val pivotY: Float
) : RcOperation, CanvasScopedOperation {

    override val name: String = "MatrixScale"

    override fun toString(): String {
        return "$name -> scaleX: $scaleX, scaleY: $scaleY, pivot: ($pivotX, $pivotY)"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 126
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            return RcMatrixScaleOperation(
                scaleX = reader.readFloat(),
                scaleY = reader.readFloat(),
                pivotX = reader.readFloat(),
                pivotY = reader.readFloat()
            )
        }
    }
}