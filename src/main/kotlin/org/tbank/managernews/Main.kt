package org.tbank.managernews

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking
import org.tbank.managernews.client.ClientGo
import org.tbank.managernews.dto.News
import org.tbank.managernews.service.Processor
import java.util.concurrent.Semaphore


fun main() = runBlocking {
    val startTime = System.currentTimeMillis()

    val clientGo = ClientGo()
    val channel = Channel<News>()
    val outputPath = "src/main/resources/news.csv"

    val workerCount = 3
    val maxConcurrentRequests = 5
    val dispatcher = newFixedThreadPoolContext(workerCount, "news-workers")
    val semaphore = Semaphore(maxConcurrentRequests)

    val workers = List(workerCount) { workerId ->
        launch(dispatcher) {
            var page = workerId + 1
            while (true) {
                semaphore.acquire()
                try {
                    val news = clientGo.getNews(page = page)
                    if (news.isEmpty()) break
                    news.forEach { channel.send(it) }
                } finally {
                    semaphore.release()
                }

                page += workerCount
            }
        }
    }

    val processor = launch {
        Processor(channel, outputPath).process()
    }

    workers.forEach { it.join() }
    channel.close()
    processor.join()

    val endTime = System.currentTimeMillis()
    println("Общее время выполнения: ${endTime - startTime} мс")
    }

