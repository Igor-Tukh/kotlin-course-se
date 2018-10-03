package ru.hse.spb

import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStreams
import ru.hse.spb.parser.ExpLexer
import ru.hse.spb.parser.ExpParser
import java.io.File


fun evaluate(text: String): Int {
    val lexer = ExpLexer(CharStreams.fromString(text))
    val parser = ExpParser(BufferedTokenStream(lexer))
    val interpreter = Interpreter()
    return interpreter.visitFile(parser.file()) ?: 0
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        System.err.println("Usage: exactly one argument -- name of file with program")
    }

    evaluate(File(args[0]).readText())
}