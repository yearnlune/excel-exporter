package io.github.yearnlune.excel

/**
 * Interface for excel exporter configuring
 *
 */
interface ExcelExporterConfigurer {

    /**
     * Use [ExcelExporterConfiguration] to configure [ExcelExporter].
     *
     * @param excelExporterConfiguration
     */
    fun configure(excelExporterConfiguration: ExcelExporterConfiguration)
}