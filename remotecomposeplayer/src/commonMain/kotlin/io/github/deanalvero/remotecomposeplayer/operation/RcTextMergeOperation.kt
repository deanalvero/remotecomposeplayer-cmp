package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcTextMergeOperation(
    override val opCode: Int = OP_CODE,
    val textId: Int,
    val srcId1: Int,
    val srcId2: Int
) : RcOperation {

    override val name: String = "TextMerge"

    override fun toString(): String {
        return "TextMerge[$textId] = [$srcId1] + [$srcId2]"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 136
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            val textId = reader.readInt()
            val srcId1 = reader.readInt()
            val srcId2 = reader.readInt()

            return RcTextMergeOperation(
                textId = textId,
                srcId1 = srcId1,
                srcId2 = srcId2
            )
        }
    }
}
