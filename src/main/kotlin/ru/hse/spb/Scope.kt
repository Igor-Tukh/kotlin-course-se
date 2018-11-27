package ru.hse.spb

class Scope(val parentScope: Scope? = null) {
    private val functions = mutableMapOf<String, (List<Int>) -> Int>(
            "println" to { args -> println(args.joinToString(" ")); 0 }
    )
    private val variables = mutableMapOf<String, Int?>()

    fun getFunction(name: String, currentLine: Int): (List<Int>) -> Int {
        return when {
            name in functions -> functions[name]!!
            parentScope != null -> parentScope.getFunction(name, currentLine)
            else -> throw UnknownFunctionException(name, currentLine)
        }
    }

    fun addFunction(name: String, function: (List<Int>) -> Int, currentLine: Int = 0) {
        when {
            name !in functions -> functions[name] = function
            else -> throw MultipleFunctionDefinitionException(name, currentLine)
        }
    }

    fun getVariable(name: String, currentLine: Int): Int? {
        return when {
            name in variables -> variables[name]
            parentScope != null -> parentScope.getVariable(name, currentLine)
            else -> throw UnknownVariableException(name, currentLine)
        }
    }

    fun updateVariable(name: String, newValue: Int, currentLine: Int) {
        when {
            name in variables -> variables[name] = newValue
            parentScope != null -> parentScope.updateVariable(name, newValue, currentLine)
            else -> throw UnknownVariableException(name, currentLine)
        }
    }

    fun addVariable(name: String, value: Int?, currentLine: Int) {
        when {
            name in variables -> throw MultipleVariableDefinitionException(name, currentLine)
            else -> variables[name] = value
        }
    }
}