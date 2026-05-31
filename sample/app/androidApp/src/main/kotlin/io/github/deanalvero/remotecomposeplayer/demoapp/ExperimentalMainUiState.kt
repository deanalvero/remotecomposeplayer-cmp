package io.github.deanalvero.remotecomposeplayer.demoapp

sealed class MainUiState {
    data object Loading : MainUiState()
    data class Loaded(val data: ByteArray) : MainUiState()
    data class Error(val message: String?) : MainUiState()
}