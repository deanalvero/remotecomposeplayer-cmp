package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation
import kotlin.math.roundToInt

data class RcBorderModifierOperation(
    override val opCode: Int = OP_CODE,
    val flags: Int,
    val colorId: Int,
    val reserve1: Int,
    val reserve2: Int,
    val borderWidth: Float,
    val roundedCorner: Float,
    val r: Float,
    val g: Float,
    val b: Float,
    val a: Float,
    val shapeType: Int
) : RcOperation, ModifierOperation {

    override val name: String = "Modifier.border"

    val usesColorId: Boolean
        get() = (flags and COLOR_REF) != 0

    val useLegacyDrawing: Boolean
        get() = reserve1 == 0

    val shapeName: String
        get() = when (shapeType) {
            SHAPE_RECTANGLE -> "Rectangle"
            SHAPE_CIRCLE -> "Circle"
            else -> "RoundedCorner($roundedCorner)"
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

        return "$name -> BorderWidth: ${borderWidth}dp | Radius: ${roundedCorner}dp | Shape: $shapeName | LegacyDrawing: $useLegacyDrawing | Fill: $fillDescription"
    }

    private fun Int.toHex(): String = this.toString(16).padStart(2, '0').uppercase()

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 107
        override val opCode: Int = OP_CODE

        const val COLOR_REF = 2
        const val SHAPE_RECTANGLE = 0
        const val SHAPE_CIRCLE = 1

        override fun decode(reader: RcBufferReader): RcOperation {
            val flags = reader.readInt()
            val colorId = reader.readInt()
            val reserve1 = reader.readInt()
            val reserve2 = reader.readInt()

            val borderWidth = reader.readFloat()
            val roundedCorner = reader.readFloat()
            val r = reader.readFloat()
            val g = reader.readFloat()
            val b = reader.readFloat()
            val a = reader.readFloat()

            val shapeType = reader.readInt()

            return RcBorderModifierOperation(
                flags = flags,
                colorId = colorId,
                reserve1 = reserve1,
                reserve2 = reserve2,
                borderWidth = borderWidth,
                roundedCorner = roundedCorner,
                r = r,
                g = g,
                b = b,
                a = a,
                shapeType = shapeType
            )
        }
    }
}