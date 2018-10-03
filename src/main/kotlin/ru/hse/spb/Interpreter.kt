package ru.hse.spb

import ru.hse.spb.parser.ExpBaseVisitor
import ru.hse.spb.parser.ExpParser
import java.lang.RuntimeException

class Interpreter: ExpBaseVisitor<Int?>() {
    private var currentScope: Scope = Scope()
    private var terminate: Boolean = false
    private var returnValue: Int? = null

    override fun visitFile(ctx: ExpParser.FileContext): Int? {
        currentScope = Scope()
        return visitBlock(ctx.block())
    }

    override fun visitBlockWithBraces(ctx: ExpParser.BlockWithBracesContext): Int? {
        currentScope = Scope(currentScope)
        val result = visit(ctx.block())
        currentScope = currentScope.parentScope ?: throw RuntimeException(
                "${ctx.start.line}: Unexpected end of scope.")
        return result
    }

    override fun visitBlock(ctx: ExpParser.BlockContext): Int? {
        ctx.statement()
                .map { visit(it) }
                .forEach { _ ->
                    if (terminate) {
                        return returnValue
                    }
                }

        return null
    }

    override fun visitStatement(ctx: ExpParser.StatementContext): Int? {
        return visit(ctx.children[0])
    }

    override fun visitFunction(ctx: ExpParser.FunctionContext): Int? {
        val functionName = ctx.Indetefier().text
        val names = ctx.parameterNames().Indetefier()
        val function: (List<Int>) -> Int = {
            arguments ->
                if (arguments.size != names.size) {
                    throw IllegalNumnberOfArgumentsException(functionName, ctx.start.line)
                }
                currentScope = Scope(currentScope)
                for (index in 0 until names.size) {
                    val name = names[index].toString()
                    val value = arguments[index]
                    currentScope.addVariable(name, value, ctx.start.line)
                }
                visit(ctx.blockWithBraces())
                currentScope = currentScope.parentScope ?: throw RuntimeException(
                        "${ctx.start.line}: Unexpected end of scope")
                terminate = false
            returnValue ?: 0
        }
        currentScope.addFunction(functionName, function, ctx.start.line)
        return null
    }

    override fun visitVariable(ctx: ExpParser.VariableContext): Int? {
        val name = ctx.Indetefier().text
        currentScope.addVariable(name, visit(ctx.expression()), ctx.start.line)
        return null
    }

    override fun visitAssigment(ctx: ExpParser.AssigmentContext): Int? {
        val name = ctx.Indetefier().text
        val value = visit(ctx.expression()) ?: throw RuntimeException(
                "${ctx.start.line}: Not int in the right part of assigment")
        currentScope.updateVariable(name, value, ctx.start.line)
        return null
    }

    override fun visitExpression(ctx: ExpParser.ExpressionContext): Int? {
        val left = ctx.left
        val right = ctx.right
        val isOperation = ctx.operation != null

        when {
            isOperation -> {
                val leftValue = visit(left)
                val rightValue = visit(right)
                if (leftValue == null || rightValue == null) {
                    throw RuntimeException(
                            "{ctx.start.line}: Error during calculating value of expression")
                }
                val operation = when {
                    ctx.PLUS() != null -> Operations.PLUS
                    ctx.MINUS() != null -> Operations.MINUS
                    ctx.MULT() != null -> Operations.MULT
                    ctx.DIVIDE() != null -> Operations.DIVIDE
                    ctx.MODULO() != null -> Operations.MODULO
                    ctx.GREATER() != null -> Operations.GREATER
                    ctx.GEQ() != null -> Operations.GEQ
                    ctx.LOWER() != null -> Operations.LOWER
                    ctx.LEQ() != null -> Operations.LEQ
                    ctx.EQ() != null -> Operations.EQ
                    ctx.NEQ() != null -> Operations.NEQ
                    ctx.OR() != null -> Operations.OR
                    else -> Operations.AND
                }
                return operation.biFunction(leftValue, rightValue)
            }
            ctx.inner != null -> return visit(ctx.inner)
            ctx.literal != null -> return Integer.parseInt(ctx.literal.text)
            ctx.indetefier != null -> return currentScope.getVariable(ctx.indetefier.text, ctx.start.line)
            else -> return visit(ctx.children[0])
        }
    }

    override fun visitFunctionCall(ctx: ExpParser.FunctionCallContext): Int? {
        val name = ctx.Indetefier().text
        val arguments = ctx.arguments().expression().map {
            val argumentValue = visit(it)
            when {
                argumentValue != null -> argumentValue
                else -> throw RuntimeException(
                        "{ctx.start.line}: Error during calculating value of argument")
            }
        }
        return currentScope.getFunction(name, ctx.start.line)(arguments.toList())
    }

    override fun visitIfStatement(ctx: ExpParser.IfStatementContext): Int? {
        val condition = visit(ctx.expression())

        return when {
            condition != 0 -> visit(ctx.blockWithBraces()[0])
            ctx.blockWithBraces().size > 1 -> visit(ctx.blockWithBraces()[1])
            else -> null
        }
    }

    override fun visitWhileLoop(ctx: ExpParser.WhileLoopContext): Int? {
        while (visit(ctx.expression()) != 0) {
            visit(ctx.blockWithBraces())
            if (terminate) {
                break
            }
        }

        return null
    }

    override fun visitReturnStatement(ctx: ExpParser.ReturnStatementContext): Int? {
        returnValue = visit(ctx.expression())
        terminate = true
        return null
    }
}