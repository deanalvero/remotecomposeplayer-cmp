package io.github.deanalvero.remotecomposeplayer.demoapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.deanalvero.remotecomposeplayer.demoapp.FileUploader
import io.github.deanalvero.remotecomposeplayer.playground.PlaygroundByteBuilder
import io.github.deanalvero.remotecomposeplayer.playground.PlaygroundComponentKind
import io.github.deanalvero.remotecomposeplayer.playground.PlaygroundDocumentState
import io.github.deanalvero.remotecomposeplayer.playground.PlaygroundNode

@Composable
fun PlaygroundEditor(
    document: PlaygroundDocumentState,
    onAddRoot: (PlaygroundComponentKind) -> Unit,
    onSelectNode: (String?) -> Unit,
    onAddChild: (String, PlaygroundComponentKind) -> Unit,
    onUpdateNode: (String, (PlaygroundNode) -> PlaygroundNode) -> Unit,
    onDeleteNode: (String) -> Unit,
    onDownload: (ByteArray) -> Unit,
    onUpload: (ByteArray) -> Unit,
    modifier: Modifier = Modifier
) {
    val bytes = remember(document) {
        PlaygroundByteBuilder.serialize(document)
    }
    val selectedNode = remember(document) {
        document.findNode(document.selectedId)
    }
    var showUploader by remember { mutableStateOf(false) }
    FileUploader(
        show = showUploader,
        onDismiss = { showUploader = false },
        onFileSelected = onUpload
    )

    BoxWithConstraints(modifier = modifier.padding(16.dp)) {
        val wide = maxWidth >= 800.dp

        if (wide) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                EditorSidebar(
                    modifier = Modifier.weight(0.38f),
                    document = document,
                    onAddRoot = onAddRoot,
                    onSelectNode = onSelectNode,
                    onAddChild = onAddChild,
                    onDeleteNode = onDeleteNode,
                    onDownload = { onDownload(bytes) },
                    onUpload = { showUploader = true }
                )

                PreviewPane(
                    bytes = bytes,
                    modifier = Modifier.weight(0.62f)
                )
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                EditorSidebar(
                    modifier = Modifier.fillMaxWidth(),
                    document = document,
                    onAddRoot = onAddRoot,
                    onSelectNode = onSelectNode,
                    onAddChild = onAddChild,
                    onDeleteNode = onDeleteNode,
                    onDownload = { onDownload(bytes) },
                    onUpload = { showUploader = true }
                )

                PreviewPane(
                    bytes = bytes,
                    modifier = Modifier.fillMaxWidth().weight(1f)
                )
            }
        }
    }

    selectedNode?.let { node ->
        InspectorDialog(
            node = node,
            onDismiss = { onSelectNode(null) },
            onChange = { updated ->
                onUpdateNode(node.id) { updated }
            },
            onDelete = {
                onDeleteNode(node.id)
                onSelectNode(null)
            }
        )
    }
}