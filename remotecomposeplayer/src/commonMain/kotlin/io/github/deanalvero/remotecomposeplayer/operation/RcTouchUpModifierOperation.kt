package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcTouchUpModifierOperation(
    override val opCode: Int = OP_CODE,
    override val actions: List<RcOperation> = emptyList()
) : RcOperation, ActionModifierOperation {

    override val name: String = "TouchUpModifier"

    override fun toString(): String {
        return name
    }

    override fun copyWithActions(actions: List<RcOperation>) = this.copy(actions = actions)

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 220
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            return RcTouchUpModifierOperation()
        }
    }
}