package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcStateLayoutOperation(
    override val opCode: Int = OP_CODE,
    val componentId: Int,
    val animationId: Int,
    val horizontalPositioning: Int,
    val verticalPositioning: Int,
    val indexId: Int
) : RcOperation {

    override val name: String = "StateLayout"

    override fun toString(): String {
        return "$name [ID: $componentId, AnimID: $animationId] -> indexId: $indexId"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 217
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            val componentId = reader.readInt()
            val animationId = reader.readInt()
            val horizontalPositioning = reader.readInt()
            val verticalPositioning = reader.readInt()
            val indexId = reader.readInt()

            return RcStateLayoutOperation(
                componentId = componentId,
                animationId = animationId,
                horizontalPositioning = horizontalPositioning,
                verticalPositioning = verticalPositioning,
                indexId = indexId
            )
        }
    }
}