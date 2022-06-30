package io.github.yearnlune.excel

/**
 * Provides a base class for creating a [ExcelExporterConfigurer] instance.
 *
 * Example)
 * ```
 * @ExcelExporterEnable
 * class ExporterConfig() : ExcelExporterConfigAdapter() {
 *
 *     override fun configure(excelExporterConfiguration: ExcelExporterConfiguration) {
 *         ...
 *     }
 * }
 * ```
 */
abstract class ExcelExporterConfigAdapter : ExcelExporterConfigurer {

    private var excelExporterConfiguration: ExcelExporterConfiguration? = null

    fun init(excelExporterConfiguration: ExcelExporterConfiguration) {
        this.excelExporterConfiguration = excelExporterConfiguration
    }

    fun getS3Support(): S3Supporter? = this.excelExporterConfiguration?.s3Supporter

    override fun configure(excelExporterConfiguration: ExcelExporterConfiguration) {
    }
}