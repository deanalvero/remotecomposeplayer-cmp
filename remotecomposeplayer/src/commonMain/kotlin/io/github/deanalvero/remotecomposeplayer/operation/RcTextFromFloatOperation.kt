package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcTextFromFloatOperation(
    override val opCode: Int = OP_CODE,
    val textId: Int,
    val value: Float,
    val digitsBefore: Short,
    val digitsAfter: Short,
    val flags: Int
) : RcOperation {

    override val name: String = "TextFromFloat"

    override fun toString(): String {
        return "$name [ID: $textId] -> value: $value | fmt: $digitsBefore.$digitsAfter | flags: $flags"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 135
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            val textId = reader.readInt()
            val value = reader.readFloat()

            val tmp = reader.readInt()
            val digitsAfter = (tmp and 0xFFFF).toShort()
            val digitsBefore = ((tmp ushr 16) and 0xFFFF).toShort()

            val flags = reader.readInt()

            return RcTextFromFloatOperation(
                textId = textId,
                value = value,
                digitsBefore = digitsBefore,
                digitsAfter = digitsAfter,
                flags = flags
            )
        }
    }
}
