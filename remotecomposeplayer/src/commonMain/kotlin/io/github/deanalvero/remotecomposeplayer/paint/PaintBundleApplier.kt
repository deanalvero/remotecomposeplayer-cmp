package io.github.deanalvero.remotecomposeplayer.paint

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import io.github.deanalvero.remotecomposeplayer.core.RemoteComposeContext

fun applyPaintBundle(
    paintData: IntArray,
    state: RcPaintState,
    context: RemoteComposeContext
) {
    var i = 0
    while (i < paintData.size) {
        val cmd = paintData[i++]
        val type = cmd and 0xFFFF
        val packed = cmd ushr 16

        when (type) {
            1 -> {
                state.textSize = context.resolveFloat(Float.fromBits(paintData[i++]))
            }
            4 -> {
                state.color = Color(paintData[i++])
                state.brush = null
            }
            5 -> {
                state.strokeWidth = context.resolveFloat(Float.fromBits(paintData[i++]))
                state.updateStrokeStyle()
            }
            6 -> {
                state.strokeMiter = context.resolveFloat(Float.fromBits(paintData[i++]))
                state.updateStrokeStyle()
            }
            7 -> {
                state.strokeCap = when (packed) {
                    1 -> StrokeCap.Round
                    2 -> StrokeCap.Square
                    else -> StrokeCap.Butt
                }
                state.updateStrokeStyle()
            }
            8 -> {
                state.style = when (packed) {
                    1, 2 -> Stroke(width = state.strokeWidth)
                    else -> Fill
                }
            }
            9 -> {
                val shaderId = paintData[i++]
            }
            10 -> {
                state.filterQuality = packed
            }
            11 -> {
                i = decodeGradient(cmd, paintData, i, state, context)
            }
            12 -> {
                state.alpha = context.resolveFloat(Float.fromBits(paintData[i++]))
            }
            13 -> {
                val filterColor = Color(paintData[i++])
                state.colorFilter = ColorFilter.tint(filterColor, mapBlendMode(packed))
            }
            14 -> {
                state.isAntiAlias = packed != 0
            }
            15 -> {
                state.strokeJoin = when (packed) {
                    1 -> StrokeJoin.Round
                    2 -> StrokeJoin.Bevel
                    else -> StrokeJoin.Miter
                }
                state.updateStrokeStyle()
            }
            16 -> {
                state.fontWeight = packed and 0x3FF
                state.isItalic = (packed ushr 10) > 0
                state.fontType = paintData[i++]
            }
            17 -> {
                // TODO
            }
            18 -> {
                state.blendMode = mapBlendMode(packed)
            }
            19 -> {
                state.color = Color(context.getColor(paintData[i++]))
                state.brush = null
            }
            20 -> {
                val filterColor = Color(context.getColor(paintData[i++]))
                state.colorFilter = ColorFilter.tint(filterColor, mapBlendMode(packed))
            }
            21 -> {
                state.colorFilter = null
            }
            22 -> {
                i++
            }
            23 -> {
                val count = packed
                val axes = mutableMapOf<Int, Float>()
                for (j in 0 until count) {
                    val tag = paintData[i++]
                    val value = context.resolveFloat(Float.fromBits(paintData[i++]))
                    axes[tag] = value
                }
                state.fontAxes = axes
            }
            24 -> {
                i += 3
            }
            25 -> {
                val count = packed
                if (count > 0) {
                    val intervals = FloatArray(count) { context.resolveFloat(Float.fromBits(paintData[i++])) }
                    state.pathEffect = PathEffect.dashPathEffect(intervals, 0f)
                } else {
                    state.pathEffect = null
                }
                state.updateStrokeStyle()
            }
            26 -> {
                i++
            }
        }
    }
}

