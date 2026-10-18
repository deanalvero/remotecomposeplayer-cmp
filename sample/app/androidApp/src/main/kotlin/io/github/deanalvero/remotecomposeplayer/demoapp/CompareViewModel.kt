package io.github.deanalvero.remotecomposeplayer.demoapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.deanalvero.remotecomposeplayer.demoapp.examples.Example
import io.github.deanalvero.remotecomposeplayer.demoapp.examples.ExampleCatalog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CompareViewModel : ViewModel() {
    val documents = ExampleCatalog.all.filterIsInstance<Example.Document>()

    private val _uiState = MutableStateFlow<CompareUiState>(CompareUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        val initial = documents.firstOrNull()
        if (initial != null) {
            selectDocument(initial)
        } else {
            _uiState.update { CompareUiState.Error("No documents available in ExampleCatalog") }
        }
    }

    fun selectDocument(document: Example.Document) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _uiState.update { CompareUiState.Loading }
                val bytes = ExampleCatalog.bytesFor(document)
                _uiState.update {
                    CompareUiState.Loaded(
                        selectedDocument = document,
                        data = bytes
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    CompareUiState.Error(e.message)
                }
            }
        }
    }
}