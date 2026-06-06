package io.github.deanalvero.remotecomposeplayer.demoapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.deanalvero.remotecomposeplayer.playground.PlaygroundComponentKind

@Composable
fun ComponentAddMenu(
    buttonLabel: String = "Add child",
    onAdd: (PlaygroundComponentKind) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Button(onClick = { expanded = true }) {
        Text(buttonLabel)
    }

    if (expanded) {
        Dialog(onDismissRequest = { expanded = false }) {
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(buttonLabel)

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = {
                            expanded = false
                            onAdd(PlaygroundComponentKind.Column)
                        }) { Text("Column") }

                        Button(onClick = {
                            expanded = false
                            onAdd(PlaygroundComponentKind.Row)
                        }) { Text("Row") }

                        Button(onClick = {
                            expanded = false
                            onAdd(PlaygroundComponentKind.Text)
                        }) { Text("Text") }
                    }
                }
            }
        }
    }
}