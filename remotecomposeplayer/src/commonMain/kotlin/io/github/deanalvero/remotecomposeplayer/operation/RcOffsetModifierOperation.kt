package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcOffsetModifierOperation(
    override val opCode: Int = OP_CODE,
    val x: Float,
    val y: Float
) : RcOperation, ModifierOperation {

    override val name: String = "OffsetModifier"

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 221
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            return RcOffsetModifierOperation(
                x = reader.readFloat(),
                y = reader.readFloat()
            )
        }
    }
}