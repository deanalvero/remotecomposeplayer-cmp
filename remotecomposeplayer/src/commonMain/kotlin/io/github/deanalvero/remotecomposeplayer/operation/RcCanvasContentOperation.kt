package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcCanvasContentOperation(
    override val opCode: Int = OP_CODE,
    val componentId: Int
) : RcOperation {

    override val name: String = "CanvasContent"

    override fun toString(): String {
        return "$name [ID: $componentId] -> Canvas drawing content"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 207
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            val componentId = reader.readInt()
            return RcCanvasContentOperation(componentId = componentId)
        }
    }
}
