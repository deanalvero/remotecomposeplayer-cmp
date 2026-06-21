package io.github.deanalvero.remotecomposeplayer.demoapp.ui.draw

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.github.deanalvero.remotecomposeplayer.playground.PlaygroundDrawOperation
import io.github.deanalvero.remotecomposeplayer.playground.PlaygroundDrawOperationKind

@Composable
fun DrawOperationSection(
    operations: List<PlaygroundDrawOperation>,
    onAddOperation: (PlaygroundDrawOperationKind) -> Unit,
    onUpdateOperation: (index: Int, updated: PlaygroundDrawOperation) -> Unit,
    onDeleteOperation: (index: Int) -> Unit
) {
    var showPicker by remember { mutableStateOf(false) }
    var editingIndex by remember { mutableStateOf<Int?>(null) }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        if (operations.isEmpty()) {
            Text("No draw operations.")
        } else {
            operations.forEachIndexed { index, operation ->
                DrawOperationChip(
                    operation = operation,
                    onClick = { editingIndex = index },
                    onDelete = { onDeleteOperation(index) }
                )
            }
        }

        OutlinedButton(onClick = { showPicker = true }) {
            Icon(
                Icons.Filled.Add,
                contentDescription = null,
                modifier = Modifier.size(14.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text("Add draw operation", style = MaterialTheme.typography.bodySmall)
        }
    }

    if (showPicker) {
        DrawOperationPickerDialog(
            onDismiss = { showPicker = false },
            onPick = { kind ->
                val newIndex = operations.size
                showPicker = false
                onAddOperation(kind)
                editingIndex = newIndex
            }
        )
    }

    editingIndex?.let { index ->
        operations.getOrNull(index)?.let { op ->
            DrawOperationEditDialog(
                operation = op,
                onDismiss = { editingIndex = null },
                onChange = { updated ->
                    onUpdateOperation(index, updated)
                },
                onDelete = {
                    onDeleteOperation(index)
                    editingIndex = null
                }
            )
        }
    }
}

