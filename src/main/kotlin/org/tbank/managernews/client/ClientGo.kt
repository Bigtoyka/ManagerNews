package org.tbank.managernews.client

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.tbank.managernews.dto.News
import java.io.File
import java.nio.file.Paths
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import kotlin.io.path.exists


class ClientGo {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }
    private val location = "msk"
    val logger: Logger = LoggerFactory.getLogger(ClientGo::class.java)

    suspend fun getNews(count: Int = 100): List<News> {
        @Serializable
        data class NewsResponse(
            val results: List<News>
        )
        return try {
            logger.info("Работа с HTTP,API")
            val response: NewsResponse = client.get("https://kudago.com/public-api/v1.4/news/") {
                parameter("page_size", count)
                parameter("order_by", "-publication_date") // сортировка по дате публикации
                parameter("location", location)
                parameter("text_format", "text")
                parameter("expand", "place")
                parameter(
                    "fields",
                    "id,title,place,description,site_url,favorites_count,comments_count,publication_date"
                )
            }.body() ?: throw Exception("Ничего не вернулось")
            logger.debug(response.toString())
            return response.results
        } catch (e: Exception) {
            logger.error("Сериализация не удалась")
            emptyList() // Возвращаем пустой список в случае ошибки
        } finally {
            client.close()
            logger.info("Работа с HTTP,API завершина")
        }
    }

    fun saveNews(path: String = "src/main/resources/news.csv", news: Collection<News>) {
        val filePath = Paths.get(path)
        require(!filePath.exists()) {
            logger.error("Файл существует")  // кидает исключение, если файл существует
        }
        val csv = news.joinToString("\n") { news ->
            "${news.rating},${news.id},${news.title},${news.place?.title ?: "Неизвестно"},${news.description},${news.publicationDate},${news.siteUrl},${news.favoritesCount},${news.commentsCount}"
        }

        try {
            File(path).writeText(csv)
            logger.info("Файл с данными сохранен");
        } catch (e: Exception) {
            throw RuntimeException(e);
        }
    }
}

