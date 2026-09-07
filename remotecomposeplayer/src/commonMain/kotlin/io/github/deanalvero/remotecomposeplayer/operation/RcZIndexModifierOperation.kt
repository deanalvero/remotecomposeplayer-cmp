package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcZIndexModifierOperation(
    override val opCode: Int = OP_CODE,
    val zIndexValue: Float
) : RcOperation, ModifierOperation {

    override val name: String = "ZIndexModifier"

    override fun toString(): String = "$name -> value: $zIndexValue"

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 223
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            val zIndex = reader.readFloat()

            return RcZIndexModifierOperation(
                zIndexValue = zIndex
            )
        }
    }
}
