package io.github.yearnlune.excel

import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.streaming.SXSSFSheet
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import java.util.concurrent.atomic.AtomicInteger

/**
 * Standard excel creator
 *
 * Example)
 *
 * |    header#1   |    header#2   |    header#3   |
 * |:-------------:|:-------------:|:-------------:|
 * | content[1][1] | content[1][2] | content[1][3] |
 * | content[2][1] | content[2][2] | content[2][3] |
 *
 */
@ExcelCreator("YEARNLUNE")
class StandardExcelCreator : ExcelCreatable {

    /**
     * Create Excel with [ExcelMeta]
     *
     * @param data
     * @return [Workbook]
     */
    override fun createExcel(data: ExcelMeta): Workbook {
        val rowIndex = AtomicInteger()
        val workBook = SXSSFWorkbook()
        val sheet: SXSSFSheet = workBook.createSheet()
        sheet.trackAllColumnsForAutoSizing()

        createHeaders(sheet, rowIndex, data.headers)
        createContents(sheet, rowIndex, data.contents)

        return workBook
    }

    /**
     * Create excel headers
     *
     * @param sheet
     * @param rowIndex
     * @param headers
     */
    private fun createHeaders(sheet: SXSSFSheet, rowIndex: AtomicInteger, headers: Array<ExcelMeta.Header>) {
        val headerRow = sheet.createRow(rowIndex.getAndIncrement())
        repeat(headers.count()) {
            if (headers[it].width == null) {
                sheet.autoSizeColumn(it)
            } else {
                sheet.setColumnWidth(it, headers[it].width!!)
            }

            val headerCell = headerRow.createCell(it)
            headerCell.setCellValue(headers[it].name)
        }
    }

    /**
     * Create excel contents
     *
     * @param sheet
     * @param rowIndex
     * @param contents
     */
    private fun createContents(sheet: SXSSFSheet, rowIndex: AtomicInteger, contents: Array<ExcelMeta.Content>) {
        var isNotNullValue = false
        repeat(contents.count()) { rowCount ->
            val contentRow = sheet.createRow(rowIndex.get())
            repeat(contents[rowCount].values.count()) {
                val contentCell = contentRow.createCell(it)
                contents[rowCount].values[it]
                    ?.let { value ->
                        contentCell.setCellValue(value)
                        isNotNullValue = true
                    }
            }

            if (isNotNullValue) {
                rowIndex.incrementAndGet()
                isNotNullValue = false
            }
        }
    }
}
