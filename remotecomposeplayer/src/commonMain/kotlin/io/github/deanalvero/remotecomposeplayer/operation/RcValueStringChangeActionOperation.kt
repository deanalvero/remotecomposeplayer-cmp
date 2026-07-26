package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcValueStringChangeActionOperation(
    override val opCode: Int = OP_CODE,
    val targetValueId: Int,
    val valueId: Int
) : RcOperation {

    override val name: String = "ValueStringChangeAction"

    override fun toString(): String {
        return "$name -> $targetValueId to $valueId"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 213
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            val targetValueId = reader.readInt()
            val valueId = reader.readInt()
            return RcValueStringChangeActionOperation(
                targetValueId = targetValueId,
                valueId = valueId
            )
        }
    }
}