package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcDataFloatOperation(
    override val opCode: Int = OP_CODE,
    val id: Int,
    val value: Float
) : RcOperation {

    override val name: String = "FloatConstant"

    override fun toString(): String {
        return "$name[$id] = $value"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 80
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            return RcDataFloatOperation(
                id = reader.readInt(),
                value = reader.readFloat()
            )
        }
    }
}