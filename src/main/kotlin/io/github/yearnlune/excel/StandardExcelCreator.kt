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
        repeat(contents.count()) { rowCount ->
            val contentRow = sheet.createRow(rowIndex.getAndIncrement())
            repeat(contents[rowCount].values.count()) {
                val contentCell = contentRow.createCell(it)
                contentCell.setCellValue(contents[rowCount].values[it])
            }
        }
    }
}
