package io.github.deanalvero.remotecomposeplayer.demoapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import io.github.deanalvero.remotecomposeplayer.playground.PlaygroundComponentKind

@Composable
fun ComponentAddMenu(
    onAdd: (PlaygroundComponentKind) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(onClick = { expanded = true }) { Text("Add child") }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Column") },
                onClick = {
                    expanded = false
                    onAdd(PlaygroundComponentKind.Column)
                }
            )
            DropdownMenuItem(
                text = { Text("Row") },
                onClick = {
                    expanded = false
                    onAdd(PlaygroundComponentKind.Row)
                }
            )
            DropdownMenuItem(
                text = { Text("Text") },
                onClick = {
                    expanded = false
                    onAdd(PlaygroundComponentKind.Text)
                }
            )
        }
    }
}