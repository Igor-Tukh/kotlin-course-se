package ru.hse.spb

import java.io.OutputStream
import java.io.Writer

interface Element {
    fun render(writer: Writer, indent: String)
}

class TextElement(private val text: String) : Element {
    override fun render(writer: Writer, indent: String) {
        writer.write("$indent$text\n")
    }
}

fun renderArguments(writer: Writer, arguments: ArrayList<String>, prefix: CharSequence, postfix: CharSequence) {
    if (arguments.size > 0) {
        writer.write(arguments.joinToString(separator = ",", prefix = prefix, postfix = postfix))
    }
}

@DslMarker
annotation class TexTagMarker

@TexTagMarker
abstract class Tag : Element {
    val children = arrayListOf<Element>()
    private val requiredArguments = arrayListOf<String>()
    private val optionalArguments = arrayListOf<String>()

    fun addRequiredArguments(required: ArrayList<String>) {
        requiredArguments.addAll(required)
    }

    fun addOptionalArguments(optional: Array<out String>) {
        optionalArguments.addAll(optional)
    }

    fun renderAllArguments(writer: Writer) {
        renderArguments(writer, requiredArguments, "{", "}")
        renderArguments(writer, optionalArguments, "[", "]")
    }

    protected fun <T : Element> initChild(child: T, init: T.() -> Unit) {
        child.init()
        children.add(child)
    }

    infix fun String.to(value: String) = this + "=" + value
}

/**
 * In this hometask we have at most one required argument, so type will be String?
 */
@TexTagMarker
open class SingleTag(private val name: String, requiredArgument: String? = null,
                     vararg optionalArguments: String) : Tag() {
    init {
        if (requiredArgument != null) {
            addRequiredArguments(arrayListOf(requiredArgument))
        }
        addOptionalArguments(optionalArguments)
    }

    override fun render(writer: Writer, indent: String) {
        writer.write("$indent\\$name")
        renderAllArguments(writer)
        writer.write("\n")
    }
}

@TexTagMarker
open class Environment(private val name: String, requiredArgument: String? = null,
                       vararg optionalArguments: String) : Tag() {
    init {
        if (requiredArgument != null) {
            addRequiredArguments(arrayListOf(requiredArgument))
        }
        addOptionalArguments(optionalArguments)
    }

    override fun render(writer: Writer, indent: String) {
        writer.write("$indent\\begin{$name}")
        renderAllArguments(writer)
        writer.write("\n")
        for (child in children) {
            child.render(writer, indent + " ".repeat(4))
        }
        writer.write("$indent\\end{$name}\n")
    }

    operator fun String.unaryPlus() {
        children.add(TextElement(this))
    }

    fun frame(frameTitle: String? = null, vararg optionalArguments: String, init: Frame.() -> Unit) =
            initChild(Frame(frameTitle, *optionalArguments), init)

    fun itemize(vararg optionalArguments: String, init: Itemize.() -> Unit) =
            initChild(Itemize(*optionalArguments), init)

    fun enumerate(vararg optionalArguments: String, init: Enumerate.() -> Unit) =
            initChild(Enumerate(*optionalArguments), init)

    fun math(init: Math.() -> Unit) = initChild(Math(), init)

    fun alignment(kind: AlignmentKind, init: Alignment.() -> Unit) = initChild(Alignment(kind), init)

    fun customTag(name: String, vararg optionalArguments: String, init: CustomTag.() -> Unit) =
            initChild(CustomTag(name, *optionalArguments), init)
}

class DocumentClass(documentClass: String, vararg optionalArguments: String) : SingleTag("documentClass",
        documentClass, *optionalArguments)

class UsePackage(usingPackage: String, vararg optionalArguments: String) : SingleTag("usepackage",
        usingPackage, *optionalArguments)

class Item(name: String? = null) : Environment("item", optionalArguments = *if (name != null)
    arrayOf(name) else emptyArray()) {
    override fun render(writer: Writer, indent: String) {
        writer.write("$indent\\item")
        for (child in children) {
            child.render(writer, " ")
        }
    }
}

class Math : Environment("math")

class Frame(frameTitle: String? = null, vararg optionalArguments: String) :
        Environment("frame", requiredArgument = frameTitle, optionalArguments = *optionalArguments)

open class ItemTag(name: String, vararg optionalArguments: String) :
        Environment(name, optionalArguments = *optionalArguments) {
    fun item(name: String? = null, init: Item.() -> Unit) {
        initChild(Item(name), init)
    }
}

class Itemize(vararg optionalArguments: String) : ItemTag("itemize", *optionalArguments)

class Enumerate(vararg optionalArguments: String) : ItemTag("enumerate", *optionalArguments)

class Alignment(kind: AlignmentKind) : Environment(kind.toString())

enum class AlignmentKind {
    LEFT,
    CENTER,
    RIGHT;

    override fun toString(): String {
        return when {
            this == LEFT -> "left"
            this == CENTER -> "center"
            else -> "right"
        }
    }
}

class CustomTag(name: String, vararg params: String) : Environment("customTag", requiredArgument = name,
        optionalArguments = *params)

class Document : Environment("document") {
    private val headers = arrayListOf<Element>()

    override fun render(writer: Writer, indent: String) {
        for (header in headers) {
            header.render(writer, indent)
        }
        super.render(writer, indent)
    }

    fun documentClass(name: String, vararg optionalArguments: String) {
        headers.add(DocumentClass(name, *optionalArguments))
    }

    fun usePackage(name: String, vararg optionalArguments: String) {
        headers.add(UsePackage(name, *optionalArguments))
    }

    fun toOutputStream(outputStream: OutputStream) = outputStream.writer().use { render(it, "") }
}

fun document(init: Document.() -> Unit): Document {
    val document = Document()
    document.init()
    return document
}