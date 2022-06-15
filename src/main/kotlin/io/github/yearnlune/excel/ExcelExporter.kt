package io.github.yearnlune.excel

import org.apache.poi.ss.usermodel.Workbook


class ExcelExporter {

    private val DEFAULT_CREATOR_NAME = "YEARNLUNE"

    private val excelCreatorFactory: ExcelCreatorFactory = ExcelCreatorFactory()

    fun export(name: String, data: ExcelMeta): Workbook? = this.excelCreatorFactory.getCreator(name)?.createExcel(data)

    fun export(data: ExcelMeta) = export(DEFAULT_CREATOR_NAME, data)
}
