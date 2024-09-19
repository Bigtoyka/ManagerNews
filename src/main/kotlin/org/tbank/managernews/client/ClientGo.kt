package org.tbank.managernews.client

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.tbank.managernews.dto.News
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
}


