package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcIntegerExpressionOperation(
    override val opCode: Int = OP_CODE,
    val id: Int,
    val mask: Int,
    val srcValues: IntArray
) : RcOperation {

    override val name: String = "IntegerExpression"

    override fun toString(): String {
        return "$name[$id] -> mask: $mask, values: ${srcValues.contentToString()}"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 144
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            val id = reader.readInt()
            val mask = reader.readInt()
            val valueLen = reader.readInt()

            val srcValues = IntArray(valueLen) { reader.readInt() }

            return RcIntegerExpressionOperation(
                id = id,
                mask = mask,
                srcValues = srcValues
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as RcIntegerExpressionOperation

        if (opCode != other.opCode) return false
        if (id != other.id) return false
        if (mask != other.mask) return false
        if (!srcValues.contentEquals(other.srcValues)) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = opCode
        result = 31 * result + id
        result = 31 * result + mask
        result = 31 * result + srcValues.contentHashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}