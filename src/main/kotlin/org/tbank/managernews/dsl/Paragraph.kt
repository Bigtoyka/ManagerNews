package org.tbank.managernews.dsl

class ParagraphBlock {
    private val content = StringBuilder()

    fun append(text: String) {
        content.append("<p>$text</p>\n")
    }

    fun build(): String = content.toString()
}

fun news(block: CustomDslCore.() -> Unit): String {
    val newsDsl = CustomDslCore()
    newsDsl.block()
    return newsDsl.build()
}

