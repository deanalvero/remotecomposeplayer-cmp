package io.github.deanalvero.remotecomposeplayer.demoapp

sealed class RemoteSourceUiState {
    data object Loading : RemoteSourceUiState()
    data class Loaded(val data: ByteArray) : RemoteSourceUiState()
    data class Error(val message: String?) : RemoteSourceUiState()
}