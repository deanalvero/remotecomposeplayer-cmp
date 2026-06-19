package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcCanvasLayoutOperation(
    override val opCode: Int = OP_CODE,
    val componentId: Int,
    val animationId: Int
) : RcOperation {

    override val name: String = "CanvasLayout"

    override fun toString(): String {
        return "$name [ID: $componentId, AnimID: $animationId] -> Canvas container"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 205
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            val componentId = reader.readInt()
            val animationId = reader.readInt()
            return RcCanvasLayoutOperation(
                componentId = componentId,
                animationId = animationId
            )
        }
    }
}
