package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcLayoutContentOperation(
    override val opCode: Int = OP_CODE,
    val componentId: Int
) : RcOperation {

    override val name: String = "LayoutContent"

    override fun toString(): String {
        return "$name [ID: $componentId] -> Child Component Container Scope"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 201
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            val componentId = reader.readInt()

            return RcLayoutContentOperation(
                componentId = componentId
            )
        }
    }
}