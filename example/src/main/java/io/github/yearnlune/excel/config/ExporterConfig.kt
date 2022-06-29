package io.github.yearnlune.excel.config

import com.amazonaws.services.s3.AmazonS3
import io.github.yearnlune.excel.ExcelExporterConfigAdapter
import io.github.yearnlune.excel.ExcelExporterConfiguration
import io.github.yearnlune.excel.ExcelExporterEnable
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn

@Configuration
@DependsOn("s3Client")
@ExcelExporterEnable
class ExporterConfig(

    private val s3Client: AmazonS3,

    private val appProperties: AppProperties
) : ExcelExporterConfigAdapter() {

    override fun configure(excelExporterConfiguration: ExcelExporterConfiguration) {
        excelExporterConfiguration
            .usingS3(s3Client, appProperties.bucket)
            .withExpiration(360)
    }
}