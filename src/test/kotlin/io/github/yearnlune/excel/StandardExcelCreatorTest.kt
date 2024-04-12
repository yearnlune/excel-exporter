package io.github.yearnlune.excel

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.*

class StandardExcelCreatorTest {

    private val standardExcelCreator: StandardExcelCreator = StandardExcelCreator()

    @Test
    @DisplayName("standard excel 만들기")
    fun createExcel() {
        /* GIVEN */
        val request = ExcelMeta(
            headers = arrayOf(
                ExcelMeta.Header("아이디"),
                ExcelMeta.Header("나이"),
                ExcelMeta.Header("가입일")
            ),
            contents = arrayOf(ExcelMeta.Content(arrayOf("Identification", 22, Date())))
        )

        /* WHEN */
        val workbook = standardExcelCreator.createExcel(request)
        val sheet = workbook.getSheetAt(0)

        /* THEN */
        assertThat(sheet.getRow(0).getCell(0).stringCellValue, `is`(request.headers[0].name))
        assertThat(sheet.getRow(0).getCell(1).stringCellValue, `is`(request.headers[1].name))
        assertThat(sheet.getRow(0).getCell(2).stringCellValue, `is`(request.headers[2].name))

        assertThat(sheet.getRow(1).getCell(0).stringCellValue, `is`(request.contents[0].values[0]))
        assertThat(
            sheet.getRow(1).getCell(1).numericCellValue,
            `is`(Matchers.closeTo((request.contents[0].values[1] as Int).toDouble(), 0.001))
        )
        assertThat(
            sheet.getRow(1).getCell(2).dateCellValue.time, `is`((request.contents[0].values[2] as Date).time)
        )
    }

    @Test
    @DisplayName("standard excel 만들기 - null value 처리")
    fun createExcel_nullValue() {
        /* GIVEN */
        val request = ExcelMeta(
            headers = arrayOf(
                ExcelMeta.Header("아이디"),
                ExcelMeta.Header("나이"),
                ExcelMeta.Header("가입일")
            ),
            contents = arrayOf(
                ExcelMeta.Content(arrayOf("ID#1", 1, Date())),
                ExcelMeta.Content(arrayOf(null, null, null)),
                ExcelMeta.Content(arrayOf("ID#3", 3, Date())),
            )
        )

        /* WHEN */
        val workbook = standardExcelCreator.createExcel(request)
        val sheet = workbook.getSheetAt(0)

        /* THEN */
        assertThat(sheet.getRow(0).getCell(0).stringCellValue, `is`(request.headers[0].name))
        assertThat(sheet.getRow(0).getCell(1).stringCellValue, `is`(request.headers[1].name))
        assertThat(sheet.getRow(0).getCell(2).stringCellValue, `is`(request.headers[2].name))

        assertThat(sheet.getRow(1).getCell(0).stringCellValue, `is`(request.contents[0].values[0]))
        assertThat(
            sheet.getRow(1).getCell(1).numericCellValue,
            `is`(Matchers.closeTo((request.contents[0].values[1] as Int).toDouble(), 0.001))
        )
        assertThat(
            sheet.getRow(1).getCell(2).dateCellValue.time, `is`((request.contents[0].values[2] as Date).time)
        )

        assertThat(sheet.getRow(2).getCell(0).stringCellValue, `is`(request.contents[2].values[0]))
        assertThat(
            sheet.getRow(2).getCell(1).numericCellValue,
            `is`(Matchers.closeTo((request.contents[2].values[1] as Int).toDouble(), 0.001))
        )
        assertThat(
            sheet.getRow(2).getCell(2).dateCellValue.time, `is`((request.contents[2].values[2] as Date).time)
        )
    }

    @Test
    @DisplayName("standard excel 만들기 - empty contents 처리")
    fun createExcel_emptyContents() {
        /* GIVEN */
        val request = ExcelMeta(
            headers = arrayOf(
                ExcelMeta.Header("아이디"),
                ExcelMeta.Header("나이"),
                ExcelMeta.Header("가입일")
            ),
            contents = arrayOf()
        )

        /* WHEN */
        val workbook = standardExcelCreator.createExcel(request)

        val sheet = workbook.getSheetAt(0)

        /* THEN */
        assertThat(sheet.getRow(0).getCell(0).stringCellValue, `is`(request.headers[0].name))
        assertThat(sheet.getRow(0).getCell(1).stringCellValue, `is`(request.headers[1].name))
        assertThat(sheet.getRow(0).getCell(2).stringCellValue, `is`(request.headers[2].name))
    }
}
