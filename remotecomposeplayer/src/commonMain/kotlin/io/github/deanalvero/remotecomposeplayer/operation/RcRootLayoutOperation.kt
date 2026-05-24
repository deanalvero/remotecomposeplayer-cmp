package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcRootLayoutOperation(
    override val opCode: Int = OP_CODE,
    val componentId: Int
) : RcOperation {

    override val name: String = "RootLayout"

    override fun toString(): String {
        return "$name [Component ID: $componentId] -> Document Tree Entry Point"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 200
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            val componentId = reader.readInt()

            return RcRootLayoutOperation(
                componentId = componentId
            )
        }
    }
}