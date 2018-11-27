package ru.hse.spb

import org.junit.Test
import org.junit.Assert.assertEquals
import java.io.ByteArrayOutputStream
import java.io.PrintStream


class TestProgram {
    @Test
    fun testOnePlusOne() {
        assertEquals(2, evaluate("return 1 + 1"))
    }

    @Test
    fun testOnePlusOneWithVariables() {
        assertEquals(2, evaluate("""
            var a = 1
            var b = 1
            return a + b""".trimIndent()))
    }

    @Test
    fun testOneMultiplyTwoWithVariables() {
        assertEquals(-2, evaluate("""
            var a = -1
            var b = 2
            return a * b""".trimIndent()))
    }

    @Test
    fun testOneMinusOneWithVariables() {
        assertEquals(0, evaluate("""
            var a = 1
            var b = 1
            return a - b""".trimIndent()))
    }

    @Test
    fun testThirteenDivideFiveWithVariables() {
        assertEquals(2, evaluate("""
            var a = 13
            var b = 5
            return a / b""".trimIndent()))
    }

    @Test
    fun testThirteenModuloFiveWithVariables() {
        assertEquals(3, evaluate("""
            var a = 13
            var b = 5
            return a % b""".trimIndent()))
    }

    @Test
    fun testThirteenLessFiveWithVariables() {
        assertEquals(0, evaluate("""
            var a = 13
            var b = 5
            return a < b""".trimIndent()))
    }

    @Test
    fun testThirteenGeqFiveWithVariables() {
        assertEquals(1, evaluate("""
            var a = 13
            var b = 5
            return a >= b""".trimIndent()))
    }


    @Test
    fun testFiveLeqFiveWithVariables() {
        assertEquals(1, evaluate("""
            var a = 5
            var b = 5
            return a <= b""".trimIndent()))
    }

    @Test
    fun testFiveLowerSixWithVariables() {
        assertEquals(1, evaluate("""
            var a = 5
            var b = 6
            return a <= b""".trimIndent()))
    }

    @Test
    fun testFiveEqualSixWithVariables() {
        assertEquals(0, evaluate("""
            var a = 5
            var b = 6
            return a == b""".trimIndent()))
    }

    @Test
    fun testFiveNotEqualSixWithVariables() {
        assertEquals(1, evaluate("""
            var a = 5
            var b = 6
            return a != b""".trimIndent()))
    }

    @Test
    fun testFiveOrSixWithVariables() {
        assertEquals(1, evaluate("""
            var a = 5
            var b = 6
            return a || b""".trimIndent()))
    }


    @Test
    fun testFiveAndZeroWithVariables() {
        assertEquals(0, evaluate("""
            var a = 5
            var b = 0
            return a && b""".trimIndent()))
    }


    @Test
    fun testFunctionSum() {
        assertEquals(11, evaluate("""
            fun sum(a, b) {
                return a + b
            }

            var a = 5
            var b = 6
            return sum(a, b)""".trimIndent()))
    }

    @Test
    fun testFunctionSumWithComment() {
        assertEquals(11, evaluate("""
            fun sum(a, b) {
                return a + b
            }

            var a = 5
            // a = 7
            var b = 6
            return sum(a, b)""".trimIndent()))
    }

    @Test
    fun testFunctionAbs() {
        assertEquals(2, evaluate("""
            fun abs(a) {
                if (a >= 0) {
                    return a
                } else {
                    return 0 - a
                }
            }

            var a = -5
            var total = 0
            total = total + (abs(-5) == 5)
            total = total + (abs(4) == -4)
            total = total + (abs(5) == 5)
            return total
            """.trimIndent()))
    }

    @Test
    fun testWhileLoop() {
        assertEquals(2, evaluate("""
            fun sqrt(a) {
                var b = 1

                while (b * b <= a) {
                    b = b + 1
                }

                return b - 1
            }

            var total = 0
            total = total + (sqrt(37) == 6)
            total = total + (sqrt(19) == 4)
            return total
            """.trimIndent()))
    }

    @Test
    fun testScopes() {
        assertEquals(2, evaluate("""
            fun test_scope() {
                var b = 2

                if (b <= 2) {
                    var b = 1
                    if (b == 2) {
                        return 0
                    } else {
                        b = 3
                    }
                }

                return b
            }

            return test_scope()
            """.trimIndent()))
    }

    @Test
    fun testPriority() {
        assertEquals(8, evaluate("""
            return 1 + 2 * 3 + 1
            """.trimIndent()))
    }

    @Test
    fun testPrintln() {
        val outputStream = ByteArrayOutputStream()
        System.setOut(PrintStream(outputStream))
        evaluate("""
            var a = 10
            var b = 3
            println(a + b, a / b, a - b, a * b, a % b)
            """.trimIndent())
        assertEquals("13 3 7 30 1\n", outputStream.toString())
    }

    @Test(expected = UnknownVariableException::class)
    fun testThrowsUnknownVariableException() {
        evaluate("""
            var a = 1
            b = 2
        """.trimIndent())
    }

    @Test(expected = UnknownFunctionException::class)
    fun testThrowsUnknownFunctionException() {
        evaluate("""
            foo(2, 3, 9)
        """.trimIndent())
    }

}