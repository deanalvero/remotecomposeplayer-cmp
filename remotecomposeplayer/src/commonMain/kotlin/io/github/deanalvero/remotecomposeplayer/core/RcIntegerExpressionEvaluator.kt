package io.github.deanalvero.remotecomposeplayer.core

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class RcIntegerExpressionEvaluator(private val context: RemoteComposeContext) {
    private val offset = 0x10000

    fun evaluate(mask: Long, values: IntArray, vars: IntArray = intArrayOf()): Int {
        val stack = IntArray(128)
        var sp = -1

        for (i in values.indices) {
            val isRef = ((mask ushr i) and 1L) == 1L
            val v = values[i]

            if (isRef) {
                if (v < offset) {
                    stack[++sp] = context.resolveInt(v)
                } else {
                    sp = evalOp(stack, sp, v, vars)
                }
            } else {
                stack[++sp] = v
            }
        }
        return if (sp >= 0) stack[sp] else 0
    }

    private fun evalOp(stack: IntArray, sp: Int, id: Int, vars: IntArray): Int {
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
                val divisor = stack[sp]
                stack[sp - 1] = if (divisor != 0) stack[sp - 1] / divisor else 0
                sp - 1
            }
            offset + 5 -> {
                val divisor = stack[sp]
                stack[sp - 1] = if (divisor != 0) stack[sp - 1] % divisor else 0
                sp - 1
            }
            offset + 6 -> {
                stack[sp - 1] = stack[sp - 1] shl stack[sp]
                sp - 1
            }
            offset + 7 -> {
                stack[sp - 1] = stack[sp - 1] shr stack[sp]
                sp - 1
            }
            offset + 8 -> {
                stack[sp - 1] = stack[sp - 1] ushr stack[sp]
                sp - 1
            }
            offset + 9 -> {
                stack[sp - 1] = stack[sp - 1] or stack[sp]
                sp - 1
            }
            offset + 10 -> {
                stack[sp - 1] = stack[sp - 1] and stack[sp]
                sp - 1
            }
            offset + 11 -> {
                stack[sp - 1] = stack[sp - 1] xor stack[sp]
                sp - 1
            }
            offset + 12 -> {
                val sign = stack[sp] shr 31
                stack[sp - 1] = (stack[sp - 1] xor sign) - sign
                sp - 1
            }
            offset + 13 -> {
                stack[sp - 1] = min(stack[sp - 1], stack[sp])
                sp - 1
            }
            offset + 14 -> {
                stack[sp - 1] = max(stack[sp - 1], stack[sp])
                sp - 1
            }
            offset + 15 -> {
                stack[sp] = -stack[sp]
                sp
            }
            offset + 16 -> {
                stack[sp] = abs(stack[sp])
                sp
            }
            offset + 17 -> {
                stack[sp] = stack[sp] + 1
                sp
            }
            offset + 18 -> {
                stack[sp] = stack[sp] - 1
                sp
            }
            offset + 19 -> {
                stack[sp] = stack[sp].inv()
                sp
            }
            offset + 20 -> {
                val v = stack[sp]
                stack[sp] = (v shr 31) or (-v ushr 31)
                sp
            }
            offset + 21 -> {
                stack[sp - 2] = min(max(stack[sp - 2], stack[sp]), stack[sp - 1])
                sp - 2
            }
            offset + 22 -> {
                stack[sp - 2] = if (stack[sp] > 0) stack[sp - 1] else stack[sp - 2]
                sp - 2
            }
            offset + 23 -> {
                stack[sp - 2] = stack[sp] + stack[sp - 1] * stack[sp - 2]
                sp - 2
            }
            offset + 24 -> {
                stack[sp] = if (vars.isNotEmpty()) vars[0] else 0
                sp
            }
            offset + 25 -> {
                stack[sp] = if (vars.size > 1) vars[1] else 0
                sp
            }
            offset + 26 -> {
                stack[sp] = if (vars.size > 2) vars[2] else 0
                sp
            }
            else -> sp
        }
    }
}