package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcTextLookupIntOperation(
    override val opCode: Int = OP_CODE,
    val textId: Int,
    val dataSetId: Int,
    val indexId: Int
) : RcOperation {

    override val name: String = "TextLookupInt"

    override fun toString(): String {
        return "$name[textId=$textId, dataSetId=$dataSetId, indexId=$indexId]"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 153
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            val textId = reader.readInt()
            val dataSetId = reader.readInt()
            val indexId = reader.readInt()

            return RcTextLookupIntOperation(
                textId = textId,
                dataSetId = dataSetId,
                indexId = indexId
            )
        }
    }
}