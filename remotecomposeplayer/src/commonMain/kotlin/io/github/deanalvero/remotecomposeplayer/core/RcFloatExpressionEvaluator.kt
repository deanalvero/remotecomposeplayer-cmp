package io.github.deanalvero.remotecomposeplayer.core

import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan
import kotlin.math.atan2
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.exp
import kotlin.math.floor
import kotlin.math.hypot
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.log2
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sign
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan
import kotlin.math.withSign
import kotlin.random.Random

class RcFloatExpressionEvaluator(private val context: RemoteComposeContext) {
    private val offset = 0x310_000
    private val fpToRad = 57.29578f
    private val fpToDeg = 0.017453292f

    private var mR0 = 0f
    private var mR1 = 0f
    private var mR2 = 0f
    private var mR3 = 0f

    fun evaluate(exp: FloatArray, variables: FloatArray = FloatArray(0)): Float {
        val stack = FloatArray(128)
        var sp = -1

        for (v in exp) {
            if (v.isNaN()) {
                val id = v.toRawBits() and 0x3FFFFF
                if (id < offset) {
                    stack[++sp] = context.resolveDynamicFloat(id)
                } else {
                    sp = evalOp(stack, sp, id, variables)
                }
            } else {
                stack[++sp] = v
            }
        }
        return if (sp >= 0) stack[sp] else 0f
    }

