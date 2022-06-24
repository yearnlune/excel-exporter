package io.github.yearnlune.excel

import org.apache.poi.ss.usermodel.Workbook
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import java.io.ByteArrayOutputStream
import java.util.*


class ExcelExporter(

    private val excelExporterConfigurer: ExcelExporterConfigurer?
) {

    private val DEFAULT_CREATOR_NAME = "YEARNLUNE"

    private val excelCreatorFactory: ExcelCreatorFactory = ExcelCreatorFactory()

    fun exportAsWorkbook(data: ExcelMeta, creatorName: String = DEFAULT_CREATOR_NAME): Workbook? {
        var workbook: Workbook? = null

        try {
            workbook = this.excelCreatorFactory.getCreator(creatorName).createExcel(data)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return workbook
    }

    fun exportAsByteString(data: ExcelMeta, creatorName: String = DEFAULT_CREATOR_NAME): String? {
        return this.exportAsWorkbook(data, creatorName)?.let { toByteString(it) }
    }

    fun exportAsResponseEntity(
        data: ExcelMeta,
        fileName: String? = "excel_exporter_data",
        creatorName: String = DEFAULT_CREATOR_NAME
    ): ResponseEntity<ByteArray> {
        return this.exportAsWorkbook(data, creatorName)
            ?.let {
                ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=$fileName.xlsx")
                    .body(toOutputStream(it).toByteArray())
            } ?: ResponseEntity.badRequest().build()
    }

    fun exportAsDownloadUrl(data: ExcelMeta, fileName: String = UUID.randomUUID().toString(), creatorName: String = DEFAULT_CREATOR_NAME): String? {
        return excelExporterConfigurer?.getS3Support()?.uploadAndGetDownloadUrl(
            fileName,
            this.exportAsWorkbook(data, creatorName)!!
        )
    }

    private fun toOutputStream(workbook: Workbook): ByteArrayOutputStream {
        val outputStream = ByteArrayOutputStream()
        workbook.write(outputStream)

        return outputStream
    }

    private fun toByteString(workbook: Workbook): String {
        val outputStream = toOutputStream(workbook)

        return Base64.getEncoder().encodeToString(outputStream.toByteArray())
    }
}
