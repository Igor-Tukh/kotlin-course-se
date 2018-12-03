package ru.hse.spb

import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayOutputStream

class LibraryTest {
    private fun Document.toResultString(): String {
        val outputStream = ByteArrayOutputStream()
        this.toOutputStream(outputStream)
        return outputStream.toString().trimIndent()
    }

    @Test
    fun testSimpleDocumentWithEmptyBody() {
        val expected = """\begin{document}
                         |\end{document}""".trimMargin()
        assertEquals(expected, document { }.toResultString())
    }

    @Test
    fun testSimpleDocumentWithHeaders() {
        val document = document {
            documentClass("class")
            usePackage("tikz")
        }

        val expected = """\documentClass{class}
                         |\usepackage{tikz}
                         |\begin{document}
                         |\end{document}""".trimMargin()

        assertEquals(expected, document.toResultString())
    }

    @Test
    fun testSimpleDocumentWithText() {
        val document = document {
            +"Hello, world!"
        }

        val expected = """\begin{document}
                         |    Hello, world!
                         |\end{document}""".trimMargin()

        assertEquals(expected, document.toResultString())
    }

    @Test
    fun testSimpleDocumentWithFormula() {
        val document = document {
            math { +"x^2+y^2+2xy\\ne(x+y)^2" }
        }

        val expected = """\begin{document}
                         |    \begin{math}
                         |        x^2+y^2+2xy\ne(x+y)^2
                         |    \end{math}
                         |\end{document}""".trimMargin()

        assertEquals(expected, document.toResultString())
    }

    @Test
    fun testSimpleDocumentWithEnumerate() {
        val document = document {
            enumerate {
                item {
                    +"2"
                }
                item {
                    +"3"
                }
                item {
                    +"9"
                }
            }
        }

        val expected = """\begin{document}
                         |    \begin{enumerate}
                         |        \item 2
                         |        \item 3
                         |        \item 9
                         |    \end{enumerate}
                         |\end{document}""".trimMargin()

        assertEquals(expected, document.toResultString())
    }

    @Test
    fun testSimpleDocumentWithAlignment() {
        val document = document {
            alignment(AlignmentKind.CENTER) {
                +"(Mytischi)"
            }
        }

        val expected = """\begin{document}
                         |    \begin{center}
                         |        (Mytischi)
                         |    \end{center}
                         |\end{document}""".trimMargin()

        assertEquals(expected, document.toResultString())
    }

    @Test
    fun testSimpleDocumentWithItemize() {
        val document = document {
            itemize {
                item {
                    +"2"
                }
                item {
                    +"3"
                }
                item {
                    +"9"
                }
            }
        }

        val expected = """\begin{document}
                         |    \begin{itemize}
                         |        \item 2
                         |        \item 3
                         |        \item 9
                         |    \end{itemize}
                         |\end{document}""".trimMargin()

        assertEquals(expected, document.toResultString())
    }

    @Test
    fun testSimpleDocumentWithFrame() {
        val document = document {
            frame("title", "fieldName" to "1") {
                +"(Stupid)"
            }
        }

        val expected = """\begin{document}
                         |    \begin{frame}{title}[fieldName=1]
                         |        (Stupid)
                         |    \end{frame}
                         |\end{document}""".trimMargin()

        assertEquals(expected, document.toResultString())
    }

    @Test
    fun testSimpleDocumentWithCustomTag() {
        val document = document {
            customTag("title", "fieldName" to "1") {
                +"(Tag)"
            }
        }

        val expected = """\begin{document}
                         |    \begin{customTag}{title}[fieldName=1]
                         |        (Tag)
                         |    \end{customTag}
                         |\end{document}""".trimMargin()

        assertEquals(expected, document.toResultString())
    }

    @Test
    fun testComplexDocumentWithCustomTag() {
        val document = document {
            val string = "239"
            documentClass("beamer")
            usePackage("babel", "russian" /* varargs */)
            frame("frametitle", "arg1" to "arg2") {
                itemize {
                    item {+string}
                }

                // begin{pyglist}[language=kotlin]...\end{pyglist}
                customTag("pyglist", "language" to "kotlin") {
                    +"42"
                }
            }
        }


        val expected = """\documentClass{beamer}
                          |\usepackage{babel}[russian]
                          |\begin{document}
                          |    \begin{frame}{frametitle}[arg1=arg2]
                          |        \begin{itemize}
                          |            \item 239
                          |        \end{itemize}
                          |        \begin{customTag}{pyglist}[language=kotlin]
                          |            42
                          |        \end{customTag}
                          |    \end{frame}
                          |\end{document}""".trimMargin()

        assertEquals(expected, document.toResultString())
    }
}