package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcFloatExpressionOperation(
    override val opCode: Int = OP_CODE,
    val id: Int,
    val srcValues: FloatArray,
    val animationSpec: FloatArray? = null
) : RcOperation {

    override val name: String = "FloatExpression"

    override fun toString(): String {
        return buildString {
            append("FloatExpression[")
            append(id)
            append("] = (")
            append(srcValues.joinToString(" "))
            append(")")
            animationSpec?.let {
                append(" anim=")
                append(it.joinToString(" "))
            }
        }
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 81
        override val opCode: Int = OP_CODE

        override fun decode(reader: RcBufferReader): RcOperation {
            val id = reader.readInt()
            val packedLen = reader.readInt()
            val valueLen = packedLen and 0xFFFF
            val animLen = (packedLen ushr 16) and 0xFFFF

            val values = FloatArray(valueLen) { reader.readFloat() }
            val animation = if (animLen > 0) FloatArray(animLen) { reader.readFloat() } else null

            return RcFloatExpressionOperation(
                id = id,
                srcValues = values,
                animationSpec = animation
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as RcFloatExpressionOperation

        if (opCode != other.opCode) return false
        if (id != other.id) return false
        if (!srcValues.contentEquals(other.srcValues)) return false
        if (!animationSpec.contentEquals(other.animationSpec)) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = opCode
        result = 31 * result + id
        result = 31 * result + srcValues.contentHashCode()
        result = 31 * result + (animationSpec?.contentHashCode() ?: 0)
        result = 31 * result + name.hashCode()
        return result
    }
}