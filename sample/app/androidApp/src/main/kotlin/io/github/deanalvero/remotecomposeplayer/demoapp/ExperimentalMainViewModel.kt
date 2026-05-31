package io.github.deanalvero.remotecomposeplayer.demoapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsBytes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val client = HttpClient()

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = client.get("http://10.0.2.2:8080/")
                println(response)
                _uiState.update {
                    MainUiState.Loaded(response.bodyAsBytes())
                }
            } catch (e: Exception) {
                _uiState.update {
                    MainUiState.Error(e.message)
                }
            }
        }
    }
}
