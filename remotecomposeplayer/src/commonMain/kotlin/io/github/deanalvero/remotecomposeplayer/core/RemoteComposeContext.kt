package io.github.deanalvero.remotecomposeplayer.core

import androidx.compose.runtime.Immutable
import io.github.deanalvero.remotecomposeplayer.operation.RcTextDataOperation

@Immutable
class RemoteComposeContext(operations: List<RcOperation>) {
    private val textRegistry: Map<Int, String> = operations
        .filterIsInstance<RcTextDataOperation>()
        .associate { it.textId to it.text }

    fun getText(textId: Int): String {
        return textRegistry[textId] ?: "Error: Text ID [$textId] not found"
    }
}
