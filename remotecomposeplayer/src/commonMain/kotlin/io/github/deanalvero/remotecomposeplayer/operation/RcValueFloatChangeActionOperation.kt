package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcValueFloatChangeActionOperation(
    override val opCode: Int = OP_CODE,
    val targetValueId: Int,
    val value: Float
) : RcOperation {

    override val name: String = "ValueFloatChangeAction"

    override fun toString(): String {
        return "$name -> $targetValueId to $value"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 222
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            val targetValueId = reader.readInt()
            val value = reader.readFloat()
            return RcValueFloatChangeActionOperation(
                targetValueId = targetValueId,
                value = value
            )
        }
    }
}