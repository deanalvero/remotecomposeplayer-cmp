package io.github.deanalvero.remotecomposeplayer.operation

import io.github.deanalvero.remotecomposeplayer.core.RcBufferReader
import io.github.deanalvero.remotecomposeplayer.core.RcOpDecoder
import io.github.deanalvero.remotecomposeplayer.core.RcOperation

data class RcHeaderOperation(
    override val opCode: Int = OP_CODE,
    val majorVersion: Int,
    val minorVersion: Int,
    val patchVersion: Int,
    val legacyWidth: Int? = null,
    val legacyHeight: Int? = null,
    val legacyCapabilities: Long? = null,
    val properties: Map<Short, Any> = emptyMap()
) : RcOperation {

    override val name: String = "Header"

    override fun toString(): String {
        val versionStr = "v$majorVersion.$minorVersion.$patchVersion"
        return if (properties.isEmpty()) {
            "$name [$versionStr] -> Legacy Dimensions: ${legacyWidth}x${legacyHeight} | Caps: $legacyCapabilities"
        } else {
            val width = properties[DOC_WIDTH] ?: "Unknown"
            val height = properties[DOC_HEIGHT] ?: "Unknown"
            val desc = properties[DOC_CONTENT_DESCRIPTION] ?: "None"
            "$name [$versionStr] -> Map Dimensions: ${width}x${height} | Description: \"$desc\" | Total Properties: ${properties.size}"
        }
    }

    companion object : RcOpDecoder {
        const val OP_CODE: Int = 0
        override val opCode: Int = OP_CODE

        private const val MAGIC_NUMBER = 0x048C0000

        private const val DATA_TYPE_INT = 0
        private const val DATA_TYPE_FLOAT = 1
        private const val DATA_TYPE_LONG = 2
        private const val DATA_TYPE_STRING = 3

        const val DOC_WIDTH: Short = 5
        const val DOC_HEIGHT: Short = 6
        const val DOC_DENSITY_AT_GENERATION: Short = 7
        const val DOC_CONTENT_DESCRIPTION: Short = 9
        const val DOC_PROFILES: Short = 14

        override fun decode(reader: RcBufferReader): RcOperation {
            var majorVersion = reader.readInt()
            val minorVersion = reader.readInt()
            val patchVersion = reader.readInt()

            if (majorVersion < 0x10000) {
                val width = reader.readInt()
                val height = reader.readInt()
                val capabilities = reader.readLong()

                return RcHeaderOperation(
                    majorVersion = majorVersion,
                    minorVersion = minorVersion,
                    patchVersion = patchVersion,
                    legacyWidth = width,
                    legacyHeight = height,
                    legacyCapabilities = capabilities
                )
            }

            val magicCheck = majorVersion and 0xFFFF0000.toInt()
            if (magicCheck != MAGIC_NUMBER) {
                throw IllegalStateException("Malformed Remote Compose stream. Magic block mismatch.")
            }

            majorVersion = majorVersion and 0xFFFF

            val length = reader.readInt()
            val propertiesMap = mutableMapOf<Short, Any>()

            for (i in 0 until length) {
                val tag = reader.readShort().toInt() and 0xFFFF
                reader.readShort()

                val dataType = tag shr 10
                val propertyKey = (tag and 0x3F).toShort()

                val parsedValue: Any = when (dataType) {
                    DATA_TYPE_INT -> reader.readInt()
                    DATA_TYPE_FLOAT -> reader.readFloat()
                    DATA_TYPE_LONG -> reader.readLong()
                    DATA_TYPE_STRING -> {
                        val stringLength = reader.readInt()
                        reader.readBytes(stringLength).decodeToString()
                    }
                    else -> throw IllegalStateException("Unsupported dynamic header primitive type variant: $dataType")
                }

                propertiesMap[propertyKey] = parsedValue
            }

            return RcHeaderOperation(
                majorVersion = majorVersion,
                minorVersion = minorVersion,
                patchVersion = patchVersion,
                properties = propertiesMap
            )
        }
    }
}