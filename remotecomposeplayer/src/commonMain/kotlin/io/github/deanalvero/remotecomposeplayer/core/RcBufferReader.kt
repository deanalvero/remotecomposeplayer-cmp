package io.github.deanalvero.remotecomposeplayer.core

class RcBufferReader(private val bytes: ByteArray) {
    private var position = 0

    val hasRemaining: Boolean get() = position < bytes.size
    val remaining: Int get() = bytes.size - position
    val currentPosition: Int get() = position

    fun readByte(): Byte {
        if (position >= bytes.size) throw IndexOutOfBoundsException("End of RC buffer stream reached.")
        return bytes[position++]
    }

    fun readInt(): Int {
        val b1 = readByte().toInt() and 0xFF
        val b2 = readByte().toInt() and 0xFF
        val b3 = readByte().toInt() and 0xFF
        val b4 = readByte().toInt() and 0xFF
        return (b1 shl 24) or (b2 shl 16) or (b3 shl 8) or b4
    }

    fun readShort(): Short {
        val b1 = readByte().toInt() and 0xFF
        val b2 = readByte().toInt() and 0xFF
        return ((b1 shl 8) or b2).toShort()
    }

    fun readFloat(): Float {
        return Float.fromBits(readInt())
    }

    fun readLong(): Long {
        val i1 = readInt().toLong() and 0xFFFFFFFFL
        val i2 = readInt().toLong() and 0xFFFFFFFFL
        return (i1 shl 32) or i2
    }

    fun readString(): String {
        val length = readInt()
        if (length <= 0) return ""
        if (position + length > bytes.size) throw IndexOutOfBoundsException("Malformed string length payload.")

        val stringBytes = bytes.copyOfRange(position, position + length)
        position += length
        return stringBytes.decodeToString()
    }

    fun readBytes(count: Int): ByteArray {
        val actualCount = count.coerceAtMost(remaining)
        val target = bytes.copyOfRange(position, position + actualCount)
        position += actualCount
        return target
    }

    fun skip(count: Int) {
        position = (position + count).coerceAtMost(bytes.size)
    }
}