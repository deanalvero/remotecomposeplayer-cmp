package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcScrollModifierOperation(
    override val opCode: Int = OP_CODE,
    val direction: Int,
    val position: Float,
    val max: Float,
    val notchMax: Float
) : RcOperation {

    override val name = "Modifier.scroll"

    override fun toString(): String {
        return "$name -> direction: $direction position: $position max: $max notchMax: $notchMax"
    }

    companion object : RcOpDecoder {

        const val OP_CODE = 226

        override val opCode = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            val direction = reader.readInt()
            val position = reader.readFloat()
            val max = reader.readFloat()
            val notchMax = reader.readFloat()

            return RcScrollModifierOperation(
                direction = direction,
                position = position,
                max = max,
                notchMax = notchMax
            )
        }
    }
}
