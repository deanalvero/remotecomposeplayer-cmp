package io.github.deanalvero.remotecomposeplayer.demoapp

sealed class CompareUiState {
    data object Loading : CompareUiState()
    data class Loaded(val data: ByteArray) : CompareUiState()
    data class Error(val message: String?) : CompareUiState()
}