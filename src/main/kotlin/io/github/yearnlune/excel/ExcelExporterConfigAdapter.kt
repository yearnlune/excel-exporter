package io.github.yearnlune.excel

abstract class ExcelExporterConfigAdapter : ExcelExporterConfigurer {

    private var excelExporterConfiguration: ExcelExporterConfiguration? = null

    fun init(excelExporterConfiguration: ExcelExporterConfiguration) {
        this.excelExporterConfiguration = excelExporterConfiguration
    }

    override fun configure(excelExporterConfiguration: ExcelExporterConfiguration) {
    }

    override fun getS3Support(): S3Supporter? {
        return this.excelExporterConfiguration?.s3Supporter
    }
}