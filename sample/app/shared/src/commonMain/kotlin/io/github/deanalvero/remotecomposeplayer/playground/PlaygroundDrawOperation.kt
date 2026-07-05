package io.github.deanalvero.remotecomposeplayer.playground

import io.github.deanalvero.remotecomposeplayer.core.RcOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcDrawArcOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcDrawCircleOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcDrawLineOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcDrawRectOperation

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
}

fun defaultDrawOperation(kind: PlaygroundDrawOperationKind): PlaygroundDrawOperation {
    return when (kind) {
        PlaygroundDrawOperationKind.Arc -> PlaygroundDrawOperation.Arc()
        PlaygroundDrawOperationKind.Circle -> PlaygroundDrawOperation.Circle()
        PlaygroundDrawOperationKind.Line -> PlaygroundDrawOperation.Line()
        PlaygroundDrawOperationKind.Rect -> PlaygroundDrawOperation.Rect()
    }
}

fun PlaygroundDrawOperation.label(): String {
    return when (this) {
        is PlaygroundDrawOperation.Arc -> "Draw Arc"
        is PlaygroundDrawOperation.Circle -> "Draw Circle"
        is PlaygroundDrawOperation.Line -> "Draw Line"
        is PlaygroundDrawOperation.Rect -> "Draw Rect"
    }
}
