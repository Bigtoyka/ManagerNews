package org.tbank.managernews.dto

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tbank.managernews.utils.CustomPlaceSerializer
import org.tbank.managernews.utils.LocalDateSerializer
import kotlin.math.exp

@Serializable
data class News(
    val id: Long,
    val title: String,
    @Serializable(with = CustomPlaceSerializer::class)
    val place: Place?,
    val description: String? = null,
    @Serializable(with = LocalDateSerializer::class)
    @SerialName("publication_date") val publicationDate: LocalDateTime,
    @SerialName("site_url") val siteUrl: String? = null,
    @SerialName("favorites_count") val favoritesCount: Int = 0,
    @SerialName("comments_count") val commentsCount: Int = 0
) {
    val rating: Double
        get() = 1.0 / (1 + exp(-(favoritesCount / (commentsCount + 1)).toDouble()))
}
@Serializable
data class Place(
    val title: String,
    val address: String
)
fun List<News>.getMostRatedNews(
    count: Int,
    period: ClosedRange<LocalDate>
): List<News> {
    return this.filter { news ->
        news.publicationDate.date in period
    }.sortedByDescending { it.rating }.take(count)
}
