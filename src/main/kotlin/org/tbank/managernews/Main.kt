package org.tbank.managernews

import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import org.tbank.managernews.client.ClientGo
import org.tbank.managernews.dto.getMostRatedNews


fun main() = runBlocking {
    val clientGo = ClientGo()
    val newsList = clientGo.getNews(200)
    val startDate = LocalDate(2024, 1, 1)
    val endDate = LocalDate(2024, 12, 31)
    val period: ClosedRange<LocalDate> = startDate..endDate
    val mostRatedNews = newsList.getMostRatedNews(3,period)
    mostRatedNews.forEach { news ->
        println("News Title: ${news.title}")
    }
    clientGo.saveNews("src/main/resources/news.csv", mostRatedNews)
}
