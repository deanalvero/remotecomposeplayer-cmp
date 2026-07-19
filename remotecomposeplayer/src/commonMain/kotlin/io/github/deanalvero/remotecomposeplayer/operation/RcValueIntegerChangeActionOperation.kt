package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcValueIntegerChangeActionOperation(
    override val opCode: Int = OP_CODE,
    val targetValueId: Int,
    val value: Int
) : RcOperation {

    override val name: String = "ValueIntegerChangeAction"

    override fun toString(): String {
        return "$name -> targetValueId: $targetValueId, value: $value"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 212
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            val targetValueId = reader.readInt()
            val value = reader.readInt()

            return RcValueIntegerChangeActionOperation(
                targetValueId = targetValueId,
                value = value
            )
        }
    }
}