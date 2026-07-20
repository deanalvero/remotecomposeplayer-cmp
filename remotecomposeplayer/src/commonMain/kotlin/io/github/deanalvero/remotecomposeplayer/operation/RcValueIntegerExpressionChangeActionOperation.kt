package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcValueIntegerExpressionChangeActionOperation(
    override val opCode: Int = OP_CODE,
    val targetValueId: Long,
    val valueExpressionId: Long
) : RcOperation {

    override val name: String = "ValueIntegerExpressionChangeActionOperation"

    override fun toString(): String {
        return "$name[targetValueId=$targetValueId, valueExpressionId=$valueExpressionId]"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 218
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            return RcValueIntegerExpressionChangeActionOperation(
                targetValueId = reader.readLong(),
                valueExpressionId = reader.readLong()
            )
        }
    }
}