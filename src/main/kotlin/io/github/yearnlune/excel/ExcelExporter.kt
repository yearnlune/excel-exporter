package io.github.yearnlune.excel

import org.apache.poi.ss.usermodel.Workbook
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * The ExcelExporter offer various export methods
 *
 * @property s3Supporter
 */
class ExcelExporter(

    private val s3Supporter: S3Supporter?
) {

    companion object {
        const val DEFAULT_CREATOR_NAME = "YEARNLUNE"
    }

    private val excelCreatorFactory: ExcelCreatorFactory = ExcelCreatorFactory

    /**
     * Export excel as [Workbook]
     *
     * @param data excel metadata
     * @param creatorName excel creator name, default [DEFAULT_CREATOR_NAME]
     * @return [Workbook]
     * @throws NotFoundCreatorException
     */
    @kotlin.jvm.Throws(NotFoundCreatorException::class)
    fun exportAsWorkbook(data: ExcelMeta, creatorName: String = DEFAULT_CREATOR_NAME): Workbook {
        return this.excelCreatorFactory.getCreator(creatorName).createExcel(data)
    }

    /**
     * Export excel as 'base64 string'
     *
     * @param data excel metadata
     * @param creatorName excel creator name, default [DEFAULT_CREATOR_NAME]
     * @return base64 string
     * @throws NotFoundCreatorException
     */
    @kotlin.jvm.Throws(NotFoundCreatorException::class)
    fun exportAsByteString(data: ExcelMeta, creatorName: String = DEFAULT_CREATOR_NAME): String {
        return toByteString(this.exportAsWorkbook(data, creatorName))
    }

    /**
     * Export excel as [ResponseEntity]
     *
     * @param data excel metadata
     * @param fileName excel file name
     * @param creatorName excel creator name, default [DEFAULT_CREATOR_NAME]
     * @return [ResponseEntity]
     */
    fun exportAsResponseEntity(
        data: ExcelMeta,
        fileName: String? = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
        creatorName: String = DEFAULT_CREATOR_NAME
    ): ResponseEntity<ByteArray> {
        runCatching {
            this.exportAsWorkbook(data, creatorName)
        }.fold({
            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=$fileName.xlsx")
                .body(toOutputStream(it).toByteArray())
        }, {
            return ResponseEntity.badRequest().build()
        })
    }

    /**
     * Export excel as download url when configuring s3
     *
     * @param data excel metadata
     * @param fileName excel file name
     * @param creatorName excel creator name, default [DEFAULT_CREATOR_NAME]
     * @return download url
     */
    @kotlin.jvm.Throws(NotFoundCreatorException::class)
    fun exportAsDownloadUrl(data: ExcelMeta, fileName: String = UUID.randomUUID().toString(), creatorName: String = DEFAULT_CREATOR_NAME): String? {
        return s3Supporter?.uploadAndGetDownloadUrl(
            fileName,
            this.exportAsWorkbook(data, creatorName)
        )
    }

    /**
     * Convert [Workbook] into [ByteArrayOutputStream]
     *
     * @param workbook
     * @return output stream
     */
    private fun toOutputStream(workbook: Workbook): ByteArrayOutputStream {
        val outputStream = ByteArrayOutputStream()
        workbook.write(outputStream)

        return outputStream
    }

    /**
     * Convert [Workbook] into 'base64 string'
     *
     * @param workbook
     * @return base64 string
     */
    private fun toByteString(workbook: Workbook): String {
        val outputStream = toOutputStream(workbook)

        return Base64.getEncoder().encodeToString(outputStream.toByteArray())
    }
}
