package org.tbank.managernews.dsl

class LinkNews {
    private val content = StringBuilder()

    fun link(url: String, text: String) {
        content.append("<a href=\"$url\">$text</a>\n")
    }

    fun build(): String = content.toString()
}