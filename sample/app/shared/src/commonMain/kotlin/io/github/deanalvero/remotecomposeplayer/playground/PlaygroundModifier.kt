package io.github.deanalvero.remotecomposeplayer.playground

import io.github.deanalvero.remotecomposeplayer.core.RcOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcBackgroundModifierOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcHeightModifierOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcPaddingModifierOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcWidthModifierOperation
import io.github.deanalvero.remotecomposeplayer.operation.common.RcDimensionType

sealed interface PlaygroundModifier {
    fun toOperation(): RcOperation

    data class Padding(
        val left: Float = 0f,
        val top: Float = 0f,
        val right: Float = 0f,
        val bottom: Float = 0f
    ) : PlaygroundModifier {
        override fun toOperation(): RcOperation {
            return RcPaddingModifierOperation(
                left = left,
                top = top,
                right = right,
                bottom = bottom
            )
        }
    }

    data class Background(
        val argb: Int = 0xFFE0E0E0.toInt(),
        val shapeType: Int = RcBackgroundModifierOperation.SHAPE_RECTANGLE
    ) : PlaygroundModifier {
        override fun toOperation(): RcOperation {
            val a = ((argb ushr 24) and 0xFF) / 255f
            val r = ((argb ushr 16) and 0xFF) / 255f
            val g = ((argb ushr 8) and 0xFF) / 255f
            val b = (argb and 0xFF) / 255f

            return RcBackgroundModifierOperation(
                flags = 0,
                colorId = 0,
                r = r,
                g = g,
                b = b,
                a = a,
                shapeType = shapeType
            )
        }
    }

    data class Width(
        val typeId: Int = RcDimensionType.WRAP.id,
        val value: Float = 0f
    ) : PlaygroundModifier {
        override fun toOperation(): RcOperation {
            return RcWidthModifierOperation(
                typeId = typeId,
                value = value
            )
        }
    }

    data class Height(
        val typeId: Int = RcDimensionType.WRAP.id,
        val value: Float = 0f
    ) : PlaygroundModifier {
        override fun toOperation(): RcOperation {
            return RcHeightModifierOperation(
                typeId = typeId,
                value = value
            )
        }
    }
}

fun defaultModifier(kind: PlaygroundModifierKind): PlaygroundModifier {
    return when (kind) {
        PlaygroundModifierKind.Padding -> PlaygroundModifier.Padding()
        PlaygroundModifierKind.Background -> PlaygroundModifier.Background()
        PlaygroundModifierKind.Width -> PlaygroundModifier.Width()
        PlaygroundModifierKind.Height -> PlaygroundModifier.Height()
    }
}

fun PlaygroundModifier.newFrom(kind: PlaygroundModifierKind): PlaygroundModifier = defaultModifier(kind)

fun PlaygroundModifier.label(): String {
    return when (this) {
        is PlaygroundModifier.Padding -> "Padding"
        is PlaygroundModifier.Background -> "Background"
        is PlaygroundModifier.Width -> "Width"
        is PlaygroundModifier.Height -> "Height"
    }
}