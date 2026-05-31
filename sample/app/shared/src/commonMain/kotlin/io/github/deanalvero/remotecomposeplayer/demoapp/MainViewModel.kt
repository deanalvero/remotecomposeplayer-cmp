package io.github.deanalvero.remotecomposeplayer.demoapp

import io.github.deanalvero.remotecomposeplayer.playground.PlaygroundByteBuilder
import io.github.deanalvero.remotecomposeplayer.playground.PlaygroundComponentKind
import io.github.deanalvero.remotecomposeplayer.playground.PlaygroundDocumentState
import io.github.deanalvero.remotecomposeplayer.playground.PlaygroundNode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(
    initialDocument: PlaygroundDocumentState = PlaygroundDocumentState.empty()
) {
    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Playground(initialDocument))
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val document: PlaygroundDocumentState
        get() = (_uiState.value as? MainUiState.Playground)?.document ?: PlaygroundDocumentState.empty()

    fun reload() {
        _uiState.value = MainUiState.Playground(PlaygroundDocumentState.empty())
    }

    fun selectNode(id: String?) {
        setDocument(document.select(id))
    }

    fun addRoot(kind: PlaygroundComponentKind) {
        setDocument(document.addRoot(kind))
    }

    fun addChild(parentId: String, kind: PlaygroundComponentKind) {
        setDocument(document.addChild(parentId, kind))
    }

    fun updateNode(nodeId: String, transform: (PlaygroundNode) -> PlaygroundNode) {
        setDocument(document.updateNode(nodeId, transform))
    }

    fun deleteNode(nodeId: String) {
        setDocument(document.deleteNode(nodeId))
    }

    fun currentBytes(): ByteArray = PlaygroundByteBuilder.serialize(document)

    private fun setDocument(updated: PlaygroundDocumentState) {
        _uiState.value = MainUiState.Playground(updated)
    }
}
