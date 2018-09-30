package ru.hse.spb

sealed class Node()

sealed class Statement() : Node()

sealed class Expression(): Statement()

data class Block(val statements: List<Statement>): Node()

data class ParameterNames(val parameterNames: List<Identifier>): Node()

data class Function(val name:  Identifier, val body: Block): Statement()

data class Variable(val name: Identifier, val value: Expression): Statement()

data class While(val expression: Expression, val body: Block): Statement()

data class If(val expression: Expression, val body: Block): Statement()

data class Assigment(val name: Identifier, val expression: Expression): Statement()

data class Return(val expression: Expression): Statement()

data class FunctionCall(val functionName: Identifier, val arguments: List<Expression>): Expression()

data class BinaryExpression(val left: Expression, val operation: Operation, val right: Expression): Expression()

data class Identifier(val name: String) : Expression()

data class Literal(val value: Int)

enum class Operation(val biFunction: (Int, Int) -> Int) {
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