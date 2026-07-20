package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcDataBooleanOperation(
    override val opCode: Int = OP_CODE,
    val id: Int,
    val value: Boolean
) : RcOperation {

    override val name: String = "BooleanConstant"

    override fun toString(): String {
        return "$name[$id] = $value"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 143
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            return RcDataBooleanOperation(
                id = reader.readInt(),
                value = reader.readByte().toInt() != 0
            )
        }
    }
}