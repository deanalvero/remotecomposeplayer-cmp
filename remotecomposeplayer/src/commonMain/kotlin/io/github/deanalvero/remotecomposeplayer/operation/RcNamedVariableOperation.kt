package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcNamedVariableOperation(
    override val opCode: Int = OP_CODE,
    val varId: Int,
    val varType: Int,
    val varName: String
) : RcOperation {

    override val name: String = "NamedVariable"

    override fun toString(): String {
        val trimmed = if (varName.length > 30) varName.take(30) + "..." else varName
        return "VariableName[$varId] = \"$trimmed\" type=$varType"
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 137
        override val opCode: Int = OP_CODE

        const val COLOR_TYPE = 2
        const val FLOAT_TYPE = 1
        const val STRING_TYPE = 0
        const val IMAGE_TYPE = 3
        const val INT_TYPE = 4
        const val LONG_TYPE = 5
        const val FLOAT_ARRAY_TYPE = 6

        override fun decode(reader: RcBufferReader): RcOperation {
            val varId = reader.readInt()
            val varType = reader.readInt()
            val varName = reader.readString()

            return RcNamedVariableOperation(
                varId = varId,
                varType = varType,
                varName = varName
            )
        }
    }
}