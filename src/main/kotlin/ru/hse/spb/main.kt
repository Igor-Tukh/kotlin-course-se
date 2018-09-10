package ru.hse.spb

import java.util.*

fun getName(k: Int, pattern: String): String {
    val len = pattern.length
    var possible = true
    var free = 0

    val answer = CharArray(len)
    val used = BooleanArray(k)

    loop@ for (symbol in pattern) {
        val number = symbol.minus('a')
        when {
            number < 0 -> continue@loop
            number >= k -> possible = false
            else -> used[number] = true
        }
    }

    for (index in 0..(len - 1) / 2) {
        val left = pattern[index]
        val right = pattern[len - 1 - index]

        when {
            left != '?' && right != '?' && left != right -> possible = false
            left == '?' && right != '?' -> {
                answer[index] = right
                answer[len - 1 - index] = right
            }
            left != '?' && right == '?' -> {
                answer[index] = left
                answer[len - 1 - index] = left
            }
            left == '?' && right == '?' -> free++
            left == right -> {
                answer[index] = left
                answer[len - 1 - index] = right
            }
        }
    }

    val need = k - (used.filter { it }).size
    free -= need

    if (free < 0) {
        possible = false
    }

    var currentPosition = 0
    (0..(len - 1) / 2)
            .asSequence()
            .filter { pattern[it] == '?' && pattern[len - 1 - it] == '?' }
            .forEach {
                if (free-- > 0) {
                    answer[it] = 'a'
                    answer[len - 1 - it] = 'a'
                } else {
                    while (currentPosition < k && used[currentPosition]) {
                        currentPosition++
                    }

                    if (currentPosition >= k) {
                        possible = false
                    }
                    val symbol = 'a'.plus(currentPosition++)
                    answer[it] = symbol
                    answer[len - 1 - it] = symbol
                }
            }

    return if (possible) {
        String(answer)
    } else {
        "IMPOSSIBLE"
    }
}

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val k = input.nextInt()
    val pattern: String = input.next()
    println(getName(k, pattern))
}