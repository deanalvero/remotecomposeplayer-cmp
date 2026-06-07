package io.github.deanalvero.remotecomposeplayer.demoapp.ui.modifier

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.deanalvero.remotecomposeplayer.playground.PlaygroundModifierKind

@Composable
fun ModifierPickerDialog(
    onDismiss: () -> Unit,
    onPick: (PlaygroundModifierKind) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Add modifier")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { onPick(PlaygroundModifierKind.Padding) }) { Text("Padding") }
                    Button(onClick = { onPick(PlaygroundModifierKind.Background) }) { Text("Background") }
                    Button(onClick = { onPick(PlaygroundModifierKind.Width) }) { Text("Width") }
                }
            }
        }
    }
}

