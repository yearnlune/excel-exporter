package io.github.yearnlune.excel

import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.util.CellRangeAddress
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

        createFilter(sheet, rowIndex, data.headers.size, data.contents.size)
        createHeaders(workBook, sheet, rowIndex, data.headers)
        createContents(workBook, sheet, rowIndex, data.contents)
        adjustWidth(sheet, data.headers)

        return workBook
    }

    /**
     * Create excel headers
     *
     * @param sheet
     * @param rowIndex
     * @param headers
     */
    private fun createHeaders(workBook: Workbook, sheet: SXSSFSheet, rowIndex: AtomicInteger, headers: Array<ExcelMeta.Header>) {
        val headerRow = sheet.createRow(rowIndex.getAndIncrement())
        repeat(headers.count()) {
            val headerCell = headerRow.createCell(it)
            headerCell.setCellValue(headers[it].name)
            headerCell.cellStyle = workBook.createCellStyle().apply {
                this.alignment = HorizontalAlignment.CENTER
                this.verticalAlignment = VerticalAlignment.CENTER
                this.fillForegroundColor = IndexedColors.GREY_40_PERCENT.getIndex()
                this.fillPattern = FillPatternType.SOLID_FOREGROUND
                this.borderBottom = BorderStyle.MEDIUM
                this.borderTop = BorderStyle.MEDIUM
                this.borderLeft = BorderStyle.MEDIUM
                this.borderRight = BorderStyle.MEDIUM
                this.setFont(
                    workBook.createFont().apply {
                        this.bold = true
                    }
                )
            }
        }
    }

    private fun createFilter(
        sheet: SXSSFSheet,
        rowIndex: AtomicInteger,
        headerLength: Int,
        quantity: Int
    ) {
        sheet.setAutoFilter(CellRangeAddress(rowIndex.get(), rowIndex.get() + quantity, 0, headerLength - 1))
    }

    /**
     * Create excel contents
     *
     * @param sheet
     * @param rowIndex
     * @param contents
     */
    private fun createContents(workBook: Workbook, sheet: SXSSFSheet, rowIndex: AtomicInteger, contents: Array<ExcelMeta.Content>) {
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
                contentCell.cellStyle = workBook.createCellStyle().apply {
                    this.borderBottom = BorderStyle.THIN
                    this.borderTop = BorderStyle.THIN
                    this.borderLeft = BorderStyle.THIN
                    this.borderRight = BorderStyle.THIN
                }
            }

            if (isNotNullValue) {
                rowIndex.incrementAndGet()
                isNotNullValue = false
            }
        }
    }

    private fun adjustWidth(sheet: SXSSFSheet, headers: Array<ExcelMeta.Header>) {
        repeat(headers.count()) {
            if (headers[it].width == null) {
                sheet.autoSizeColumn(it)
            } else {
                sheet.setColumnWidth(it, headers[it].width!!)
            }

            if (sheet.getColumnWidth(it) < 10 * 256) {
                sheet.setColumnWidth(it, 10 * 256)
            }
        }
    }
}
