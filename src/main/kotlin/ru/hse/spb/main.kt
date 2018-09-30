package ru.hse.spb

import java.util.*

fun getName(k: Int, pattern: String): String? {
    val length = pattern.length
    var free = 0

    val answer = CharArray(length)
    val used = BooleanArray(k)

    loop@ for (symbol in pattern) {
        val number = symbol.minus('a')
        when {
            number < 0 -> continue@loop
            number >= k -> return null
            else -> used[number] = true
        }
    }

    for (index in 0..(length - 1) / 2) {
        val left = pattern[index]
        val right = pattern[length - 1 - index]
        val isLeftQuestion: Boolean = left == '?'
        val isRightQuestion: Boolean = right == '?'
        val areSymbolsEqual: Boolean = left == right

        when {
            !isLeftQuestion && !isRightQuestion && !areSymbolsEqual -> return null
            isLeftQuestion && !isRightQuestion -> {
                answer[index] = right
                answer[length - 1 - index] = right
            }
            !isLeftQuestion && isRightQuestion -> {
                answer[index] = left
                answer[length - 1 - index] = left
            }
            isLeftQuestion && isRightQuestion -> free++
            areSymbolsEqual -> {
                answer[index] = left
                answer[length - 1 - index] = right
            }
        }
    }

    val need = k - used.count { it }
    free -= need

    if (free < 0) {
        return null
    }

    var currentPosition = 0
    (0..(length - 1) / 2)
            .filter { pattern[it] == '?' && pattern[length - 1 - it] == '?' }
            .forEach {
                if (free-- > 0) {
                    answer[it] = 'a'
                    answer[length - 1 - it] = 'a'
                } else {
                    while (currentPosition < k && used[currentPosition]) {
                        currentPosition++
                    }

                    if (currentPosition >= k) {
                        return null
                    }
                    val symbol = 'a'.plus(currentPosition++)
                    answer[it] = symbol
                    answer[length - 1 - it] = symbol
                }
            }

    return String(answer)
}

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val k = input.nextInt()
    val pattern: String = input.next()
    println(getName(k, pattern) ?: "IMPOSSIBLE")
}