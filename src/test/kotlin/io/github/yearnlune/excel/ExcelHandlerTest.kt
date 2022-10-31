package io.github.yearnlune.excel

import org.apache.poi.xssf.streaming.SXSSFCell
import org.apache.poi.xssf.streaming.SXSSFRow
import org.apache.poi.xssf.streaming.SXSSFSheet
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class ExcelHandlerTest {

    private val wb = SXSSFWorkbook()

    private val sheet: SXSSFSheet = wb.createSheet()

    private val row: SXSSFRow = sheet.createRow(0)

    private lateinit var cell: SXSSFCell

    private val columnNumber: AtomicInteger = AtomicInteger()

    @BeforeEach
    fun beforeEach() {
        this.cell = row.createCell(columnNumber.getAndIncrement())
    }

    @Test
    @DisplayName("int 타입 값 넣기")
    fun setCellValue_withInt() {
        /* GIVEN */
        val intValue = 1234

        /* WHEN */
        this.cell.setCellValue(intValue)

        /* THEN */
        assertThat(intValue, `is`(this.cell.numericCellValue.toInt()))
    }

    @Test
    @DisplayName("long 타입 값 넣기")
    fun setCellValue_withLong() {
        /* GIVEN */
        val longValue = 1234L

        /* WHEN */
        this.cell.setCellValue(longValue)

        /* THEN */
        assertThat(longValue, `is`(this.cell.numericCellValue.toLong()))
    }

    @Test
    @DisplayName("float 타입 값 넣기")
    fun setCellValue_withFloat() {
        /* GIVEN */
        val floatValue = 123.4f

        /* WHEN */
        this.cell.setCellValue(floatValue)

        /* THEN */
        assertThat(floatValue, `is`(this.cell.numericCellValue.toFloat()))
    }

    @Test
    @DisplayName("double 타입 값 넣기")
    fun setCellValue_withDouble() {
        /* GIVEN */
        val doubleValue: Any = 123.4

        /* WHEN */
        this.cell.setCellValue(doubleValue)

        /* THEN */
        assertThat(doubleValue, `is`(this.cell.numericCellValue))
    }

    @Test
    @DisplayName("date 타입 값 넣기")
    fun setCellValue_withDate() {
        /* GIVEN */
        val dateValue: Any = Date()

        /* WHEN */
        this.cell.setCellValue(dateValue)

        /* THEN */
        assertThat((dateValue as Date).time, `is`(this.cell.dateCellValue.time))
    }

    @Test
    @DisplayName("date 타입 값 넣기")
    fun setCellValue_withLocalDate() {
        /* GIVEN */
        val dateValue: Any = LocalDate.now()

        /* WHEN */
        this.cell.setCellValue(dateValue)

        /* THEN */
        assertThat(
            dateValue as LocalDate, `is`(
                Instant.ofEpochMilli(this.cell.dateCellValue.time).atZone(
                    ZoneId.systemDefault()
                ).toLocalDate()
            )
        )
    }

    @Test
    @DisplayName("string 타입 값 넣기")
    fun setCellValue_withString() {
        /* GIVEN */
        val stringValue: Any = "value"

        /* WHEN */
        this.cell.setCellValue(stringValue)

        /* THEN */
        assertThat(stringValue, `is`(this.cell.stringCellValue))
    }

    @Test
    @DisplayName("boolean 타입 값 넣기")
    fun setCellValue_withBoolean() {
        /* GIVEN */
        val booleanType: Any = true

        /* WHEN */
        this.cell.setCellValue(booleanType)

        /* THEN */
        assertThat(booleanType, `is`(this.cell.booleanCellValue))
    }
}
