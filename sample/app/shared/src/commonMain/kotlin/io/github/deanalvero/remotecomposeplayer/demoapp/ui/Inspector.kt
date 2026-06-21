package io.github.deanalvero.remotecomposeplayer.demoapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import io.github.deanalvero.remotecomposeplayer.demoapp.ui.draw.DrawOperationSection
import io.github.deanalvero.remotecomposeplayer.demoapp.ui.field.ColorField
import io.github.deanalvero.remotecomposeplayer.demoapp.ui.field.FloatField
import io.github.deanalvero.remotecomposeplayer.demoapp.ui.field.IntChoiceField
import io.github.deanalvero.remotecomposeplayer.demoapp.ui.field.IntField
import io.github.deanalvero.remotecomposeplayer.demoapp.ui.modifier.ModifierSection
import io.github.deanalvero.remotecomposeplayer.operation.RcBoxLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcRowLayoutOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcTextLayoutOperation
import io.github.deanalvero.remotecomposeplayer.playground.PlaygroundModifier
import io.github.deanalvero.remotecomposeplayer.playground.PlaygroundNode
import io.github.deanalvero.remotecomposeplayer.playground.defaultDrawOperation
import io.github.deanalvero.remotecomposeplayer.playground.defaultModifier

@Composable
fun Inspector(
    node: PlaygroundNode,
    onChange: (PlaygroundNode) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("Editing ${nodeLabel(node)}", style = MaterialTheme.typography.titleSmall)

        when (node) {
            is PlaygroundNode.Column -> {
                IntChoiceField(
                    label = "Horizontal",
                    value = node.horizontal,
                    options = listOf(
                        RcRowLayoutOperation.START to "START",
                        RcRowLayoutOperation.CENTER to "CENTER",
                        RcRowLayoutOperation.END to "END",
                        RcRowLayoutOperation.SPACE_BETWEEN to "SPACE_BETWEEN",
                        RcRowLayoutOperation.SPACE_EVENLY to "SPACE_EVENLY",
                        RcRowLayoutOperation.SPACE_AROUND to "SPACE_AROUND"
                    ),
                    onValueChange = { onChange(node.copy(horizontal = it)) }
                )
                IntChoiceField(
                    label = "Vertical",
                    value = node.vertical,
                    options = listOf(
                        RcRowLayoutOperation.TOP to "TOP",
                        RcRowLayoutOperation.CENTER to "CENTER",
                        RcRowLayoutOperation.BOTTOM to "BOTTOM",
                        RcRowLayoutOperation.SPACE_BETWEEN to "SPACE_BETWEEN",
                        RcRowLayoutOperation.SPACE_EVENLY to "SPACE_EVENLY",
                        RcRowLayoutOperation.SPACE_AROUND to "SPACE_AROUND"
                    ),
                    onValueChange = { onChange(node.copy(vertical = it)) }
                )
                FloatField("Spaced by", node.spacedBy) { onChange(node.copy(spacedBy = it)) }
            }

            is PlaygroundNode.Row -> {
                IntChoiceField(
                    label = "Horizontal",
                    value = node.horizontal,
                    options = listOf(
                        RcRowLayoutOperation.START to "START",
                        RcRowLayoutOperation.CENTER to "CENTER",
                        RcRowLayoutOperation.END to "END",
                        RcRowLayoutOperation.SPACE_BETWEEN to "SPACE_BETWEEN",
                        RcRowLayoutOperation.SPACE_EVENLY to "SPACE_EVENLY",
                        RcRowLayoutOperation.SPACE_AROUND to "SPACE_AROUND"
                    ),
                    onValueChange = { onChange(node.copy(horizontal = it)) }
                )
                IntChoiceField(
                    label = "Vertical",
                    value = node.vertical,
                    options = listOf(
                        RcRowLayoutOperation.TOP to "TOP",
                        RcRowLayoutOperation.CENTER to "CENTER",
                        RcRowLayoutOperation.BOTTOM to "BOTTOM",
                        RcRowLayoutOperation.SPACE_BETWEEN to "SPACE_BETWEEN",
                        RcRowLayoutOperation.SPACE_EVENLY to "SPACE_EVENLY",
                        RcRowLayoutOperation.SPACE_AROUND to "SPACE_AROUND"
                    ),
                    onValueChange = { onChange(node.copy(vertical = it)) }
                )
                FloatField("Spaced by", node.spacedBy) { onChange(node.copy(spacedBy = it)) }
            }

            is PlaygroundNode.Box -> {
                IntChoiceField(
                    label = "Horizontal",
                    value = node.horizontal,
                    options = listOf(
                        RcBoxLayoutOperation.START to "START",
                        RcBoxLayoutOperation.CENTER to "CENTER",
                        RcBoxLayoutOperation.END to "END"
                    ),
                    onValueChange = { onChange(node.copy(horizontal = it)) }
                )
                IntChoiceField(
                    label = "Vertical",
                    value = node.vertical,
                    options = listOf(
                        RcBoxLayoutOperation.TOP to "TOP",
                        RcBoxLayoutOperation.CENTER to "CENTER",
                        RcBoxLayoutOperation.BOTTOM to "BOTTOM"
                    ),
                    onValueChange = { onChange(node.copy(vertical = it)) }
                )
            }

            is PlaygroundNode.Canvas -> {
                Text(
                    text = "Canvas draw operations are edited from the tree item.",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            is PlaygroundNode.Text -> {
                TextFieldValueEditor("Text", node.text) { onChange(node.copy(text = it)) }
                ColorField("Color ARGB", node.color) { onChange(node.copy(color = it)) }
                FloatField("Font size", node.fontSize) { onChange(node.copy(fontSize = it)) }
                FloatField("Font weight", node.fontWeight) { onChange(node.copy(fontWeight = it)) }
                IntChoiceField(
                    label = "Text align",
                    value = node.textAlign,
                    options = listOf(
                        RcTextLayoutOperation.TEXT_ALIGN_START to "START",
                        RcTextLayoutOperation.TEXT_ALIGN_LEFT to "LEFT",
                        RcTextLayoutOperation.TEXT_ALIGN_RIGHT to "RIGHT",
                        RcTextLayoutOperation.TEXT_ALIGN_CENTER to "CENTER",
                        RcTextLayoutOperation.TEXT_ALIGN_JUSTIFY to "JUSTIFY",
                        RcTextLayoutOperation.TEXT_ALIGN_END to "END"
                    ),
                    onValueChange = { onChange(node.copy(textAlign = it)) }
                )
                IntChoiceField(
                    label = "Overflow",
                    value = node.overflow,
                    options = listOf(
                        RcTextLayoutOperation.OVERFLOW_CLIP to "CLIP",
                        RcTextLayoutOperation.OVERFLOW_VISIBLE to "VISIBLE",
                        RcTextLayoutOperation.OVERFLOW_ELLIPSIS to "ELLIPSIS",
                        RcTextLayoutOperation.OVERFLOW_START_ELLIPSIS to "START_ELLIPSIS",
                        RcTextLayoutOperation.OVERFLOW_MIDDLE_ELLIPSIS to "MIDDLE_ELLIPSIS"
                    ),
                    onValueChange = { onChange(node.copy(overflow = it)) }
                )
                IntField("Max lines", node.maxLines) { onChange(node.copy(maxLines = it.coerceAtLeast(1))) }
                IntField("Font style", node.fontStyle) { onChange(node.copy(fontStyle = it)) }
                IntField("Font family id", node.fontFamilyId) { onChange(node.copy(fontFamilyId = it)) }
            }
            is PlaygroundNode.Spacer -> {
                Text(
                    text = "Spacer has no layout properties of its own. Add a Width modifier below to give it size.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Text("Modifiers", style = MaterialTheme.typography.titleMedium)
        ModifierSection(
            modifiers = node.modifiers,
            onAddModifier = { kind ->
                val updated = node.withModifier(node.modifiers + defaultModifier(kind))
                onChange(updated)
            },
            onUpdateModifier = { index, updatedModifier ->
                onChange(node.replaceModifier(index, updatedModifier))
            },
            onDeleteModifier = { index ->
                onChange(node.removeModifier(index))
            }
        )

        if (node is PlaygroundNode.Canvas) {
            Text("Draw operations", style = MaterialTheme.typography.titleMedium)
            DrawOperationSection(
                operations = node.drawOperations,
                onAddOperation = { kind ->
                    onChange(node.copy(drawOperations = node.drawOperations + defaultDrawOperation(kind)))
                },
                onUpdateOperation = { index, updated ->
                    onChange(
                        node.copy(
                            drawOperations = node.drawOperations.toMutableList().also { it[index] = updated }
                        )
                    )
                },
                onDeleteOperation = { index ->
                    onChange(
                        node.copy(
                            drawOperations = node.drawOperations.toMutableList().also { it.removeAt(index) }
                        )
                    )
                }
            )
        }
    }
}

private fun PlaygroundNode.withModifier(updated: List<PlaygroundModifier>): PlaygroundNode {
    return when (this) {
        is PlaygroundNode.Column -> copy(modifiers = updated)
        is PlaygroundNode.Row -> copy(modifiers = updated)
        is PlaygroundNode.Box -> copy(modifiers = updated)
        is PlaygroundNode.Spacer -> copy(modifiers = updated)
        is PlaygroundNode.Canvas -> copy(modifiers = updated)
        is PlaygroundNode.Text -> copy(modifiers = updated)
    }
}

fun PlaygroundNode.addModifier(mod: PlaygroundModifier): PlaygroundNode =
    updateModifiers(modifiers + mod)

fun PlaygroundNode.replaceModifier(index: Int, updated: PlaygroundModifier): PlaygroundNode {
    val newModifiers = modifiers.toMutableList().apply { this[index] = updated }
    return withModifier(newModifiers)
}

fun PlaygroundNode.removeModifier(index: Int): PlaygroundNode {
    val newModifiers = modifiers.toMutableList().apply { removeAt(index) }
    return withModifier(newModifiers)
}

private fun PlaygroundNode.updateModifiers(new: List<PlaygroundModifier>): PlaygroundNode =
    when (this) {
        is PlaygroundNode.Column -> copy(modifiers = new)
        is PlaygroundNode.Row -> copy(modifiers = new)
        is PlaygroundNode.Box -> copy(modifiers = new)
        is PlaygroundNode.Spacer -> copy(modifiers = new)
        is PlaygroundNode.Canvas -> copy(modifiers = new)
        is PlaygroundNode.Text -> copy(modifiers = new)
    }
