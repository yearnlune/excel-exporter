package io.github.yearnlune.excel

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

/**
 * Enables 'excel export service'
 *
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Configuration
@Import(ExcelExporterServiceConfiguration::class)
annotation class ExcelExporterEnable
