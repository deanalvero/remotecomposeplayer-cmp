package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcColorConstantOperation(
    override val opCode: Int = OP_CODE,
    val colorId: Int,
    val color: Int
) : RcOperation {

    override val name: String = "ColorConstant"

    override fun toString(): String {
        val hex = "0x" + color.toUInt().toString(16).padStart(8, '0')
            .uppercase()
        return "ColorConstant[$colorId] = $hex"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 138
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            val colorId = reader.readInt()
            val color = reader.readInt()
            return RcColorConstantOperation(
                colorId = colorId,
                color = color
            )
        }
    }
}