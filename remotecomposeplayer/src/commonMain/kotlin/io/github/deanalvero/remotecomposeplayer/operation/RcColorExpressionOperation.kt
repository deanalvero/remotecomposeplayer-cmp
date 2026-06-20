package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcColorExpressionOperation(
    override val opCode: Int = OP_CODE,
    val id: Int,
    val modeRaw: Int,
    val param2: Int,
    val param3: Int,
    val param4: Int
) : RcOperation {

    override val name: String = "ColorExpression"

    val mode: Int get() = modeRaw and 0xFF

    override fun toString(): String {
        return "ColorExpression[$id] mode=$modeRaw p2=$param2 p3=$param3 p4=$param4"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 134
        override val opCode: Int = OP_CODE

        const val COLOR_COLOR_INTERPOLATE = 0
        const val ID_COLOR_INTERPOLATE = 1
        const val COLOR_ID_INTERPOLATE = 2
        const val ID_ID_INTERPOLATE = 3
        const val HSV_MODE = 4
        const val ARGB_MODE = 5
        const val IDARGB_MODE = 6

        override fun decode(reader: RcBufferReader): RcOperation {
            val id = reader.readInt()
            val modeRaw = reader.readInt()
            val param2 = reader.readInt()
            val param3 = reader.readInt()
            val param4 = reader.readInt()

            return RcColorExpressionOperation(
                id = id,
                modeRaw = modeRaw,
                param2 = param2,
                param3 = param3,
                param4 = param4
            )
        }
    }
}