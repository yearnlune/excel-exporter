package io.github.yearnlune.excel

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class ExcelMetaTest {

    @Test
    @DisplayName("meta 생성하기 - 객체 리스트")
    fun create() {
        /* GIVEN */
        val currentDate = LocalDateTime.of(2022, 11, 11, 12, 0)
        val bookList = listOf(
            Book("9738941404389", "title#1", 5.00, 3, currentDate, Author("author#1", "author#1@example.com")),
            Book("9783161484100", "title#2", 12.99, 1, currentDate, Author("author#2", "author#2@example.com")),
            Book("9791191114225", "title#3", 24.50, 1, currentDate, Author("author#3", "author#3@example.com")),
            Book("9788901260716", "title#4", 32.05, 3, currentDate),
            Book("9791191043754", "title#5", 22.50, 5, currentDate),
        )

        /* WHEN */
        val excelMeta = ExcelMeta.create(bookList, setOf("isbn", "title", "price", "revision", "publishDate", "author.name", "author.email"))

        /* THEN */
        assertThat(
            excelMeta.headers.map { it.name }.toTypedArray(),
            equalTo(arrayOf("isbn", "title", "price", "revision", "publishDate", "author.name", "author.email"))
        )
        assertThat(
            excelMeta.contents[0].values,
            equalTo(arrayOf("9738941404389", "title#1", 5.0, 3, "2022-11-11T12:00:00", "author#1", "author#1@example.com"))
        )
        assertThat(
            excelMeta.contents[3].values,
            equalTo(arrayOf("9788901260716", "title#4", 32.05, 3, "2022-11-11T12:00:00", null, null))
        )
    }

    @Test
    @DisplayName("meta 생성하기 - 객체 리스트 & null value 처리")
    fun create_withNull() {
        /* GIVEN */
        val currentDate = LocalDateTime.of(2022, 11, 11, 12, 0)
        val bookList = listOf(
            Book("9738941404389", "title#1", 5.00, 3, currentDate, Author("author#1", "author#1@example.com")),
            Book("9783161484100", "title#2", 12.99, 1, currentDate, Author("author#2", "author#2@example.com")),
            Book("9791191114225", "title#3", 24.50, 1, currentDate, Author("author#3", "author#3@example.com")),
            Book("9788901260716", "title#4", 32.05, 3, currentDate),
            Book(null, null, 22.50, 5, currentDate),
            Book(null, null, null, null, null, Author()),
            Book(null, null, null, null, null, null),
        )

        /* WHEN */
        val excelMeta = ExcelMeta.create(bookList, setOf("isbn", "title", "price", "revision", "publishDate", "author.name", "author.email"))

        /* THEN */
        assertThat(
            excelMeta.headers.map { it.name }.toTypedArray(),
            equalTo(arrayOf("isbn", "title", "price", "revision", "publishDate", "author.name", "author.email"))
        )
        assertThat(
            excelMeta.contents.first().values,
            equalTo(arrayOf("9738941404389", "title#1", 5.0, 3, "2022-11-11T12:00:00", "author#1", "author#1@example.com"))
        )
        assertThat(excelMeta.contents[4].values, equalTo(arrayOf(null, null, 22.50, 5, "2022-11-11T12:00:00", null, null)))
        assertThat(excelMeta.contents.size, `is`(bookList.size))
    }

    @Test
    @DisplayName("meta 생성하기 - 맵 리스트")
    fun create_withMap() {
        val bookList = listOf<Map<String, Any>>(
            mapOf(
                "isbn" to "9783161484100",
                "title" to "title#1"
            ),
            mapOf(
                "price" to 2.99,
                "revision" to 3
            )
        )

        /* WHEN */
        val excelMeta = ExcelMeta.create(bookList)

        /* THEN */
        assertThat(
            excelMeta.headers.map { it.name }.toTypedArray(),
            equalTo(arrayOf("isbn", "title", "price", "revision"))
        )
        assertThat(
            excelMeta.contents.first().values,
            equalTo(arrayOf("9783161484100", "title#1", null, null))
        )

        assertThat(
            excelMeta.contents[1].values,
            equalTo(arrayOf(null, null, 2.99, 3))
        )
    }

    @Test
    @DisplayName("meta 생성하기 - 빈 데이터 처리")
    fun create_withEmptyContents() {
        /* GIVEN */
        val bookList = listOf<Map<String, Any>>()

        /* WHEN */
        val excelMeta = ExcelMeta.create(bookList, setOf("isbn", "title", "price", "revision"))
        assertThat(
            excelMeta.headers.map { it.name }.toTypedArray(),
            equalTo(arrayOf("isbn", "title", "price", "revision"))
        )
    }

    data class Book(

        val isbn: String?,

        val title: String?,

        val price: Double?,

        val revision: Int?,

        val publishDate: LocalDateTime?,

        val author: Author? = null
    )

    data class Author(
        val name: String? = null,

        val email: String? = null
    )
}