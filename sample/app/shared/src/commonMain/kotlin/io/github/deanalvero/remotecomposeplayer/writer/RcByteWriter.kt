package io.github.deanalvero.remotecomposeplayer.writer

class RcByteWriter {
    private val bytes = mutableListOf<Byte>()

    fun writeByte(value: Int) {
        bytes += (value and 0xFF).toByte()
    }

    fun writeShort(value: Int) {
        writeByte(value ushr 8)
        writeByte(value)
    }

    fun writeInt(value: Int) {
        writeByte(value ushr 24)
        writeByte(value ushr 16)
        writeByte(value ushr 8)
        writeByte(value)
    }

    fun writeLong(value: Long) {
        writeInt((value ushr 32).toInt())
        writeInt(value.toInt())
    }

    fun writeFloat(value: Float) {
        writeInt(value.toRawBits())
    }

    fun writeString(value: String) {
        val utf8 = value.encodeToByteArray()
        writeInt(utf8.size)
        for (b in utf8) bytes += b
    }

    fun toByteArray(): ByteArray {
        val result = ByteArray(bytes.size)
        for (i in bytes.indices) {
            result[i] = bytes[i]
        }
        return result
    }
}