package io.github.yearnlune.excel

interface ExcelExporterConfigurer {

    fun configure(excelExporterConfiguration: ExcelExporterConfiguration)

    fun getS3Support(): S3Supporter?
}