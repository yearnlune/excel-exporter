package io.github.yearnlune.excel

import org.apache.poi.ss.usermodel.WorkbookFactory
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.core.Is
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.ByteArrayInputStream
import java.time.LocalDateTime
import java.util.*

class ExcelExporterTest {

    private val excelExporter = ExcelExporter(null)

    private val excelMeta = ExcelMeta(
        headers = arrayOf(
            ExcelMeta.Header("아이디", 220),
            ExcelMeta.Header("나이", 120),
            ExcelMeta.Header("가입일", 300)
        ),
        contents = arrayOf(ExcelMeta.Content(arrayOf("Identification", 22, LocalDateTime.now())))
    )

    @Test
    @DisplayName("기본 템플릿 엑셀로 추출하기")
    fun exportAsWorkbook_withDefaultTemplate() {
        /* WHEN */
        val workBook = this.excelExporter.exportAsWorkbook(this.excelMeta)
        val sheet = workBook.getSheetAt(0)
        val row = sheet?.getRow(0)
        val cell = row?.getCell(0)

        /* THEN */
        assertThat("아이디", `is`(cell?.stringCellValue))
    }

    @Test
    @DisplayName("특정 템플릿 엑셀로 추출하기")
    fun exportAsWorkbook_withSpecificTemplate() {
        /* WHEN */
        val workBook = this.excelExporter.exportAsWorkbook(this.excelMeta, "YEARNLUNE")
        val sheet = workBook.getSheetAt(0)
        val row = sheet?.getRow(0)
        val cell = row?.getCell(0)

        /* THEN */
        assertThat("아이디", `is`(cell?.stringCellValue))
    }

    @Test
    @DisplayName("정의되지 않은 템플릿 엑셀로 추출하기")
    fun exportAsWorkbook_withNotExistTemplate() {
        /* GIVEN */
        val creatorName = "NOT_EXIST"

        val exception = assertThrows<NotFoundCreatorException> {
            /* WHEN */
            this.excelExporter.exportAsWorkbook(this.excelMeta, creatorName)
        }

        /* THEN */
        assertThat(exception.message, Is.`is`("COULD NOT FOUND [$creatorName] Excel Creator"))
    }

    @Test
    @DisplayName("String 타입으로 엑셀 추출하기")
    fun exportAsByteString_withDefaultTemplate_withBase64() {
        /* WHEN */
        val byteString = this.excelExporter.exportAsByteString(data = this.excelMeta)
        val workBook = WorkbookFactory.create(ByteArrayInputStream(Base64.getDecoder().decode(byteString)))
        val sheet = workBook?.getSheetAt(0)
        val row = sheet?.getRow(0)
        val cell = row?.getCell(0)

        /* THEN */
        assertThat("아이디", `is`(cell?.stringCellValue))
    }

    @Test
    @DisplayName("String 타입으로 정의되지 않은 템플릿의 엑셀 추출하기")
    fun exportAsByteString_withNotExistTemplate() {
        /* GIVEN */
        val creatorName = "NOT_EXIST"

        val exception = assertThrows<NotFoundCreatorException> {
            /* WHEN */
            this.excelExporter.exportAsByteString(this.excelMeta, creatorName)
        }

        /* THEN */
        assertThat(exception.message, Is.`is`("COULD NOT FOUND [$creatorName] Excel Creator"))
    }

    @Test
    @DisplayName("ResponseEntity 타입으로 엑셀 추출하기")
    fun exportAsResponseEntity_withDefaultTemplate() {
        /* WHEN */
        val responseEntity = this.excelExporter.exportAsResponseEntity(this.excelMeta)

        /* THEN */
        assertThat(200, `is`(responseEntity.statusCodeValue))
    }

    @Test
    @DisplayName("ResponseEntity 타입으로 정의되지 않은 템플릿의 엑셀 추출하기")
    fun exportAsResponseEntity_withNotExistTemplate() {
        /* GIVEN */
        val creatorName = "NOT_EXIST"

        /* WHEN */
        val responseEntity = this.excelExporter.exportAsResponseEntity(this.excelMeta, "UNKNOWN_FILE", creatorName)

        /* THEN */
        assertThat(400, `is`(responseEntity.statusCodeValue))
    }
}
