package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcTextDataOperation(
    override val opCode: Int = OP_CODE,
    val textId: Int,
    val text: String
) : RcOperation {

    override val name: String = "TextData"

    override fun toString(): String {
        val cleanText = text.replace('\n', ' ')
        val displayText = if (cleanText.length > 40) "${cleanText.take(40)}..." else cleanText

        return "$name [ID: $textId] -> \"$displayText\""
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 102
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            val textId = reader.readInt()
            val text = reader.readString()

            return RcTextDataOperation(
                textId = textId,
                text = text
            )
        }
    }
}