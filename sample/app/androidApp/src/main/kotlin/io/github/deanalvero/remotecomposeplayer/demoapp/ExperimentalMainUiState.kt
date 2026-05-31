package io.github.deanalvero.remotecomposeplayer.demoapp

sealed class ExperimentalMainUiState {
    data object Loading : ExperimentalMainUiState()
    data class Loaded(val data: ByteArray) : ExperimentalMainUiState()
    data class Error(val message: String?) : ExperimentalMainUiState()
}