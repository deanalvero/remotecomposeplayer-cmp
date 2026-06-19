package io.github.deanalvero.remotecomposeplayer.ui

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import io.github.deanalvero.remotecomposeplayer.core.RcOperation
import io.github.deanalvero.remotecomposeplayer.core.RemoteComposeContext
import io.github.deanalvero.remotecomposeplayer.operation.RcTextLayoutOperation

@Composable
fun RenderLeaf(
    operation: RcOperation,
    context: RemoteComposeContext,
    modifier: Modifier = Modifier
) {
    when (operation) {
        is RcTextLayoutOperation -> {
            val resolvedText = context.getText(operation.textId)

            val composeTextAlign = when (operation.textAlign and 0xFFFF) {
                RcTextLayoutOperation.TEXT_ALIGN_LEFT -> TextAlign.Left
                RcTextLayoutOperation.TEXT_ALIGN_RIGHT -> TextAlign.Right
                RcTextLayoutOperation.TEXT_ALIGN_CENTER -> TextAlign.Center
                RcTextLayoutOperation.TEXT_ALIGN_JUSTIFY -> TextAlign.Justify
                RcTextLayoutOperation.TEXT_ALIGN_START -> TextAlign.Start
                RcTextLayoutOperation.TEXT_ALIGN_END -> TextAlign.End
                else -> TextAlign.Unspecified
            }

            val composeOverflow = when (operation.overflow) {
                RcTextLayoutOperation.OVERFLOW_CLIP -> TextOverflow.Clip
                RcTextLayoutOperation.OVERFLOW_ELLIPSIS,
                RcTextLayoutOperation.OVERFLOW_START_ELLIPSIS,
                RcTextLayoutOperation.OVERFLOW_MIDDLE_ELLIPSIS -> TextOverflow.Ellipsis
                RcTextLayoutOperation.OVERFLOW_VISIBLE -> TextOverflow.Visible
                else -> TextOverflow.Clip
            }

            val composeFontStyle = if (operation.fontStyle == 1) {
                FontStyle.Italic
            } else {
                FontStyle.Normal
            }

            BasicText(
                text = resolvedText,
                modifier = modifier,
                style = TextStyle(
                    color = Color(operation.color),
                    fontSize = operation.fontSize.sp,
                    fontWeight = FontWeight(operation.fontWeight.toInt().coerceIn(1, 1000)),
                    fontStyle = composeFontStyle,
                    textAlign = composeTextAlign,
                    fontFamily = FontFamily.Default
                ),
                overflow = composeOverflow,
                maxLines = operation.maxLines
            )
        }
        else -> Unit
    }
}