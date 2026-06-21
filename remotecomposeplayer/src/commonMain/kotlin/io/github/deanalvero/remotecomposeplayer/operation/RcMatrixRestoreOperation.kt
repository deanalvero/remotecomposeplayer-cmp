package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

class RcMatrixRestoreOperation(
    override val opCode: Int = OP_CODE
) : RcOperation, CanvasScopedOperation {

    override val name: String = "MatrixRestore"

    override fun toString(): String {
        return name
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 131
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            return RcMatrixRestoreOperation()
        }
    }
}