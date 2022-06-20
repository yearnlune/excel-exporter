package io.github.yearnlune.excel

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ExcelExporterServiceConfiguration {

    @Autowired(required = false)
    protected lateinit var excelExporterConfig: ExcelExporterConfigAdapter

    @Bean
    open fun excelExporter(): ExcelExporter {
        excelExporterConfig.configure(excelExporterConfiguration())
        return ExcelExporter(excelExporterConfig)
    }

    private fun excelExporterConfiguration(): ExcelExporterConfiguration {
        val excelExporterConfiguration = ExcelExporterConfiguration()
        excelExporterConfig.init(excelExporterConfiguration)
        return excelExporterConfiguration
    }
}
