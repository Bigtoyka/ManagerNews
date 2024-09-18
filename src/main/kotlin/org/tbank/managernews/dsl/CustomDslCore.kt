package org.tbank.managernews.dsl

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.tbank.managernews.dto.News
import java.io.File

class CustomDslCore {
    val logger: Logger = LoggerFactory.getLogger(CustomDslCore::class.java)
    private val content = StringBuilder()

    fun header(level: Int = 1, text: String) {
        content.append("<h$level>$text</h$level>\n")
    }

    fun paragraph(block: ParagraphBlock.() -> Unit) {
        val paragraphBlock = ParagraphBlock()
        paragraphBlock.block()
        content.append(paragraphBlock.build())
    }

    fun link(block: LinkNews.() -> Unit) {
        val linkBlock = LinkNews()
        linkBlock.block()
        content.append(linkBlock.build())
    }

    fun build(): String = """
<html>
<head>
  <meta charset="UTF-8">
</head>
<body>
${content.toString()}
</body>
</html>
    """.trimIndent()

    fun saveNewsHTML(path: String = "src/main/resources/newsHTML.html", content: String) {
        try {
            logger.info("Сохранение файла HTML")
            val file = File(path)
            if (file.exists()) {
                logger.warn("Файл уже существует")
            }
            file.writeText(content, Charsets.UTF_8)

            logger.info("Файл HTML сохранен")
        } catch (e: Exception) {
            logger.error("Файл HTML не был сохранен")
            throw RuntimeException(e);
        }
    }
    fun generateBody(newsList: List<News>) : String{
        val newsContent = news {
            header(1, "Новости дня")

            newsList.forEach { news ->
                header(2, news.title)

                paragraph {
                    append("Место: ${news.place}")
                    append("Описание: ${news.description}")
                    append("Рейтинг: ${news.rating}")
                }

                link {
                    link(news.siteUrl ?: "#", "Читать подробнее")
                }
            }
        }
        return newsContent
    }
}
