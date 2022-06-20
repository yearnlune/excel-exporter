package io.github.yearnlune.excel

import com.amazonaws.services.s3.AmazonS3Client
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Profile

@Suppress("SpringJavaInjectionPointsAutowiringInspection")
@Configuration
@Profile("test")
@DependsOn("s3Client")
@ExcelExporterEnable
open class ExporterConfig(

    private val s3Client: AmazonS3Client,

    private val appProperties: AppProperties
) : ExcelExporterConfigAdapter() {

    override fun configure(excelExporterConfiguration: ExcelExporterConfiguration) {
        excelExporterConfiguration
            .usingS3(s3Client, appProperties.bucket)
            .withExpiration(360)
    }
}