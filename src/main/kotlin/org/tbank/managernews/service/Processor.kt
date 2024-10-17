package org.tbank.managernews.service

import kotlinx.coroutines.channels.Channel
import org.tbank.managernews.dto.News

class Processor(private val channel: Channel<News>, private val outputPath: String) {

    suspend fun process() {
        val newsList = mutableListOf<News>()
        while (!channel.isClosedForReceive) {
            val news = channel.receiveCatching()
            if (news.isSuccess) {
                newsList.add(news.getOrNull()!!)
            } else {
                break
            }
        }
        saveNews(outputPath, newsList)
    }
}

