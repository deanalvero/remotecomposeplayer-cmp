package io.github.deanalvero.remotecomposeplayer.demoapp

import io.github.deanalvero.remotecomposeplayer.demoapp.examples.Example

sealed class CompareUiState {
    data object Loading : CompareUiState()
    data class Loaded(
        val selectedItem: CompareItem,
        val data: ByteArray
    ) : CompareUiState()data class Error(val message: String?, val isRemoteSource: Boolean = false) : CompareUiState()
}