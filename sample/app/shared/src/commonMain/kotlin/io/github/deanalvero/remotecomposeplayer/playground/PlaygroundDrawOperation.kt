package io.github.deanalvero.remotecomposeplayer.playground

import io.github.deanalvero.remotecomposeplayer.core.RcOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcDrawArcOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcDrawCircleOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcDrawLineOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcDrawOvalOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcDrawOvalOperation.Companion.OP_CODE
import io.github.deanalvero.remotecomposeplayer.operation.RcDrawRectOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcDrawRoundRectOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcDrawSectorOperation

sealed interface PlaygroundDrawOperation {
    fun toOperation(): RcOperation

    data class Circle(
        val centerX: Float = 25f,
        val centerY: Float = 25f,
        val radius: Float = 25f
    ) : PlaygroundDrawOperation {
        override fun toOperation(): RcOperation {
            return RcDrawCircleOperation(
                centerX = centerX,
                centerY = centerY,
                radius = radius
            )
        }
    }

    data class Line(
        val startX: Float = 0f,
        val startY: Float = 0f,
        val endX: Float = 50f,
        val endY: Float = 50f
    ) : PlaygroundDrawOperation {
        override fun toOperation(): RcOperation {
            return RcDrawLineOperation(
                startX = startX,
                startY = startY,
                endX = endX,
                endY = endY
            )
        }
    }

    data class Rect(
        val left: Float = 0f,
        val top: Float = 0f,
        val right: Float = 100f,
        val bottom: Float = 100f
    ) : PlaygroundDrawOperation {
        override fun toOperation(): RcOperation {
            return RcDrawRectOperation(
                left = left,
                top = top,
                right = right,
                bottom = bottom
            )
        }
    }

    data class Arc(
        val left: Float = 0f,
        val top: Float = 0f,
        val right: Float = 100f,
        val bottom: Float = 100f,
        val startAngle: Float = 0f,
        val sweepAngle: Float = 90f
    ) : PlaygroundDrawOperation {
        override fun toOperation(): RcOperation {
            return RcDrawArcOperation(
                left = left,
                top = top,
                right = right,
                bottom = bottom,
                startAngle = startAngle,
                sweepAngle = sweepAngle
            )
        }
    }

    data class Oval(
        val left: Float = 0f,
        val top: Float = 20f,
        val right: Float = 100f,
        val bottom: Float = 80f
    ) : PlaygroundDrawOperation {
        override fun toOperation(): RcOperation {
            return RcDrawOvalOperation(
                left = left,
                top = top,
                right = right,
                bottom = bottom
            )
        }
    }

    data class Sector(
        val left: Float = 0f,
        val top: Float = 0f,
        val right: Float = 100f,
        val bottom: Float = 100f,
        val startAngle: Float = 0f,
        val sweepAngle: Float = 270f
    ) : PlaygroundDrawOperation {
        override fun toOperation(): RcOperation {
            return RcDrawSectorOperation(
                left = left,
                top = top,
                right = right,
                bottom = bottom,
                startAngle = startAngle,
                sweepAngle = sweepAngle
            )
        }
    }

    data class RoundRect(
        val left: Float = 0f,
        val top: Float = 0f,
        val right: Float = 100f,
        val bottom: Float = 100f,
        val rx: Float = 16f,
        val ry: Float = 16f
    ) : PlaygroundDrawOperation {
        override fun toOperation(): RcOperation {
            return RcDrawRoundRectOperation(
                left = left,
                top = top,
                right = right,
                bottom = bottom,
                rx = rx,
                ry = ry
            )
        }
    }
}

fun defaultDrawOperation(kind: PlaygroundDrawOperationKind): PlaygroundDrawOperation {
    return when (kind) {
        PlaygroundDrawOperationKind.Arc -> PlaygroundDrawOperation.Arc()
        PlaygroundDrawOperationKind.Circle -> PlaygroundDrawOperation.Circle()
        PlaygroundDrawOperationKind.Line -> PlaygroundDrawOperation.Line()
        PlaygroundDrawOperationKind.Oval -> PlaygroundDrawOperation.Oval()
        PlaygroundDrawOperationKind.Rect -> PlaygroundDrawOperation.Rect()
        PlaygroundDrawOperationKind.RoundRect -> PlaygroundDrawOperation.RoundRect()
        PlaygroundDrawOperationKind.Sector -> PlaygroundDrawOperation.Sector()
    }
}

fun PlaygroundDrawOperation.label(): String {
    return when (this) {
        is PlaygroundDrawOperation.Arc -> "Draw Arc"
        is PlaygroundDrawOperation.Circle -> "Draw Circle"
        is PlaygroundDrawOperation.Line -> "Draw Line"
        is PlaygroundDrawOperation.Oval -> "Draw Oval"
        is PlaygroundDrawOperation.Rect -> "Draw Rect"
        is PlaygroundDrawOperation.RoundRect -> "Draw RoundRect"
        is PlaygroundDrawOperation.Sector -> "Draw Sector"
    }
}
