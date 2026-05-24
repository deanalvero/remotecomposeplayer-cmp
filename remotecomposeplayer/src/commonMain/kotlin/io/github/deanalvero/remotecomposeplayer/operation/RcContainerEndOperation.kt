package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

class RcContainerEndOperation(
    override val opCode: Int = OP_CODE
) : RcOperation {

    override val name: String = "ContainerEnd"

    override fun toString(): String {
        return "$name -> [End of Layout Scope]"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 214
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            return RcContainerEndOperation()
        }
    }
}