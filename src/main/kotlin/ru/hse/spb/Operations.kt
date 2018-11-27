package ru.hse.spb

fun Boolean.asInt(): Int = if (this) 1 else 0

enum class Operations(val biFunction: (Int, Int) -> Int) {
    PLUS(Int::plus),
    MINUS(Int::minus),
    MULT(Int::times),
    DIVIDE(Int::div),
    MODULO(Int::rem),
    GREATER({ first, second -> (first > second).asInt()}), // because true(1) and false(0)
    LOWER({ first, second -> (first < second).asInt()}),   // is better than compareTo result
    GEQ({ first, second -> (first >= second).asInt()}),
    LEQ({ first, second -> (first <= second).asInt()}),
    EQ({ first, second -> (first == second).asInt()}),
    NEQ({ first, second -> (first != second).asInt()}),
    OR({ first, second -> (first != 0 || second != 0).asInt()}),
    AND({ first, second -> (first != 0 && second != 0).asInt()})
}