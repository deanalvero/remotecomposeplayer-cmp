package io.github.deanalvero.remotecomposeplayer

import io.github.deanalvero.remotecomposeplayer.operation.RcTextFromFloatOperation
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.pow

fun Float.format(op: RcTextFromFloatOperation): String {
    if (isNaN()) return "NaN"
    if (isInfinite()) return toString()

    val flags = op.flags
    val digitsBefore = op.digitsBefore.coerceAtLeast(0)
    val digitsAfter = op.digitsAfter.coerceAtLeast(0)

    val fullFormat = (flags and TextFromFloatFlags.FULL_FORMAT) != 0
    if (fullFormat) {
        return toString()
    }

    val legacyMode = (flags and TextFromFloatFlags.LEGACY_MODE) != 0
    val rounding = (flags and TextFromFloatFlags.OPTIONS_ROUNDING) != 0
    val negativeParentheses = (flags and TextFromFloatFlags.OPTIONS_NEGATIVE_PARENTHESES) != 0

    val padAfterMode = flags and 0x3
    val padBeforeMode = flags and (0x3 shl 2)
    val groupingMode = flags and (0x3 shl 4)
    val separatorMode = flags and (0x3 shl 6)

    val negative = this < 0f
    val absValue = abs(this).toDouble()

    val scale = 10.0.pow(digitsAfter.toDouble()).toLong().coerceAtLeast(1L)
    val scaled = absValue * scale.toDouble()

    val scaledLong = if (rounding) {
        floor(scaled + 0.5).toLong()
    } else {
        floor(scaled).toLong()
    }

    var whole = scaledLong / scale
    val fraction = (scaledLong % scale).toInt()

    var wholeText = whole.toString()

    if (digitsBefore > 0 && wholeText.length > digitsBefore) {
        wholeText = wholeText.takeLast(digitsBefore.toInt())
    }

    if (digitsBefore > 0 && wholeText.length < digitsBefore) {
        wholeText = when (padBeforeMode) {
            TextFromFloatFlags.PAD_PRE_ZERO -> wholeText.padStart(digitsBefore.toInt(), '0')
            TextFromFloatFlags.PAD_PRE_NONE -> wholeText
            else -> wholeText.padStart(digitsBefore.toInt(), ' ')
        }
    }

    wholeText = applyGrouping(
        text = wholeText,
        groupingMode = groupingMode,
        separatorMode = separatorMode,
        legacyMode = legacyMode
    )

    val decimalSep = decimalSeparator(separatorMode, legacyMode)
    val groupSep = groupingSeparator(separatorMode, legacyMode)

    val fractionText = when {
        digitsAfter <= 0 -> ""
        else -> {
            val raw = fraction.toString().padStart(digitsAfter.toInt(), '0')
            when (padAfterMode) {
                TextFromFloatFlags.PAD_AFTER_SPACE -> raw.padEnd(digitsAfter.toInt(), ' ')
                TextFromFloatFlags.PAD_AFTER_NONE -> raw.trimEnd('0')
                TextFromFloatFlags.PAD_AFTER_ZERO -> raw.padEnd(digitsAfter.toInt(), '0')
                else -> raw.padEnd(digitsAfter.toInt(), ' ')
            }
        }
    }

    var result = if (digitsAfter > 0) {
        val frac = when (padAfterMode) {
            TextFromFloatFlags.PAD_AFTER_NONE -> fractionText
            else -> fractionText.padEnd(digitsAfter.toInt(), if (padAfterMode == TextFromFloatFlags.PAD_AFTER_ZERO) '0' else ' ')
        }
        if (frac.isEmpty()) wholeText else "$wholeText$decimalSep$frac"
    } else {
        wholeText
    }

    if (negative && result != "0" && result != "0.0") {
        result = if (negativeParentheses) "($result)" else "-$result"
    }

    return result
}

private object TextFromFloatFlags {
    const val PAD_AFTER_SPACE = 0
    const val PAD_AFTER_NONE = 1
    const val PAD_AFTER_ZERO = 3

    const val PAD_PRE_SPACE = 0
    const val PAD_PRE_NONE = 4
    const val PAD_PRE_ZERO = 12

    const val GROUPING_NONE = 0
    const val GROUPING_BY3 = 1 shl 4
    const val GROUPING_BY4 = 2 shl 4
    const val GROUPING_BY32 = 3 shl 4

    const val SEPARATOR_COMMA_PERIOD = 0
    const val SEPARATOR_PERIOD_COMMA = 1 shl 6
    const val SEPARATOR_SPACE_COMMA = 2 shl 6
    const val SEPARATOR_UNDER_PERIOD = 3 shl 6

    const val OPTIONS_NEGATIVE_PARENTHESES = 1 shl 8
    const val OPTIONS_ROUNDING = 2 shl 8
    const val LEGACY_MODE = 1 shl 10
    const val FULL_FORMAT = 1 shl 12
}

private fun applyGrouping(
    text: String,
    groupingMode: Int,
    separatorMode: Int,
    legacyMode: Boolean
): String {
    if (text.isEmpty()) return text
    if (groupingMode == TextFromFloatFlags.GROUPING_NONE) return text

    val sep = groupingSeparator(separatorMode, legacyMode)

    return when (groupingMode) {
        TextFromFloatFlags.GROUPING_BY3 -> groupFromRight(text, 3, sep)
        TextFromFloatFlags.GROUPING_BY4 -> groupFromRight(text, 4, sep)
        TextFromFloatFlags.GROUPING_BY32 -> groupIndianStyle(text, sep)
        else -> text
    }
}

private fun groupFromRight(text: String, groupSize: Int, separator: Char): String {
    if (text.length <= groupSize) return text

    val reversed = text.reversed()
    val chunks = reversed.chunked(groupSize)
    return chunks.joinToString(separator.toString()) { it.reversed() }.reversed()
}

private fun groupIndianStyle(text: String, separator: Char): String {
    if (text.length <= 3) return text

    val last3 = text.takeLast(3)
    val rest = text.dropLast(3)

    val restGrouped = rest.reversed()
        .chunked(2)
        .joinToString(separator.toString()) { it.reversed() }
        .reversed()

    return if (restGrouped.isEmpty()) last3 else "$restGrouped$separator$last3"
}

private fun groupingSeparator(separatorMode: Int, legacyMode: Boolean): Char {
    if (legacyMode) return ','
    return when (separatorMode) {
        TextFromFloatFlags.SEPARATOR_PERIOD_COMMA -> '.'
        TextFromFloatFlags.SEPARATOR_SPACE_COMMA -> ' '
        TextFromFloatFlags.SEPARATOR_UNDER_PERIOD -> '_'
        else -> ','
    }
}

private fun decimalSeparator(separatorMode: Int, legacyMode: Boolean): Char {
    if (legacyMode) return '.'
    return when (separatorMode) {
        TextFromFloatFlags.SEPARATOR_PERIOD_COMMA -> ','
        TextFromFloatFlags.SEPARATOR_SPACE_COMMA -> ','
        TextFromFloatFlags.SEPARATOR_UNDER_PERIOD -> '.'
        else -> '.'
    }
}