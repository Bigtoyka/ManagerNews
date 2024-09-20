import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.tbank.managernews.client.ClientGo
import org.tbank.managernews.dto.News
import org.tbank.managernews.dto.Place
import org.tbank.managernews.service.getMostRatedNews
import org.tbank.managernews.service.saveNews
import java.io.File
import kotlin.test.Test

class NewsTest {

    private var clientGo: ClientGo = ClientGo()

    @Test
    fun testGetNews() = runBlocking {
        val newsList = clientGo.getNews(5)

        assertNotNull(newsList)
        assertEquals(5, newsList.size)
    }

    @Test
    fun getMostRatedNewsTest() = runBlocking {
        val newsList = clientGo.getNews(100)
        assertNotNull(newsList)
        assertEquals(100, newsList.size)
        val period: ClosedRange<LocalDate> = LocalDate(2024, 1, 1)..LocalDate(2024, 12, 31)
        val ratedList = newsList.getMostRatedNews(2,period)
        assertEquals(2, ratedList.size)
    }
    @Test
    fun testSaveFileAndCheckResults() {
        val Path = "src/main/resources/test.csv"

        val newsList = listOf(
            News(
                id = 1,
                title = "titleTest",
                place = Place("placeTest", "addTest"),
                description = "desTest",
                publicationDate = LocalDateTime(2024,9,17,22,55) ,
                siteUrl = "t.org",
                favoritesCount = 20,
                commentsCount = 5,
            )
        )

        saveNews(Path, newsList)

        val file = File(Path)
        assert(file.exists())
        Assertions.assertTrue(file.exists())
        assertEquals("0.9525741268224334,1,titleTest,placeTest,desTest,2024-09-17T22:55,t.org,20,5", file.readLines()[0])
        file.delete()
    }
}