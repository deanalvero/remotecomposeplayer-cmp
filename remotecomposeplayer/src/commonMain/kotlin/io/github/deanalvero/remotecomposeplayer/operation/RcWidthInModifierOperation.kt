package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcWidthInModifierOperation(
    override val opCode: Int = OP_CODE,
    val min: Float,
    val max: Float
) : RcOperation {

    override val name = "Modifier.widthIn"

    override fun toString(): String {
        return "$name -> min: $min max: $max"
    }

    companion object : RcOpDecoder {

        const val OP_CODE = 231

        override val opCode = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            return RcWidthInModifierOperation(
                min = reader.readFloat(),
                max = reader.readFloat()
            )
        }
    }
}