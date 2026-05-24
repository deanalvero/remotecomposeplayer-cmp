package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation
import kotlin.math.roundToInt

data class RcBackgroundModifierOperation(
    override val opCode: Int = OP_CODE,
    val flags: Int,
    val colorId: Int,
    val r: Float,
    val g: Float,
    val b: Float,
    val a: Float,
    val shapeType: Int
) : RcOperation {

    override val name: String = "Modifier.background"

    val usesColorId: Boolean
        get() = (flags and COLOR_REF) != 0

    val shapeName: String
        get() = when (shapeType) {
            SHAPE_RECTANGLE -> "Rectangle"
            SHAPE_CIRCLE -> "Circle"
            else -> "Unknown($shapeType)"
        }

    override fun toString(): String {
        val fillDescription = if (usesColorId) {
            "ColorRef: Context ID [$colorId]"
        } else {
            val a8 = (a * 255).roundToInt().coerceIn(0, 255).toHex()
            val r8 = (r * 255).roundToInt().coerceIn(0, 255).toHex()
            val g8 = (g * 255).roundToInt().coerceIn(0, 255).toHex()
            val b8 = (b * 255).roundToInt().coerceIn(0, 255).toHex()

            "Color: #$a8$r8$g8$b8"
        }

        return "$name -> Shape: $shapeName | Fill: $fillDescription"
    }

    private fun Int.toHex(): String = this.toString(16).padStart(2, '0').uppercase()

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 55
        override val opCode: Int = OP_CODE

        const val COLOR_REF = 2
        const val SHAPE_RECTANGLE = 0
        const val SHAPE_CIRCLE = 1

        override fun decode(reader: RcBufferReader): RcOperation {
            val flags = reader.readInt()
            val colorId = reader.readInt()

            reader.readInt()
            reader.readInt()

            val r = reader.readFloat()
            val g = reader.readFloat()
            val b = reader.readFloat()
            val a = reader.readFloat()

            val shapeType = reader.readInt()

            return RcBackgroundModifierOperation(
                flags = flags,
                colorId = colorId,
                r = r,
                g = g,
                b = b,
                a = a,
                shapeType = shapeType
            )
        }
    }
}