private fun decodeGradient(
    cmd: Int,
    array: IntArray,
    startIndex: Int,
    state: RcPaintState,
    context: RemoteComposeContext
): Int {
    var i = startIndex
    val gradientType = cmd ushr 16

    val control = array[i++]
    val len = control and 0xFF
    val register = control ushr 16

    val colors = if (len > 0) {
        List(len) { idx ->
            val rawColor = array[i++]
            if ((register and (1 shl idx)) != 0) {
                Color(context.getColor(rawColor))
            } else {
                Color(rawColor)
            }
        }
    } else emptyList()

    val stopLen = array[i++]
    val stops = if (stopLen > 0) {
        List(stopLen) { context.resolveFloat(Float.fromBits(array[i++])) }
    } else null

    if (colors.isEmpty()) return i

    when (gradientType) {
        0 -> {
            val startX = context.resolveFloat(Float.fromBits(array[i++]))
            val startY = context.resolveFloat(Float.fromBits(array[i++]))
            val endX = context.resolveFloat(Float.fromBits(array[i++]))
            val endY = context.resolveFloat(Float.fromBits(array[i++]))
            val tileMode = mapTileMode(array[i++])

            state.brush = Brush.linearGradient(
                *buildColorStops(colors, stops),
                start = Offset(startX, startY),
                end = Offset(endX, endY),
                tileMode = tileMode
            )
        }
        1 -> {
            val centerX = context.resolveFloat(Float.fromBits(array[i++]))
            val centerY = context.resolveFloat(Float.fromBits(array[i++]))
            val radius = context.resolveFloat(Float.fromBits(array[i++]))
            val tileMode = mapTileMode(array[i++])

            state.brush = Brush.radialGradient(
                *buildColorStops(colors, stops),
                center = Offset(centerX, centerY),
                radius = radius,
                tileMode = tileMode
            )
        }
        2 -> {
            val centerX = context.resolveFloat(Float.fromBits(array[i++]))
            val centerY = context.resolveFloat(Float.fromBits(array[i++]))

            state.brush = Brush.sweepGradient(
                *buildColorStops(colors, stops),
                center = Offset(centerX, centerY)
            )
        }
    }
    return i
}

private fun buildColorStops(colors: List<Color>, stops: List<Float>?): Array<Pair<Float, Color>> {
    return if (stops != null && stops.size == colors.size) {
        stops.zip(colors).toTypedArray()
    } else {
        colors.mapIndexed { index, color ->
            (index.toFloat() / (colors.size - 1).coerceAtLeast(1)) to color
        }.toTypedArray()
    }
}

private fun mapBlendMode(mode: Int): BlendMode {
    return when (mode) {
        0 -> BlendMode.Clear
        1 -> BlendMode.Src
        2 -> BlendMode.Dst
        3 -> BlendMode.SrcOver
        4 -> BlendMode.DstOver
        5 -> BlendMode.SrcIn
        6 -> BlendMode.DstIn
        7 -> BlendMode.SrcOut
        8 -> BlendMode.DstOut
        9 -> BlendMode.SrcAtop
        10 -> BlendMode.DstAtop
        11 -> BlendMode.Xor
        12 -> BlendMode.Plus
        13 -> BlendMode.Modulate
        14 -> BlendMode.Screen
        15 -> BlendMode.Overlay
        16 -> BlendMode.Darken
        17 -> BlendMode.Lighten
        18 -> BlendMode.ColorDodge
        19 -> BlendMode.ColorBurn
        20 -> BlendMode.Hardlight
        21 -> BlendMode.Softlight
        22 -> BlendMode.Difference
        23 -> BlendMode.Exclusion
        24 -> BlendMode.Multiply
        25 -> BlendMode.Hue
        26 -> BlendMode.Saturation
        27 -> BlendMode.Color
        28 -> BlendMode.Luminosity
        else -> BlendMode.SrcOver
    }
}

private fun mapTileMode(mode: Int): TileMode {
    return when (mode) {
        1 -> TileMode.Repeated
        2 -> TileMode.Mirror
        3 -> TileMode.Decal
        else -> TileMode.Clamp
    }
}
