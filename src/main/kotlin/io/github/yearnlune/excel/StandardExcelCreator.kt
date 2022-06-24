package io.github.yearnlune.excel

import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.streaming.SXSSFSheet
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import java.util.concurrent.atomic.AtomicInteger

@ExcelCreator("YEARNLUNE")
class StandardExcelCreator : ExcelCreatable {

    override fun createExcel(data: ExcelMeta): Workbook {
        val rowIndex = AtomicInteger()
        val workBook = SXSSFWorkbook()
        val sheet: SXSSFSheet = workBook.createSheet()
        sheet.trackAllColumnsForAutoSizing()

        createHeaders(sheet, rowIndex, data.headers)
        createContents(sheet, rowIndex, data.contents)

        return workBook
    }

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
