package io.github.deanalvero.remotecomposeplayer.demoapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.deanalvero.remotecomposeplayer.playground.PlaygroundComponentKind
import io.github.deanalvero.remotecomposeplayer.playground.PlaygroundNode

@Composable
fun NodeRow(
    node: PlaygroundNode,
    depth: Int,
    selectedId: String?,
    onSelect: (String?) -> Unit,
    onAddChild: (String, PlaygroundComponentKind) -> Unit,
    onDelete: (String) -> Unit
) {
    val selected = node.id == selectedId

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (selected) {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    } else {
                        Color.Transparent
                    }
                )
                .clickable { onSelect(node.id) }
                .padding(start = (depth * 14).dp, top = 8.dp, bottom = 8.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(nodeLabel(node), fontWeight = FontWeight.SemiBold)
                Text(
                    text = "id=${node.id}, component=${node.componentId}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
            if (node is PlaygroundNode.Column || node is PlaygroundNode.Row) {
                ComponentAddMenu(
                    buttonLabel = "Add child",
                    onAdd = { onAddChild(node.id, it) }
                )
                Spacer(Modifier.width(8.dp))
            }
            Button(onClick = { onDelete(node.id) }) { Text("Delete") }
        }

        when (node) {
            is PlaygroundNode.Column -> node.children.forEach { child ->
                NodeRow(child, depth + 1, selectedId, onSelect, onAddChild, onDelete)
            }
            is PlaygroundNode.Row -> node.children.forEach { child ->
                NodeRow(child, depth + 1, selectedId, onSelect, onAddChild, onDelete)
            }
            is PlaygroundNode.Text -> Unit
        }
    }
}