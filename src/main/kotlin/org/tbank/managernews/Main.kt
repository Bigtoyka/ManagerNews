package org.tbank.managernews

import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import org.tbank.managernews.client.ClientGo
import org.tbank.managernews.dsl.CustomDslCore
import org.tbank.managernews.service.getMostRatedNews
import org.tbank.managernews.service.saveNews


fun main() = runBlocking {
    val customDslCore = CustomDslCore()
    val clientGo = ClientGo()
    val newsList = clientGo.getNews(2)
    val startDate = LocalDate(2024, 1, 1)
    val endDate = LocalDate(2024, 12, 31)
    val period: ClosedRange<LocalDate> = startDate..endDate
    val mostRatedNews = newsList.getMostRatedNews(3, period)
    mostRatedNews.forEach { news ->
        println("News Title: ${news.title}")
    }
    saveNews(news = mostRatedNews)
    val newsContent = customDslCore.generateBody(newsList)
    customDslCore.saveNewsHTML(content = newsContent)

}

