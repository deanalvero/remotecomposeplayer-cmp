package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcThemeOperation(
    override val opCode: Int = OP_CODE,
    val theme: Int
) : RcOperation {

    override val name: String = "Theme"

    override fun toString(): String {
        return "SET_THEME $theme"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 63
        override val opCode: Int = OP_CODE

        const val SYSTEM = 0
        const val UNSPECIFIED = -1
        const val DARK = -2
        const val LIGHT = -3

        override fun decode(reader: RcBufferReader): RcOperation {
            val theme = reader.readInt()
            return RcThemeOperation(theme = theme)
        }
    }
}