package io.github.deanalvero.remotecomposeplayer.demoapp.ui.modifier

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.deanalvero.remotecomposeplayer.demoapp.ui.field.ColorField
import io.github.deanalvero.remotecomposeplayer.demoapp.ui.field.FloatField
import io.github.deanalvero.remotecomposeplayer.demoapp.ui.field.IntChoiceField
import io.github.deanalvero.remotecomposeplayer.operation.RcBackgroundModifierOperation
import io.github.deanalvero.remotecomposeplayer.operation.common.RcDimensionType
import io.github.deanalvero.remotecomposeplayer.playground.PlaygroundModifier
import io.github.deanalvero.remotecomposeplayer.playground.label

@Composable
fun ModifierItemCard(
    modifier: PlaygroundModifier,
    onChange: (PlaygroundModifier) -> Unit,
    onDelete: () -> Unit
) {
    Card {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(modifier.label())
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete modifier")
                }
            }

            when (modifier) {
                is PlaygroundModifier.Padding -> {
                    FloatField("Left", modifier.left) { onChange(modifier.copy(left = it)) }
                    FloatField("Top", modifier.top) { onChange(modifier.copy(top = it)) }
                    FloatField("Right", modifier.right) { onChange(modifier.copy(right = it)) }
                    FloatField("Bottom", modifier.bottom) { onChange(modifier.copy(bottom = it)) }
                }

                is PlaygroundModifier.Background -> {
                    ColorField("Background color", modifier.argb) { onChange(modifier.copy(argb = it)) }
                    IntChoiceField(
                        label = "Shape",
                        value = modifier.shapeType,
                        options = listOf(
                            RcBackgroundModifierOperation.SHAPE_RECTANGLE to "Rectangle",
                            RcBackgroundModifierOperation.SHAPE_CIRCLE to "Circle"
                        ),
                        onValueChange = { onChange(modifier.copy(shapeType = it)) }
                    )
                }

                is PlaygroundModifier.Width -> {
                    IntChoiceField(
                        label = "Width strategy",
                        value = modifier.typeId,
                        options = listOf(
                            RcDimensionType.EXACT.id to "EXACT",
                            RcDimensionType.FILL.id to "FILL",
                            RcDimensionType.WRAP.id to "WRAP",
                            RcDimensionType.WEIGHT.id to "WEIGHT",
                            RcDimensionType.EXACT_DP.id to "EXACT_DP",
                            RcDimensionType.FILL_PARENT_MAX_WIDTH.id to "FILL_PARENT_MAX_WIDTH"
                        ),
                        onValueChange = { onChange(modifier.copy(typeId = it)) }
                    )
                    FloatField("Value", modifier.value) { onChange(modifier.copy(value = it)) }
                }
            }
        }
    }
}