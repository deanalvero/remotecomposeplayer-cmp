package io.github.deanalvero.remotecomposeplayer.core

import androidx.compose.runtime.Immutable
import io.github.deanalvero.remotecomposeplayer.operation.RcTextDataOperation

@Immutable
class RemoteComposeContext(
    operations: List<RcOperation>,
    val colors: Map<Int, Int> = emptyMap()
) {
    private val textRegistry: Map<Int, String> = operations
        .filterIsInstance<RcTextDataOperation>()
        .associate { it.textId to it.text }

    fun getText(textId: Int): String {
        return textRegistry[textId] ?: "Error: Text ID [$textId] not found"
    }

    fun getColor(colorId: Int): Int {
        val themeColor = colors[colorId]
        if (themeColor != null) return themeColor

        return 0xFFFF00FF.toInt()
    }
}
