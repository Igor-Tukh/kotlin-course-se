package ru.hse.spb

import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStreams
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.hse.spb.parser.ExpLexer
import ru.hse.spb.parser.ExpParser

class TestParse {
    private var parser: ExpParser? = null
    private var lexer: ExpLexer? = null

    private fun init(text: String) {
        lexer = ExpLexer(CharStreams.fromString(text))
        parser = ExpParser(BufferedTokenStream(lexer))
    }

    @Test
    fun testOne() {
        init("1")
        val expextedResult = "(file (block (statement (expression 1))))"
        assertEquals(expextedResult, parser!!.file().toStringTree(parser))
    }

    @Test
    fun testSum() {
        init("return 1 + 1")
        val expextedResult = "(file (block (statement (returnStatement " +
                "return (expression (expression 1) + (expression 1))))))"
        assertEquals(expextedResult, parser!!.file().toStringTree(parser))
    }

    @Test
    fun testWhile() {
        init("var a = 1 while (a < 3) { a = a + 1 }")
        val expextedResult = "(file (block (statement (variable var a = (expression 1))) (statement (" +
                "whileLoop while ( (expression (expression a) < (expression 3)) ) " +
                "(blockWithBraces { (block (statement " +
                "(assigment a = (expression (expression a) + (expression 1))))) })))))"
        assertEquals(expextedResult, parser!!.file().toStringTree(parser))
    }

    @Test
    fun testGcd() {
        val source: String =
                """fun gcd(a, b) {
                    | if (b == 0) {
                    |   return a
                    | }
                    | return gcd(b, a % b)
                    |}
                """.trimMargin()
        init(source)
        val expextedResult = "(file (block (statement (function fun gcd ( (parameterNames a , b) ) " +
                "(blockWithBraces { " +
                "(block (statement (ifStatement if ( " +
                "(expression (expression b) == (expression 0)) ) " +
                "(blockWithBraces { (block (statement (returnStatement return (expression a)))) }))) " +
                "(statement (returnStatement return (expression (functionCall " +
                "gcd ( (arguments (expression b) , (expression (expression a) % (expression b))) )))))) })))))"
        assertEquals(expextedResult, parser!!.file().toStringTree(parser))
    }
}