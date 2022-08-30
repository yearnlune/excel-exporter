package io.github.yearnlune.excel

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.text.SimpleDateFormat
import java.util.*

class ExcelMetaTest {

    @Test
    @DisplayName("객체 리스트로 meta 생성하기")
    fun create() {
        /* GIVEN */
        val currentDate = Date()
        val dateFormat = SimpleDateFormat.getDateTimeInstance()
        val bookList = listOf(
            Book("9738941404389", "title#1", 5.00, 3, currentDate, Author("author#1", "author#1@example.com")),
            Book("9783161484100", "title#2", 12.99, 1, currentDate, Author("author#2", "author#2@example.com")),
            Book("9791191114225", "title#3", 24.50, 1, currentDate, Author("author#3", "author#3@example.com")),
            Book("9788901260716", "title#4", 32.05, 3, currentDate),
            Book("9791191043754", "title#5", 22.50, 5, currentDate),
        )

        /* WHEN */
        val excelMeta = ExcelMeta.create(bookList)

        /* THEN */
        assertThat(
            excelMeta.headers.map { it.name }.toTypedArray(),
            equalTo(arrayOf("isbn", "title", "price", "revision", "publishDate", "author.name", "author.email"))
        )
        assertThat(
            excelMeta.contents[0].values,
            equalTo(arrayOf("9738941404389", "title#1", 5.0, 3, dateFormat.format(currentDate), "author#1", "author#1@example.com"))
        )
        assertThat(
            excelMeta.contents[3].values,
            equalTo(arrayOf("9788901260716", "title#4", 32.05, 3, dateFormat.format(currentDate), null, null))
        )
    }

    @Test
    @DisplayName("객체 리스트로 meta 생성하기 - null value 처리")
    fun create_withNull() {
        /* GIVEN */
        val currentDate = Date()
        val dateFormat = SimpleDateFormat.getDateTimeInstance()
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
        val excelMeta = ExcelMeta.create(bookList)

        /* THEN */
        assertThat(
            excelMeta.headers.map { it.name }.toTypedArray(),
            equalTo(arrayOf("isbn", "title", "price", "revision", "publishDate", "author.name", "author.email"))
        )
        assertThat(
            excelMeta.contents.first().values,
            equalTo(arrayOf("9738941404389", "title#1", 5.0, 3, dateFormat.format(currentDate), "author#1", "author#1@example.com"))
        )
        assertThat(excelMeta.contents[4].values, equalTo(arrayOf(null, null, 22.50, 5, dateFormat.format(currentDate), null, null)))
        assertThat(excelMeta.contents.size, `is`(bookList.size))
    }

    @Test
    @DisplayName("맵 리스트로 meta 생성하기")
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

    data class Book(

        val isbn: String?,

        val title: String?,

        val price: Double?,

        val revision: Int?,

        val publishDate: Date?,

        val author: Author? = null
    )

    data class Author(
        val name: String? = null,

        val email: String? = null
    )
}