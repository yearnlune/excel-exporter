package io.github.yearnlune.excel

import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.streaming.SXSSFWorkbook

@ExcelCreator
class TestExcelCreator : ExcelCreatable {

    override fun createExcel(data: ExcelMeta): Workbook {
        return SXSSFWorkbook()
    }
}
