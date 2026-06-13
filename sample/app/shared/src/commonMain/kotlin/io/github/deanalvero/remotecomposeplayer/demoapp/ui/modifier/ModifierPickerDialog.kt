package io.github.deanalvero.remotecomposeplayer.demoapp.ui.modifier

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Add modifier")
                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Filled.Close, contentDescription = "Close Modifier Adder")
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { onPick(PlaygroundModifierKind.Padding) }) { Text("Padding") }
                    Button(onClick = { onPick(PlaygroundModifierKind.Background) }) { Text("Background") }
                    Button(onClick = { onPick(PlaygroundModifierKind.Width) }) { Text("Width") }
                }
            }
        }
    }
}
