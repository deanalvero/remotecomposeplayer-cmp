package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcTouchExpressionOperation(
    override val opCode: Int = OP_CODE,
    val id: Int,
    val startValue: Float,
    val min: Float,
    val max: Float,
    val velocityId: Float,
    val touchEffects: Int,
    val exp: FloatArray,
    val stopMode: Int,
    val stopsData: FloatArray,
    val easingData: FloatArray
) : RcOperation {

    override val name = "TouchExpression"

    override fun toString(): String {
        return "$name[id=$id, stopMode=$stopMode, expSize=${exp.size}, stopsSize=${stopsData.size}]"
    }

    companion object : RcOpDecoder {
        const val OP_CODE = 157
        override val opCode = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            val id = reader.readInt()
            val startValue = reader.readFloat()
            val min = reader.readFloat()
            val max = reader.readFloat()
            val velocityId = reader.readFloat()
            val touchEffects = reader.readInt()

            val len = reader.readInt()
            val valueLen = len and 0xFFFF
            val exp = FloatArray(valueLen) { reader.readFloat() }

            val stopLogic = reader.readInt()
            val stopLen = stopLogic and 0xFFFF
            val stopMode = stopLogic ushr 16
            val stopsData = FloatArray(stopLen) { reader.readFloat() }

            val easingLen = reader.readInt()
            val easingData = FloatArray(easingLen) { reader.readFloat() }

            return RcTouchExpressionOperation(
                id = id,
                startValue = startValue,
                min = min,
                max = max,
                velocityId = velocityId,
                touchEffects = touchEffects,
                exp = exp,
                stopMode = stopMode,
                stopsData = stopsData,
                easingData = easingData
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as RcTouchExpressionOperation

        if (opCode != other.opCode) return false
        if (id != other.id) return false
        if (startValue != other.startValue) return false
        if (min != other.min) return false
        if (max != other.max) return false
        if (velocityId != other.velocityId) return false
        if (touchEffects != other.touchEffects) return false
        if (stopMode != other.stopMode) return false
        if (!exp.contentEquals(other.exp)) return false
        if (!stopsData.contentEquals(other.stopsData)) return false
        if (!easingData.contentEquals(other.easingData)) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = opCode
        result = 31 * result + id
        result = 31 * result + startValue.hashCode()
        result = 31 * result + min.hashCode()
        result = 31 * result + max.hashCode()
        result = 31 * result + velocityId.hashCode()
        result = 31 * result + touchEffects
        result = 31 * result + stopMode
        result = 31 * result + exp.contentHashCode()
        result = 31 * result + stopsData.contentHashCode()
        result = 31 * result + easingData.contentHashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}