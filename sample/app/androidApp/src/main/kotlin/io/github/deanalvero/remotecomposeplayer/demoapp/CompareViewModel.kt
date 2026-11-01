package io.github.deanalvero.remotecomposeplayer.demoapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.deanalvero.remotecomposeplayer.demoapp.examples.Example
import io.github.deanalvero.remotecomposeplayer.demoapp.examples.ExampleCatalog
import io.github.deanalvero.remotecomposeplayer.demoapp.examples.remoteSourceBaseUrl
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsBytes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CompareViewModel : ViewModel() {
    val items: List<CompareItem> = ExampleCatalog.all
        .filterIsInstance<Example.Document>()
        .map { CompareItem.Document(it) } + CompareItem.RemoteSource

    private val _uiState = MutableStateFlow<CompareUiState>(CompareUiState.Loading)
    val uiState = _uiState.asStateFlow()
    private var currentItem: CompareItem? = null

    init {
        load()
    }

    fun load() {
        val initial = currentItem ?: items.firstOrNull()
        if (initial != null) {
            selectItem(initial)
        } else {
            _uiState.update { CompareUiState.Error("No documents available in ExampleCatalog") }
        }
    }

    fun selectItem(item: CompareItem) {
        currentItem = item
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _uiState.update { CompareUiState.Loading }
                val bytes = when (item) {
                    is CompareItem.Document -> ExampleCatalog.bytesFor(item.example)
                    is CompareItem.RemoteSource -> {
                        val client = HttpClient()
                        try {
                            val response = client.get(remoteSourceBaseUrl())
                            response.bodyAsBytes()
                        } finally {
                            client.close()
                        }
                    }
                }
                _uiState.update {
                    CompareUiState.Loaded(
                        selectedItem = item,
                        data = bytes
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    CompareUiState.Error(e.message, isRemoteSource = item is CompareItem.RemoteSource)
                }
            }
        }
    }
}