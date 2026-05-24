package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcPaddingModifierOperation(
    override val opCode: Int = OP_CODE,
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float
) : RcOperation {

    override val name: String = "Modifier.padding"

    override fun toString(): String {
        val description = when {
            left == top && top == right && right == bottom -> "all = ${left}dp"
            left == right && top == bottom -> "horizontal = ${left}dp, vertical = ${top}dp"
            else -> "start = ${left}dp, top = ${top}dp, end = ${right}dp, bottom = ${bottom}dp"
        }

        return "$name -> $description"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 58
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            val left = reader.readFloat()
            val top = reader.readFloat()
            val right = reader.readFloat()
            val bottom = reader.readFloat()

            return RcPaddingModifierOperation(
                left = left,
                top = top,
                right = right,
                bottom = bottom
            )
        }
    }
}