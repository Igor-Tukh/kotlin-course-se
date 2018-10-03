package ru.hse.spb

enum class Operations(val biFunction: (Int, Int) -> Int) {
    PLUS(Int::plus),
    MINUS(Int::minus),
    MULT(Int::times),
    DIVIDE(Int::div),
    MODULO(Int::rem),
    GREATER({ first, second -> if (first > second) 1 else 0 }), // because true(1) and false(0)
    LOWER({ first, second -> if (first < second) 1 else 0 }),   // is better than compareTo result
    GEQ({ first, second -> if (first >= second) 1 else 0 }),
    LEQ({ first, second -> if (first <= second) 1 else 0 }),
    EQ({ first, second -> if (first == second) 1 else 0 }),
    NEQ({ first, second -> if (first != second) 1 else 0}),
    OR({ first, second -> if (first != 0 || second != 0) 1 else 0 }),
    AND({ first, second -> if (first != 0 && second != 0) 1 else 0})
}