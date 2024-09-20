package org.tbank.managernews.service

import kotlinx.datetime.LocalDate
import org.tbank.managernews.dto.News

fun List<News>.getMostRatedNews(
    count: Int,
    period: ClosedRange<LocalDate>
): List<News> {
    return this.asSequence()
        .filter { news ->
            news.publicationDate.date in period
        }
        .sortedByDescending { it.rating }
        .take(count)
        .toList()
}