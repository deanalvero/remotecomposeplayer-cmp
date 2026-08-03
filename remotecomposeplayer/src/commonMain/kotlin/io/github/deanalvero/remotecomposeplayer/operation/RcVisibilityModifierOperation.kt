package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcVisibilityModifierOperation(
    override val opCode: Int = OP_CODE,
    val visibilityId: Int
) : RcOperation {

    override val name: String = "Modifier.visibility"

    override fun toString(): String {
        return "$name -> Visibility Variable ID: $visibilityId"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 211
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            val visibilityId = reader.readInt()
            return RcVisibilityModifierOperation(visibilityId = visibilityId)
        }
    }
}