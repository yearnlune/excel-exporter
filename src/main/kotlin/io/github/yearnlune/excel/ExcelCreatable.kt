package io.github.yearnlune.excel

import org.apache.poi.ss.usermodel.Workbook

interface ExcelCreatable {

    fun createExcel(data: ExcelMeta): Workbook
}
