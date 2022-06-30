package io.github.yearnlune.excel

import org.apache.poi.ss.usermodel.Workbook

/**
 * ExcelCreator interface.
 *
 */
interface ExcelCreatable {

    /**
     * Create excel with excel metadata.
     *
     * @param data excel metadata
     * @return [Workbook] excel workbook
     */
    fun createExcel(data: ExcelMeta): Workbook
}
