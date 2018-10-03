package ru.hse.spb

class UnknownFunctionException(functionName: String, currentLine: Int): Exception(
        "$currentLine: $functionName")

class UnknownVariableException(variableName: String, currentLine: Int): Exception(
        "$currentLine: $variableName")

class MultipleFunctionDefinitionException(functionName: String, currentLine: Int): Exception(
        "$currentLine: $functionName")

class MultipleVariableDefinitionException(variableName: String, currentLine: Int): Exception(
        "$currentLine: $variableName")

class IllegalNumnberOfArgumentsException(functionName: String, currentLine: Int): Exception(
        "$currentLine: $functionName")