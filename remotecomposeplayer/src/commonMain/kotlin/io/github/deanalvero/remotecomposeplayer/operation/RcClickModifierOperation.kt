package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

class RcClickModifierOperation(
    override val opCode: Int = OP_CODE
) : RcOperation {

    override val name: String = "ClickModifier"

    override fun toString(): String {
        return name
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 59
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            return RcClickModifierOperation()
        }
    }
}