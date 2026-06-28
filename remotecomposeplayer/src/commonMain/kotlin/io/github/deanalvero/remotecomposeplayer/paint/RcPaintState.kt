package io.github.deanalvero.remotecomposeplayer.paint

import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke

class RcPaintState {
    var color: Color = Color.Black
    var brush: Brush? = null
    var alpha: Float = 1.0f

    var style: DrawStyle = Fill
    var strokeWidth: Float = 0f
    var strokeCap: StrokeCap = StrokeCap.Butt
    var strokeJoin: StrokeJoin = StrokeJoin.Miter
    var strokeMiter: Float = 4f
    var pathEffect: PathEffect? = null

    var colorFilter: ColorFilter? = null
    var blendMode: BlendMode = BlendMode.SrcOver
    var filterQuality: Int = 0
    var isAntiAlias: Boolean = true

    var textSize: Float = 16f
    var fontWeight: Int = 400
    var isItalic: Boolean = false
    var fontType: Int = 0
    var fontAxes: Map<Int, Float> = emptyMap()

    fun updateStrokeStyle() {
        if (style is Stroke || strokeWidth > 0f) {
            style = Stroke(
                width = strokeWidth,
                miter = strokeMiter,
                cap = strokeCap,
                join = strokeJoin,
                pathEffect = pathEffect
            )
        }
    }
}