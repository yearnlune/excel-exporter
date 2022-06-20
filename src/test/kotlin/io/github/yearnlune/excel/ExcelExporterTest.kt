package io.github.yearnlune.excel

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class ExcelExporterTest {

    private val excelExporter = ExcelExporter(null)

    @Test
    @DisplayName("기본 템플릿 엑셀로 추출하기")
    fun exportAsWorkbook_withDefaultTemplate() {
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
        val workBook = this.excelExporter.exportAsWorkbook(excelMeta)
        val sheet = workBook?.getSheetAt(0)
        val row = sheet?.getRow(0)
        val cell = row?.getCell(0)

        /* THEN */
        assertThat("아이디", `is`(cell?.stringCellValue))
    }

    @Test
    @DisplayName("특정 템플릿 엑셀로 추출하기")
    fun exportAsWorkbook_withSpecificTemplate() {
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
        val workBook = this.excelExporter.exportAsWorkbook(excelMeta, "YEARNLUNE")
        val sheet = workBook?.getSheetAt(0)
        val row = sheet?.getRow(0)
        val cell = row?.getCell(0)

        /* THEN */
        assertThat("아이디", `is`(cell?.stringCellValue))
    }

    @Test
    @DisplayName("정의되지 않은 템플릿 엑셀로 추출하기")
    fun exportAsWorkbook_withNotExistTemplate() {
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
        val workBook = this.excelExporter.exportAsWorkbook(excelMeta, "NOT_EXIST")
        val sheet = workBook?.getSheetAt(0)
        val row = sheet?.getRow(0)
        val cell = row?.getCell(0)

        /* THEN */
        assertThat(null, `is`(cell?.stringCellValue))
    }

    @Test
    @DisplayName("ResponseEntity 타입으로 엑셀 추출하기")
    fun exportAsResponseEntity_withDefaultTemplate() {
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
        val responseEntity = this.excelExporter.exportAsResponseEntity(excelMeta)

        /* THEN */
        assertThat(200, `is`(responseEntity.statusCodeValue))
    }

    @Test
    @DisplayName("ResponseEntity 타입으로 정의되지 않은 템플릿의 엑셀 추출하기")
    fun exportAsResponseEntity_withNotExistTemplate() {
        /* GIVEN */
        val excelMeta = ExcelMeta(
            headers = arrayOf(
                ExcelMeta.Header("아이디", 200),
                ExcelMeta.Header("나이", 150),
                ExcelMeta.Header("가입일", 220)
            ),
            contents = arrayOf(ExcelMeta.Content(arrayOf("Identification", 22, LocalDateTime.now())))
        )

        /* WHEN */
        val responseEntity = this.excelExporter.exportAsResponseEntity(excelMeta, "UNKNOWN_FILE", "UNKNOWN")

        /* THEN */
        assertThat(400, `is`(responseEntity.statusCodeValue))
    }
}
