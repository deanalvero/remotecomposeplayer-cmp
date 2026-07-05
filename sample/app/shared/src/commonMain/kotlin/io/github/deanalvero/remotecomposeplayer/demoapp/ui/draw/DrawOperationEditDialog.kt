package io.github.deanalvero.remotecomposeplayer.demoapp.ui.draw

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.deanalvero.remotecomposeplayer.demoapp.ui.field.FloatField
import io.github.deanalvero.remotecomposeplayer.playground.PlaygroundDrawOperation
import io.github.deanalvero.remotecomposeplayer.playground.label

@Composable
fun DrawOperationEditDialog(
    operation: PlaygroundDrawOperation,
    onDismiss: () -> Unit,
    onChange: (PlaygroundDrawOperation) -> Unit,
    onDelete: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Edit ${operation.label()}",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Filled.Close, contentDescription = "Close")
                    }
                }

                HorizontalDivider()

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    when (operation) {
                        is PlaygroundDrawOperation.Circle -> {
                            FloatField(
                                label = "Center X",
                                value = operation.centerX,
                                onValueChange = { onChange(operation.copy(centerX = it)) }
                            )
                            FloatField(
                                label = "Center Y",
                                value = operation.centerY,
                                onValueChange = { onChange(operation.copy(centerY = it)) }
                            )
                            FloatField(
                                label = "Radius",
                                value = operation.radius,
                                onValueChange = { onChange(operation.copy(radius = it)) }
                            )
                        }

                        is PlaygroundDrawOperation.Line -> {
                            FloatField(
                                label = "Start X",
                                value = operation.startX,
                                onValueChange = { onChange(operation.copy(startX = it)) }
                            )
                            FloatField(
                                label = "Start Y",
                                value = operation.startY,
                                onValueChange = { onChange(operation.copy(startY = it)) }
                            )
                            FloatField(
                                label = "End X",
                                value = operation.endX,
                                onValueChange = { onChange(operation.copy(endX = it)) }
                            )
                            FloatField(
                                label = "End Y",
                                value = operation.endY,
                                onValueChange = { onChange(operation.copy(endY = it)) }
                            )
                        }

                        is PlaygroundDrawOperation.Rect -> {
                            FloatField(
                                label = "Left",
                                value = operation.left,
                                onValueChange = { onChange(operation.copy(left = it)) }
                            )
                            FloatField(
                                label = "Top",
                                value = operation.top,
                                onValueChange = { onChange(operation.copy(top = it)) }
                            )
                            FloatField(
                                label = "Right",
                                value = operation.right,
                                onValueChange = { onChange(operation.copy(right = it)) }
                            )
                            FloatField(
                                label = "Bottom",
                                value = operation.bottom,
                                onValueChange = { onChange(operation.copy(bottom = it)) }
                            )
                        }


                        is PlaygroundDrawOperation.Arc -> {
                            FloatField(
                                label = "Left",
                                value = operation.left,
                                onValueChange = { onChange(operation.copy(left = it)) }
                            )
                            FloatField(
                                label = "Top",
                                value = operation.top,
                                onValueChange = { onChange(operation.copy(top = it)) }
                            )
                            FloatField(
                                label = "Right",
                                value = operation.right,
                                onValueChange = { onChange(operation.copy(right = it)) }
                            )
                            FloatField(
                                label = "Bottom",
                                value = operation.bottom,
                                onValueChange = { onChange(operation.copy(bottom = it)) }
                            )
                            FloatField(
                                label = "Start Angle",
                                value = operation.startAngle,
                                onValueChange = { onChange(operation.copy(startAngle = it)) }
                            )
                            FloatField(
                                label = "Sweep Angle",
                                value = operation.sweepAngle,
                                onValueChange = { onChange(operation.copy(sweepAngle = it)) }
                            )
                        }

                        is PlaygroundDrawOperation.Oval -> {
                            FloatField("Left", operation.left) { onChange(operation.copy(left = it)) }
                            FloatField("Top", operation.top) { onChange(operation.copy(top = it)) }
                            FloatField("Right", operation.right) { onChange(operation.copy(right = it)) }
                            FloatField("Bottom", operation.bottom) { onChange(operation.copy(bottom = it)) }
                        }
                        is PlaygroundDrawOperation.Sector -> {
                            FloatField("Left", operation.left) { onChange(operation.copy(left = it)) }
                            FloatField("Top", operation.top) { onChange(operation.copy(top = it)) }
                            FloatField("Right", operation.right) { onChange(operation.copy(right = it)) }
                            FloatField("Bottom", operation.bottom) { onChange(operation.copy(bottom = it)) }
                            FloatField("Start Angle", operation.startAngle) { onChange(operation.copy(startAngle = it)) }
                            FloatField("Sweep Angle", operation.sweepAngle) { onChange(operation.copy(sweepAngle = it)) }
                        }
                        is PlaygroundDrawOperation.RoundRect -> {
                            FloatField("Left", operation.left) { onChange(operation.copy(left = it)) }
                            FloatField("Top", operation.top) { onChange(operation.copy(top = it)) }
                            FloatField("Right", operation.right) { onChange(operation.copy(right = it)) }
                            FloatField("Bottom", operation.bottom) { onChange(operation.copy(bottom = it)) }
                            FloatField("Corner Radius X", operation.rx) { onChange(operation.copy(rx = it)) }
                            FloatField("Corner Radius Y", operation.ry) { onChange(operation.copy(ry = it)) }
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TextButton(onClick = onDelete) { Text("Delete") }
                    }
                }
            }
        }
    }
}
