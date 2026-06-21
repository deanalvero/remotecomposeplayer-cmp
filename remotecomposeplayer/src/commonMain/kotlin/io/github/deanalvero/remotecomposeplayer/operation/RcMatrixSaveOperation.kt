package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

class RcMatrixSaveOperation(
    override val opCode: Int = OP_CODE
) : RcOperation, CanvasScopedOperation {

    override val name: String = "MatrixSave"

    override fun toString(): String {
        return name
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 130
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            return RcMatrixSaveOperation()
        }
    }
}