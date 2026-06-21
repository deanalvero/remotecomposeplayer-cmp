package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcComponentValueOperation(
    override val opCode: Int = OP_CODE,
    val type: Int,
    val componentId: Int,
    val valueId: Int
) : RcOperation {

    override val name: String = "ComponentValue"

    override fun toString(): String {
        val typeStr = when (type) {
            WIDTH -> "WIDTH"
            HEIGHT -> "HEIGHT"
            POS_X -> "POS_X"
            POS_Y -> "POS_Y"
            POS_ROOT_X -> "POS_ROOT_X"
            POS_ROOT_Y -> "POS_ROOT_Y"
            CONTENT_WIDTH -> "CONTENT_WIDTH"
            CONTENT_HEIGHT -> "CONTENT_HEIGHT"
            else -> "UNKNOWN($type)"
        }
        return "$name [Component ID: $componentId] -> Exposes $typeStr as Variable ID: $valueId"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 150
        override val opCode: Int = OP_CODE

        const val WIDTH = 0
        const val HEIGHT = 1
        const val POS_X = 2
        const val POS_Y = 3
        const val POS_ROOT_X = 4
        const val POS_ROOT_Y = 5
        const val CONTENT_WIDTH = 6
        const val CONTENT_HEIGHT = 7

        override fun decode(reader: RcBufferReader): RcOperation {
            val type = reader.readInt()
            val componentId = reader.readInt()
            val valueId = reader.readInt()

            return RcComponentValueOperation(
                type = type,
                componentId = componentId,
                valueId = valueId
            )
        }
    }
}