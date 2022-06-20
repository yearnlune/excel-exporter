package io.github.yearnlune.excel

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Configuration
@Import(ExcelExporterServiceConfiguration::class)
annotation class ExcelExporterEnable
