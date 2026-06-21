package io.github.deanalvero.remotecomposeplayer.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import io.github.deanalvero.remotecomposeplayer.core.RemoteComposeContext
import io.github.deanalvero.remotecomposeplayer.operation.CanvasScopedOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcBoxLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcCanvasLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcColumnLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcDrawCircleOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcDrawLineOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcLayoutContentOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcRootLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcRowLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcTextLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.mapBoxAlignment

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
                Canvas(modifier = modifier) {
                    drawNodes.forEach { child ->
                        when (val op = child.operation) {
                            is RcDrawCircleOperation -> {
                                drawCircle(
                                    color = Color.Black,
                                    radius = op.radius,
                                    center = Offset(op.centerX, op.centerY)
                                )
                            }

                            is RcDrawLineOperation -> {
                                drawLine(
                                    color = Color.Black,
                                    start = Offset(op.startX, op.startY),
                                    end = Offset(op.endX, op.endY)
                                )
                            }
                        }
                    }
                }
            }
        }

        is RcCanvasLayoutOperation -> {
            Box(modifier = modifier) {
                RemoteComposeRenderer(node.children, context, Modifier, this)
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