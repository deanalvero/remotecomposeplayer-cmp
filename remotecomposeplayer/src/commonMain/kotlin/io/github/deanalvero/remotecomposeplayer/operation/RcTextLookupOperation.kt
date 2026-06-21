package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcTextLookupOperation(
    override val opCode: Int = OP_CODE,
    val textId: Int,
    val dataSetId: Int,
    val index: Float
) : RcOperation {

    override val name: String = "TextLookup"

    override fun toString(): String {
        return "TextLookup[$textId] = $dataSetId $index"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 151
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            val textId = reader.readInt()
            val dataSetId = reader.readInt()
            val index = reader.readFloat()

            return RcTextLookupOperation(
                textId = textId,
                dataSetId = dataSetId,
                index = index
            )
        }
    }
}