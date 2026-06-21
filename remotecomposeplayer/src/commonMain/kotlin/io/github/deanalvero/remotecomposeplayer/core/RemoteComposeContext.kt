package io.github.deanalvero.remotecomposeplayer.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import io.github.deanalvero.remotecomposeplayer.clock.KtxRemoteClock
import io.github.deanalvero.remotecomposeplayer.clock.RemoteClock
import io.github.deanalvero.remotecomposeplayer.format
import io.github.deanalvero.remotecomposeplayer.operation.RcTextDataOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcTextFromFloatOperation

@Immutable
class RemoteComposeContext(
    operations: List<RcOperation>,
    val colors: Map<Int, Int> = emptyMap()
) {
    private val clock: RemoteClock = KtxRemoteClock()

    private val textRegistry: Map<Int, String> = operations
        .filterIsInstance<RcTextDataOperation>()
        .associate { it.textId to it.text }

    private val textFromFloatRegistry: Map<Int, RcTextFromFloatOperation> = operations
        .filterIsInstance<RcTextFromFloatOperation>()
        .associate { it.textId to it }

    @Composable
    fun resolveText(textId: Int): String {
        textRegistry[textId]?.let { return it }

        textFromFloatRegistry[textId]?.let { op ->
            val floatValue = extractDynamicFloat(op.value, clock)
            return floatValue.format(op)
        }
        return "Error: Text ID [$textId] not found"
    }

    @Composable
    private fun extractDynamicFloat(rawValue: Float, clock: RemoteClock): Float {
        if (!rawValue.isNaN()) return rawValue

        val variableId = rawValue.toRawBits() and 0x3FFFFF

        return when (variableId) {
            ID_CONTINUOUS_SEC -> {
                var currentSeconds by remember {
                    mutableFloatStateOf(clock.snapshot().continuousSeconds)
                }
                LaunchedEffect(clock) {
                    while (true) {
                        withFrameNanos {
                            currentSeconds = clock.snapshot().continuousSeconds
                        }
                    }
                }
                currentSeconds % 3600f
            }
            else -> 0f
        }
    }

    fun getStaticText(textId: Int): String {
        textRegistry[textId]?.let { return it }
        textFromFloatRegistry[textId]?.let { op ->
            return if (op.value.isNaN()) {
                val variableId = op.value.toRawBits() and 0x3FFFFF
                "{Dynamic Float ID: $variableId}"
            } else {
                op.value.toString()
            }
        }
        return "Error: Text ID [$textId] not found"
    }

    fun getColor(colorId: Int): Int {
        val themeColor = colors[colorId]
        if (themeColor != null) return themeColor
        return 0xFFFF00FF.toInt()
    }

    companion object {
        private const val ID_CONTINUOUS_SEC = 1
    }
}