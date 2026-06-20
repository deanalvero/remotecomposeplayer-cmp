package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation
import io.github.deanalvero.remotecomposeplayer.operation.common.RcDimensionType

data class RcHeightModifierOperation(
    override val opCode: Int = OP_CODE,
    val typeId: Int,
    val value: Float
) : RcOperation {

    override val name: String = "Modifier.height"

    val dimensionType: RcDimensionType
        get() = RcDimensionType.fromInt(typeId)

    override fun toString(): String {
        val typeLabel = dimensionType.name

        val valueStr = when (dimensionType) {
            RcDimensionType.EXACT -> "${value}px"
            RcDimensionType.EXACT_DP -> "${value}dp"
            RcDimensionType.WEIGHT -> "${value}f"
            RcDimensionType.WRAP,
            RcDimensionType.FILL,
            RcDimensionType.FILL_PARENT_MAX_WIDTH,
            RcDimensionType.FILL_PARENT_MAX_HEIGHT -> "N/A"
            else -> value.toString()
        }

        return "$name -> Strategy: $typeLabel | Value: $valueStr"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 67
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            val type = reader.readInt()
            val value = reader.readFloat()

            return RcHeightModifierOperation(
                typeId = type,
                value = value
            )
        }
    }
}