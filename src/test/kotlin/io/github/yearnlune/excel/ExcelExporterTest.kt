package io.github.yearnlune.excel

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class ExcelExporterTest {

    private val excelExporter = ExcelExporter()

    @Test
    @DisplayName("Excel로 기본 템플릿 추출하기")
    fun export_withDefaultTemplate() {
        /* GIVEN */
        val excelMeta = ExcelMeta(
            headers = arrayOf(
                ExcelMeta.Header("아이디"),
                ExcelMeta.Header("나이"),
                ExcelMeta.Header("가입일")
            ),
            contents = arrayOf(ExcelMeta.Content(arrayOf("Identification", 22, LocalDateTime.now())))
        )

        /* WHEN */
        val workBook = this.excelExporter.export(excelMeta)
        val sheet = workBook?.getSheetAt(0)
        val row = sheet?.getRow(0)
        val cell = row?.getCell(0)

        /* THEN */
        assertThat("아이디", `is`(cell?.stringCellValue))
    }

    @Test
    @DisplayName("Excel로 특정 템플릿 추출하기")
    fun export_withStandardTemplate() {
        /* GIVEN */
        val excelMeta = ExcelMeta(
            headers = arrayOf(
                ExcelMeta.Header("아이디"),
                ExcelMeta.Header("나이"),
                ExcelMeta.Header("가입일")
            ),
            contents = arrayOf(ExcelMeta.Content(arrayOf("Identification", 22, LocalDateTime.now())))
        )

        /* WHEN */
        val workBook = this.excelExporter.export("YEARNLUNE", excelMeta)
        val sheet = workBook?.getSheetAt(0)
        val row = sheet?.getRow(0)
        val cell = row?.getCell(0)

        /* THEN */
        assertThat("아이디", `is`(cell?.stringCellValue))
    }

    @Test
    @DisplayName("Excel로 정의되지 않은 템플릿 추출하기")
    fun export_withNotExistTemplate() {
        /* GIVEN */
        val excelMeta = ExcelMeta(
            headers = arrayOf(
                ExcelMeta.Header("아이디"),
                ExcelMeta.Header("나이"),
                ExcelMeta.Header("가입일")
            ),
            contents = arrayOf(ExcelMeta.Content(arrayOf("Identification", 22, LocalDateTime.now())))
        )

        /* WHEN */
        val workBook = this.excelExporter.export("NOT_EXIST", excelMeta)
        val sheet = workBook?.getSheetAt(0)
        val row = sheet?.getRow(0)
        val cell = row?.getCell(0)

        /* THEN */
        assertThat(null, `is`(cell?.stringCellValue))
    }
}
