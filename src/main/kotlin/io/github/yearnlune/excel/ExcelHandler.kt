package io.github.yearnlune.excel

import org.apache.poi.xssf.streaming.SXSSFCell
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*


fun SXSSFCell.setCellValue(any: Any?) {
    return when (any) {
        is Int -> this.setCellValue(any.toDouble())
        is Long -> this.setCellValue(any.toDouble())
        is Float -> this.setCellValue(any.toDouble())
        is Double -> this.setCellValue(any)
        is Date -> this.setCellValue(any)
        is LocalDate -> this.setCellValue(any)
        is LocalDateTime -> this.setCellValue(any)
        else -> this.setCellValue(any as? String)
    }
}
