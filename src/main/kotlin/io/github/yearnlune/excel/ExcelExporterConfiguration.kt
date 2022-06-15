package io.github.yearnlune.excel

import org.springframework.context.annotation.Bean

class ExcelExporterConfiguration {

    @Bean
    fun excelExporter(): ExcelExporter = ExcelExporter()
}
