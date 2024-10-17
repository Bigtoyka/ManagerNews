package org.tbank.managernews.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.tbank.managernews.client.ClientGo
import org.tbank.managernews.dto.News
import java.io.File
import java.nio.file.Paths
import kotlin.io.path.exists

fun saveNews(path: String = "src/main/resources/news.csv", news: Collection<News>) {
    val logger: Logger = LoggerFactory.getLogger(ClientGo::class.java)
    val filePath = Paths.get(path)
    require(!filePath.exists()) {
        logger.error("Файл существует")
    }
    val csv = news.joinToString("\n") { news ->
        "${news.rating},${news.id},${news.title},${news.place?.title ?: "Неизвестно"},${news.description},${news.publicationDate},${news.siteUrl},${news.favoritesCount},${news.commentsCount}"
    }

    try {
        File(path).bufferedWriter().use { out -> out.write(csv); out.close() }
        logger.info("Файл с данными сохранен");
    } catch (e: Exception) {
        throw RuntimeException(e);
    }
}