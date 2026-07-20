package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcDataIntOperation(
    override val opCode: Int = OP_CODE,
    val id: Int,
    val value: Int
) : RcOperation {

    override val name: String = "IntegerConstant"

    override fun toString(): String {
        return "$name[$id] = $value"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 140
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            return RcDataIntOperation(
                id = reader.readInt(),
                value = reader.readInt()
            )
        }
    }
}