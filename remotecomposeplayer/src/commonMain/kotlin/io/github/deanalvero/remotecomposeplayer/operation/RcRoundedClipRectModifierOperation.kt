package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcRoundedClipRectModifierOperation(
    override val opCode: Int = OP_CODE,
    val topStart: Float,
    val topEnd: Float,
    val bottomStart: Float,
    val bottomEnd: Float
) : RcOperation {

    override val name: String = "RoundedClipRectModifierOperation"

    override fun toString(): String {
        return "$name[topStart=$topStart, topEnd=$topEnd, bottomStart=$bottomStart, bottomEnd=$bottomEnd]"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 54
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            return RcRoundedClipRectModifierOperation(
                topStart = reader.readFloat(),
                topEnd = reader.readFloat(),
                bottomStart = reader.readFloat(),
                bottomEnd = reader.readFloat()
            )
        }
    }
}