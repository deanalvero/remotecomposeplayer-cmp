package io.github.deanalvero.remotecomposeplayer.operation.common

enum class RcDimensionType(val id: Int) {
    FIXED(0),
    WRAP(1),
    MATCH_PARENT(2),
    WEIGHT(3),
    UNKNOWN(-1);

    companion object {
        fun fromInt(id: Int): RcDimensionType {
            return entries.find { it.id == id } ?: UNKNOWN
        }
    }
}
