package io.github.deanalvero.remotecomposeplayer.playground

import io.github.deanalvero.remotecomposeplayer.core.RcOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcDrawCircleOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcDrawLineOperation

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
}

fun defaultDrawOperation(kind: PlaygroundDrawOperationKind): PlaygroundDrawOperation {
    return when (kind) {
        PlaygroundDrawOperationKind.Circle -> PlaygroundDrawOperation.Circle()
        PlaygroundDrawOperationKind.Line -> PlaygroundDrawOperation.Line()
    }
}

fun PlaygroundDrawOperation.label(): String {
    return when (this) {
        is PlaygroundDrawOperation.Circle -> "Draw Circle"
        is PlaygroundDrawOperation.Line -> "Draw Line"
    }
}
