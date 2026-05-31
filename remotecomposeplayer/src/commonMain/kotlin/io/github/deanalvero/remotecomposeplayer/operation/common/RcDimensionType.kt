package io.github.deanalvero.remotecomposeplayer.operation.common

enum class RcDimensionType(val id: Int) {
    EXACT(0),
    FILL(1),
    WRAP(2),
    WEIGHT(3),
    INTRINSIC_MIN(4),
    INTRINSIC_MAX(5),
    EXACT_DP(6),
    FILL_PARENT_MAX_WIDTH(7),
    FILL_PARENT_MAX_HEIGHT(8),
    UNKNOWN(-1);

    companion object {
        fun fromInt(id: Int): RcDimensionType {
            return entries.find { it.id == id } ?: UNKNOWN
        }
    }
}