    private fun evalOp(stack: FloatArray, sp: Int, id: Int, vars: FloatArray): Int {
        return when (id) {
            offset + 1 -> {
                stack[sp - 1] = stack[sp - 1] + stack[sp]
                sp - 1
            }
            offset + 2 -> {
                stack[sp - 1] = stack[sp - 1] - stack[sp]
                sp - 1
            }
            offset + 3 -> {
                stack[sp - 1] = stack[sp - 1] * stack[sp]
                sp - 1
            }
            offset + 4 -> {
                stack[sp - 1] = stack[sp - 1] / stack[sp]
                sp - 1
            }
            offset + 5 -> {
                stack[sp - 1] = stack[sp - 1] % stack[sp]
                sp - 1
            }
            offset + 6 -> {
                stack[sp - 1] = min(stack[sp - 1], stack[sp])
                sp - 1
            }
            offset + 7 -> {
                stack[sp - 1] = max(stack[sp - 1], stack[sp])
                sp - 1
            }
            offset + 8 -> {
                stack[sp - 1] = stack[sp - 1].pow(stack[sp])
                sp - 1
            }
            offset + 9 -> {
                stack[sp] = sqrt(stack[sp])
                sp
            }
            offset + 10 -> {
                stack[sp] = abs(stack[sp])
                sp
            }
            offset + 11 -> {
                stack[sp] = sign(stack[sp])
                sp
            }
            offset + 12 -> {
                stack[sp - 1] = stack[sp].withSign(stack[sp - 1])
                sp - 1
            }
            offset + 13 -> {
                stack[sp] = exp(stack[sp])
                sp
            }
            offset + 14 -> {
                stack[sp] = floor(stack[sp])
                sp
            }
            offset + 15 -> {
                stack[sp] = log10(stack[sp])
                sp
            }
            offset + 16 -> {
                stack[sp] = ln(stack[sp])
                sp
            }
            offset + 17 -> {
                stack[sp] = round(stack[sp])
                sp
            }
            offset + 18 -> {
                stack[sp] = sin(stack[sp])
                sp
            }
            offset + 19 -> {
                stack[sp] = cos(stack[sp])
                sp
            }
            offset + 20 -> {
                stack[sp] = tan(stack[sp])
                sp
            }
            offset + 21 -> {
                stack[sp] = asin(stack[sp])
                sp
            }
            offset + 22 -> {
                stack[sp] = acos(stack[sp])
                sp
            }
            offset + 23 -> {
                stack[sp] = atan(stack[sp])
                sp
            }
            offset + 24 -> {
                stack[sp - 1] = atan2(stack[sp - 1], stack[sp])
                sp - 1
            }
            offset + 25 -> {
                stack[sp - 2] = stack[sp] + stack[sp - 1] * stack[sp - 2]
                sp - 2
            }
            offset + 26 -> {
                stack[sp - 2] = if (stack[sp] > 0) stack[sp - 1] else stack[sp - 2]
                sp - 2
            }
            offset + 27 -> {
                stack[sp - 2] = min(max(stack[sp - 2], stack[sp]), stack[sp - 1])
                sp - 2
            }
            offset + 28 -> {
                stack[sp] = stack[sp].pow(1f / 3f)
                sp
            }
            offset + 29 -> {
                stack[sp] = stack[sp] * fpToRad
                sp
            }
            offset + 30 -> {
                stack[sp] = stack[sp] * fpToDeg
                sp
            }
            offset + 31 -> {
                stack[sp] = ceil(stack[sp])
                sp
            }
            offset + 32 -> {
                val listId = stack[sp - 1].toRawBits() and 0x3FFFFF
                stack[sp - 1] = context.getListValue(listId, stack[sp].toInt())
                sp - 1
            }
            offset + 33 -> {
                val listId = stack[sp].toRawBits() and 0x3FFFFF
                stack[sp] = context.getListValues(listId)?.maxOrNull() ?: 0f
                sp
            }
            offset + 34 -> {
                val listId = stack[sp].toRawBits() and 0x3FFFFF
                stack[sp] = context.getListValues(listId)?.minOrNull() ?: 0f
                sp
            }
            offset + 35 -> {
                val listId = stack[sp].toRawBits() and 0x3FFFFF
                stack[sp] = context.getListValues(listId)?.sum() ?: 0f
                sp
            }
            offset + 36 -> {
                val listId = stack[sp].toRawBits() and 0x3FFFFF
                val arr = context.getListValues(listId)
                stack[sp] = if (arr != null && arr.isNotEmpty()) arr.sum() / arr.size else 0f
                sp
            }
            offset + 37 -> {
                val listId = stack[sp].toRawBits() and 0x3FFFFF
                stack[sp] = context.getListValues(listId)?.size?.toFloat() ?: 0f
                sp
            }
            offset + 39 -> {
                stack[sp + 1] = Random.nextFloat()
                sp + 1
            }
            offset + 42 -> {
                stack[sp] = Random.nextFloat() * (stack[sp] - stack[sp - 1]) + stack[sp - 1]
                sp
            }
            offset + 43 -> {
                stack[sp - 1] = stack[sp - 1] * stack[sp - 1] + stack[sp] * stack[sp]
                sp - 1
            }
            offset + 44 -> {
                stack[sp - 1] = if (stack[sp - 1] > stack[sp]) 1f else 0f
                sp - 1
            }
            offset + 45 -> {
                stack[sp] = stack[sp] * stack[sp]
                sp
            }
            offset + 46 -> {
                stack[sp + 1] = stack[sp]
                sp + 1
            }
            offset + 47 -> {
                stack[sp - 1] = hypot(stack[sp - 1], stack[sp])
                sp - 1
            }
            offset + 48 -> {
                val swap = stack[sp - 1]
                stack[sp - 1] = stack[sp]
                stack[sp] = swap
                sp
            }
            offset + 49 -> {
                val tmp1 = stack[sp - 2]
                val tmp2 = stack[sp - 1]
                val tmp3 = stack[sp]
                stack[sp - 2] = tmp1 + (tmp2 - tmp1) * tmp3
                sp - 2
            }
            offset + 51 -> {
                stack[sp] = log2(stack[sp])
                sp
            }
            offset + 52 -> {
                stack[sp] = 1.0f / stack[sp]
                sp
            }
            offset + 53 -> {
                stack[sp] = stack[sp] - stack[sp].toInt()
                sp
            }
            offset + 55 -> sp
            offset + 56 -> {
                mR0 = stack[sp]; sp - 1
            }
            offset + 57 -> {
                mR1 = stack[sp]; sp - 1
            }
            offset + 58 -> {
                mR2 = stack[sp]; sp - 1
            }
            offset + 59 -> {
                mR3 = stack[sp]; sp - 1
            }
            offset + 60 -> {
                stack[sp + 1] = mR0; sp + 1
            }
            offset + 61 -> {
                stack[sp + 1] = mR1; sp + 1
            }
            offset + 62 -> {
                stack[sp + 1] = mR2; sp + 1
            }
            offset + 63 -> {
                stack[sp + 1] = mR3; sp + 1
            }
            offset + 70 -> {
                stack[sp + 1] = vars.getOrElse(0) { 0f }
                sp + 1
            }
            offset + 71 -> {
                stack[sp + 1] = vars.getOrElse(1) { 0f }
                sp + 1
            }
            offset + 72 -> {
                stack[sp + 1] = vars.getOrElse(2) { 0f }
                sp + 1
            }
            offset + 73 -> {
                stack[sp] = -stack[sp]
                sp
            }
            else -> sp
        }
    }
}