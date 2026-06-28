package io.github.deanalvero.remotecomposeplayer.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import io.github.deanalvero.remotecomposeplayer.clock.KtxRemoteClock
import io.github.deanalvero.remotecomposeplayer.clock.RemoteClock
import io.github.deanalvero.remotecomposeplayer.format
import io.github.deanalvero.remotecomposeplayer.operation.RcColorConstantOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcColorExpressionOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcComponentValueOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcDataListIdsOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcFloatExpressionOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcNamedVariableOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcTextDataOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcTextFromFloatOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcTextLookupOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcTextMergeOperation
import io.github.deanalvero.remotecomposeplayer.operation.RcThemeOperation

@Immutable
class RemoteComposeContext(
    operations: List<RcOperation>,
    val isDarkTheme: Boolean = true,
    val colors: Map<Int, Int> = emptyMap()
) {
    private val clock: RemoteClock = KtxRemoteClock()

    private val textRegistry: Map<Int, String> = operations
        .filterIsInstance<RcTextDataOperation>()
        .associate { it.textId to it.text }

    private val textFromFloatRegistry: Map<Int, RcTextFromFloatOperation> = operations
        .filterIsInstance<RcTextFromFloatOperation>()
        .associate { it.textId to it }

    private val floatVariables = mutableStateMapOf<Int, Float>()

    val namedVariables: Map<String, RcNamedVariableOperation> = operations
        .filterIsInstance<RcNamedVariableOperation>()
        .associateBy { it.varName }

    private val dataLists: Map<Int, RcDataListIdsOperation> = operations
        .filterIsInstance<RcDataListIdsOperation>()
        .associateBy { it.id }

    private val floatExpressions: Map<Int, RcFloatExpressionOperation> = operations
        .filterIsInstance<RcFloatExpressionOperation>()
        .associateBy { it.id }

    val componentValueBindings: Map<Int, List<RcComponentValueOperation>> = operations
        .filterIsInstance<RcComponentValueOperation>()
        .groupBy { it.componentId }

    private val textMergeRegistry: Map<Int, RcTextMergeOperation> = operations
        .filterIsInstance<RcTextMergeOperation>()
        .associateBy { it.textId }

    private val textLookupRegistry: Map<Int, RcTextLookupOperation> = operations
        .filterIsInstance<RcTextLookupOperation>()
        .associateBy { it.textId }

    private val evaluator = RcFloatExpressionEvaluator(this)

    var currentSnapshot by mutableStateOf(clock.snapshot())
        private set

    private val resolvedColors = mutableMapOf<Int, Int>().apply {
        putAll(colors)
        operations.filterIsInstance<RcColorConstantOperation>().forEach { put(it.colorId, it.color) }

        var currentTheme = 0

        operations.forEach { op ->
            if (op is RcThemeOperation) {
                currentTheme = op.theme
            } else if (op is RcColorExpressionOperation && op.mode == RcColorExpressionOperation.ID_ID_INTERPOLATE) {

                val isDarkContext = currentTheme == -2 || (currentTheme == 0)
                val isLightContext = currentTheme == -3 || (currentTheme == 0 && !isDarkTheme)

                if (currentTheme == -1 || isDarkContext || isLightContext) {
                    val c1 = get(op.param2) ?: 0
                    val c2 = get(op.param3) ?: 0
                    val ratio = Float.fromBits(op.param4).coerceIn(0f, 1f)

                    val a = (c1 ushr 24 and 0xff) + ((c2 ushr 24 and 0xff) - (c1 ushr 24 and 0xff)) * ratio
                    val r = (c1 ushr 16 and 0xff) + ((c2 ushr 16 and 0xff) - (c1 ushr 16 and 0xff)) * ratio
                    val g = (c1 ushr 8 and 0xff) + ((c2 ushr 8 and 0xff) - (c1 ushr 8 and 0xff)) * ratio
                    val b = (c1 and 0xff) + ((c2 and 0xff) - (c1 and 0xff)) * ratio

                    put(op.id, (a.toInt() shl 24) or (r.toInt() shl 16) or (g.toInt() shl 8) or b.toInt())
                }
            }
        }
    }

    @Composable
    fun Ticker() {
        LaunchedEffect(clock) {
            while (true) {
                withFrameNanos {
                    currentSnapshot = clock.snapshot()
                }
            }
        }
    }

    fun updateFloatVariable(valueId: Int, value: Float) {
        if (floatVariables[valueId] != value) {
            floatVariables[valueId] = value
        }
    }

    fun resolveDynamicFloat(id: Int): Float {
        val cleanId = id and 0x3FFFFF
        when (cleanId) {
            1 -> return currentSnapshot.continuousSeconds
            2 -> return currentSnapshot.continuousSeconds % 60f
            3 -> return (currentSnapshot.continuousSeconds / 60f) % 60f
            4 -> return (currentSnapshot.continuousSeconds / 3600f) % 24f
            5 -> return currentSnapshot.dayOfWeek.toFloat()
            6 -> return currentSnapshot.dayOfMonth.toFloat()
        }
        floatExpressions[id]?.let { expr ->
            return evaluator.evaluate(expr.srcValues)
        }
        return floatVariables[id] ?: 0f
    }

    fun resolveFloat(value: Float): Float {
        if (!value.isNaN()) return value
        val id = value.toRawBits() and 0x7FFFFF
        return resolveDynamicFloat(id)
    }

    fun getListValues(listId: Int): FloatArray? {
        val listOp = dataLists[listId] ?: return null
        return listOp.ids.map { resolveDynamicFloat(it) }.toFloatArray()
    }

    fun getListValue(listId: Int, index: Int): Float {
        val listOp = dataLists[listId] ?: return Float.NaN
        if (index < 0 || index >= listOp.ids.size) return Float.NaN
        return resolveDynamicFloat(listOp.ids[index])
    }

    fun resolveText(textId: Int): String {
        textRegistry[textId]?.let { return it }
        textFromFloatRegistry[textId]?.let { op ->
            val floatValue = resolveFloat(op.value)
            return floatValue.format(op)
        }
        textMergeRegistry[textId]?.let { op ->
            return resolveText(op.srcId1) + resolveText(op.srcId2)
        }
        textLookupRegistry[textId]?.let { op ->
            val dataList = dataLists[op.dataSetId] ?: return "Err: List ${op.dataSetId}"
            val indexF = resolveFloat(op.index)
            val index = indexF.toInt().coerceIn(0, dataList.ids.lastIndex)
            return resolveText(dataList.ids[index])
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
            else -> resolveDynamicFloat(variableId)
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
        return resolvedColors[colorId] ?: 0xFFFF00FF.toInt()
    }

    companion object {
        private const val ID_CONTINUOUS_SEC = 1
    }
}