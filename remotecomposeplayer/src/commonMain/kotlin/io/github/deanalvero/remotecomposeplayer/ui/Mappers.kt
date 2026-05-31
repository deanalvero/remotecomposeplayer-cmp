package io.github.deanalvero.remotecomposeplayer.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import io.github.deanalvero.remotecomposeplayer.operation.RcColumnLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcRowLayoutOperation

fun mapColumnAlignment(horizontalPos: Int): Alignment.Horizontal {
    return when (horizontalPos) {
        RcColumnLayoutOperation.CENTER -> Alignment.CenterHorizontally
        RcColumnLayoutOperation.END -> Alignment.End
        else -> Alignment.Start
    }
}

fun mapColumnArrangement(verticalPos: Int, spacedBy: Float): Arrangement.Vertical {
    val spacing = spacedBy.dp
    return when (verticalPos) {
        RcColumnLayoutOperation.SPACE_BETWEEN -> Arrangement.SpaceBetween
        RcColumnLayoutOperation.SPACE_EVENLY -> Arrangement.SpaceEvenly
        RcColumnLayoutOperation.SPACE_AROUND -> Arrangement.SpaceAround
        RcColumnLayoutOperation.CENTER ->
            if (spacedBy > 0) Arrangement.spacedBy(spacing, Alignment.CenterVertically) else Arrangement.Center
        RcColumnLayoutOperation.BOTTOM ->
            if (spacedBy > 0) Arrangement.spacedBy(spacing, Alignment.Bottom) else Arrangement.Bottom
        else ->
            if (spacedBy > 0) Arrangement.spacedBy(spacing, Alignment.Top) else Arrangement.Top
    }
}

fun mapRowAlignment(verticalPos: Int): Alignment.Vertical {
    return when (verticalPos) {
        RcRowLayoutOperation.CENTER -> Alignment.CenterVertically
        RcRowLayoutOperation.BOTTOM -> Alignment.Bottom
        else -> Alignment.Top
    }
}

fun mapRowArrangement(horizontalPos: Int, spacedBy: Float): Arrangement.Horizontal {
    val spacing = spacedBy.dp
    return when (horizontalPos) {
        RcRowLayoutOperation.SPACE_BETWEEN -> Arrangement.SpaceBetween
        RcRowLayoutOperation.SPACE_EVENLY -> Arrangement.SpaceEvenly
        RcRowLayoutOperation.SPACE_AROUND -> Arrangement.SpaceAround
        RcRowLayoutOperation.CENTER ->
            if (spacedBy > 0) Arrangement.spacedBy(spacing, Alignment.CenterHorizontally) else Arrangement.Center
        RcRowLayoutOperation.END ->
            if (spacedBy > 0) Arrangement.spacedBy(spacing, Alignment.End) else Arrangement.End
        else ->
            if (spacedBy > 0) Arrangement.spacedBy(spacing, Alignment.Start) else Arrangement.Start
    }
}
