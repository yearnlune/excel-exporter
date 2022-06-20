package io.github.yearnlune.excel

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class ExcelExporterTestApplication

fun main(args: Array<String>) {
    runApplication<ExcelExporterTestApplication>(*args)
}
