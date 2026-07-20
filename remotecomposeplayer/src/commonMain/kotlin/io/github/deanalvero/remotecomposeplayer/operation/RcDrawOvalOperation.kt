package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcDrawOvalOperation(
    override val opCode: Int = OP_CODE,
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float
) : RcOperation, CanvasScopedOperation {

    override val name: String = "DrawOval"

    override fun toString(): String {
        return "$name[left=$left, top=$top, right=$right, bottom=$bottom]"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 56
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcDrawOvalOperation {
            return RcDrawOvalOperation(
                left = reader.readFloat(),
                top = reader.readFloat(),
                right = reader.readFloat(),
                bottom = reader.readFloat()
            )
        }
    }
}