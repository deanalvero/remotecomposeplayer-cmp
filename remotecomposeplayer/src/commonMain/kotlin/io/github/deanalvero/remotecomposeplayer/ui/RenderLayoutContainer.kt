package io.github.deanalvero.remotecomposeplayer.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import io.github.deanalvero.remotecomposeplayer.core.RemoteComposeContext
import io.github.deanalvero.remotecomposeplayer.operation.CanvasScopedOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcBoxLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcCanvasLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcColumnLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcDrawCircleOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcDrawLineOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcDrawTextAnchoredOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcLayoutContentOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcMatrixRestoreOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcMatrixRotateOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcMatrixSaveOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcMatrixScaleOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcMatrixSkewOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcMatrixTranslateOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcPaintDataOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcRootLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcRowLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcTextLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.mapBoxAlignment
import io.github.deanalvero.remotecomposeplayer.paint.RcPaintState
import io.github.deanalvero.remotecomposeplayer.paint.applyPaintBundle

@Composable
fun RenderLayoutContainer(
    node: RcNode.Layout,
    context: RemoteComposeContext,
    modifier: Modifier = Modifier,
    scope: Any? = null
) {
    when (val operation = node.operation) {
        is RcColumnLayoutOperation -> {
            Column(
                modifier = modifier,
                horizontalAlignment = mapColumnAlignment(operation.horizontalPositioning),
                verticalArrangement = mapColumnArrangement(operation.verticalPositioning, operation.spacedBy)
            ) {
                RemoteComposeRenderer(node.children, context, Modifier, this)
            }
        }

        is RcRowLayoutOperation -> {
            Row(
                modifier = modifier,
                horizontalArrangement = mapRowArrangement(operation.horizontalPositioning, operation.spacedBy),
                verticalAlignment = mapRowAlignment(operation.verticalPositioning)
            ) {
                RemoteComposeRenderer(node.children, context, Modifier, this)
            }
        }

        is RcRootLayoutOperation -> {
            Box(modifier = modifier.fillMaxSize()) {
                RemoteComposeRenderer(node.children, context, Modifier, this)
            }
        }

        is RcLayoutContentOperation -> {
            val drawNodes = node.children.filter { child ->
                child is RcNode.Leaf && child.operation is CanvasScopedOperation
            }
            val contentNodes = node.children.filterNot { child ->
                child is RcNode.Leaf && child.operation is CanvasScopedOperation
            }

            if (contentNodes.isNotEmpty()) {
                RemoteComposeRenderer(contentNodes, context, modifier, scope)
            }

            if (drawNodes.isNotEmpty()) {
                val textMeasurer = rememberTextMeasurer()

                Canvas(modifier = modifier) {
                    val paintState = RcPaintState()
                    val canvas = drawContext.canvas
                    drawNodes.forEach { child ->
                        when (val op = child.operation) {
                            is RcPaintDataOperation -> {
                                applyPaintBundle(op.paintData, paintState, context)
                            }
                            is RcDrawTextAnchoredOperation -> {
                                val resolvedText = context.resolveText(op.textId)
                                val x = context.resolveFloat(op.x)
                                val y = context.resolveFloat(op.y)
                                val panX = context.resolveFloat(op.panX)
                                val panY = context.resolveFloat(op.panY)

                                val textLayoutResult = textMeasurer.measure(
                                    text = resolvedText,
                                    style = TextStyle(
                                        color = paintState.color,
                                        fontSize = paintState.textSize.sp,
                                        fontWeight = FontWeight(
                                            paintState.fontWeight.coerceIn(1, 1000)
                                        ),
                                        fontStyle = if (paintState.isItalic) FontStyle.Italic else FontStyle.Normal
                                    )
                                )

                                val offsetX = x - (textLayoutResult.size.width * panX)
                                val offsetY = y - (textLayoutResult.size.height * panY)

                                drawText(
                                    textLayoutResult = textLayoutResult,
                                    topLeft = Offset(offsetX, offsetY),
                                    color = paintState.color
                                )
                            }
                            is RcDrawCircleOperation -> {
                                val cx = context.resolveFloat(op.centerX)
                                val cy = context.resolveFloat(op.centerY)
                                val rad = context.resolveFloat(op.radius)
                                val brush = paintState.brush

                                if (brush != null) {
                                    drawCircle(
                                        brush = brush,
                                        radius = rad,
                                        center = Offset(cx, cy),
                                        alpha = paintState.alpha,
                                        style = paintState.style,
                                        colorFilter = paintState.colorFilter,
                                        blendMode = paintState.blendMode
                                    )
                                } else {
                                    drawCircle(
                                        color = paintState.color,
                                        radius = rad,
                                        center = Offset(cx, cy),
                                        alpha = paintState.alpha,
                                        style = paintState.style,
                                        colorFilter = paintState.colorFilter,
                                        blendMode = paintState.blendMode
                                    )
                                }
                            }
                            is RcDrawLineOperation -> {
                                val startX = context.resolveFloat(op.startX)
                                val startY = context.resolveFloat(op.startY)
                                val endX = context.resolveFloat(op.endX)
                                val endY = context.resolveFloat(op.endY)
                                val brush = paintState.brush
                                if (brush != null) {
                                    drawLine(
                                        brush = brush,
                                        start = Offset(startX, startY),
                                        end = Offset(endX, endY),
                                        strokeWidth = paintState.strokeWidth,
                                        cap = paintState.strokeCap,
                                        pathEffect = paintState.pathEffect,
                                        alpha = paintState.alpha,
                                        colorFilter = paintState.colorFilter,
                                        blendMode = paintState.blendMode
                                    )
                                } else {
                                    drawLine(
                                        color = paintState.color,
                                        start = Offset(startX, startY),
                                        end = Offset(endX, endY),
                                        strokeWidth = paintState.strokeWidth,
                                        cap = paintState.strokeCap,
                                        pathEffect = paintState.pathEffect,
                                        alpha = paintState.alpha,
                                        colorFilter = paintState.colorFilter,
                                        blendMode = paintState.blendMode
                                    )
                                }
                            }
                            is RcMatrixSaveOperation -> {
                                canvas.save()
                            }
                            is RcMatrixRestoreOperation -> {
                                canvas.restore()
                            }
                            is RcMatrixTranslateOperation -> {
                                val dx = context.resolveFloat(op.dx)
                                val dy = context.resolveFloat(op.dy)
                                canvas.translate(dx, dy)
                            }
                            is RcMatrixScaleOperation -> {
                                val px = context.resolveFloat(op.pivotX)
                                val py = context.resolveFloat(op.pivotY)
                                val sx = context.resolveFloat(op.scaleX)
                                val sy = context.resolveFloat(op.scaleY)
                                canvas.translate(px, py)
                                canvas.scale(sx, sy)
                                canvas.translate(-px, -py)
                            }
                            is RcMatrixRotateOperation -> {
                                val px = context.resolveFloat(op.pivotX)
                                val py = context.resolveFloat(op.pivotY)
                                val rot = context.resolveFloat(op.rotate)
                                canvas.translate(px, py)
                                canvas.rotate(rot)
                                canvas.translate(-px, -py)
                            }
                            is RcMatrixSkewOperation -> {
                                val skewX = context.resolveFloat(op.skewX)
                                val skewY = context.resolveFloat(op.skewY)
                                canvas.skew(skewX, skewY)
                            }
                        }
                    }
                }
            }
        }

        is RcCanvasLayoutOperation -> {
            Box(modifier = modifier) {
                RemoteComposeRenderer(node.children, context, Modifier.fillMaxSize(), this)
            }
        }

        is RcTextLayoutOperation -> {
            RenderText(
                operation = operation,
                context = context,
                modifier = modifier
            )

            if (node.children.isNotEmpty()) {
                RemoteComposeRenderer(
                    nodes = node.children,
                    context = context,
                    modifier = Modifier,
                    scope = scope
                )
            }
        }

        is RcBoxLayoutOperation -> {
            Box(
                modifier = modifier,
                contentAlignment = mapBoxAlignment(operation.horizontalPositioning, operation.verticalPositioning)
            ) {
                RemoteComposeRenderer(node.children, context, Modifier, this)
            }
        }

        else -> {
            Box(modifier = modifier) {
                RemoteComposeRenderer(node.children, context, Modifier, this)
            }
        }
    }
}
