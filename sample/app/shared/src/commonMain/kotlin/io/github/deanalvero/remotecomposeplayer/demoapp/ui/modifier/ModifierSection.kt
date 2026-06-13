package io.github.deanalvero.remotecomposeplayer.demoapp.ui.modifier

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import io.github.deanalvero.remotecomposeplayer.playground.PlaygroundModifier
import io.github.deanalvero.remotecomposeplayer.playground.PlaygroundModifierKind

@Composable
fun ModifierSection(
    modifiers: List<PlaygroundModifier>,
    onAddModifier: (PlaygroundModifierKind) -> Unit,
    onUpdateModifier: (index: Int, updated: PlaygroundModifier) -> Unit,
    onDeleteModifier: (index: Int) -> Unit
) {
    var showPicker by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        if (modifiers.isEmpty()) {
            Text("No modifiers applied.")
        } else {
            modifiers.forEachIndexed { index, modifier ->
                ModifierItemCard(
                    modifier = modifier,
                    onChange = { updated -> onUpdateModifier(index, updated) },
                    onDelete = { onDeleteModifier(index) }
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { showPicker = true }) {
                Icon(Icons.Filled.Add, contentDescription = null)
                Text("Add modifier")
            }
        }
    }

    if (showPicker) {
        ModifierPickerDialog(
            onDismiss = { showPicker = false },
            onPick = {
                showPicker = false
                onAddModifier(it)
            }
        )
    }
}