package io.github.deanalvero.remotecomposeplayer.demoapp

import io.github.deanalvero.remotecomposeplayer.playground.PlaygroundDocumentState

sealed interface MainUiState {
    data class Playground(val document: PlaygroundDocumentState) : MainUiState
    data class Error(val message: String) : MainUiState
}
