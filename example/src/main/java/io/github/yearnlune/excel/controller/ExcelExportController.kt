package io.github.yearnlune.excel.controller

import io.github.yearnlune.excel.ExcelExporter
import io.github.yearnlune.excel.ExcelMeta
import io.github.yearnlune.excel.domain.Student
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import java.util.*

@Controller
class ExcelExportController(
    private val excelExporter: ExcelExporter
) {

    @GetMapping("/excel/student")
    fun findStudents(): ResponseEntity<ByteArray> {
        val students: List<Student> = listOf(
            Student(name = "A", email = "a@example.com", birth = Date()),
            Student(name = "B", email = "b@example.com", birth = Date()),
            Student(name = "C", email = "c@example.com", birth = Date())
        )
        return excelExporter.exportAsResponseEntity(ExcelMeta.create(students), "students")
    }

    @GetMapping("/s3/student")
    fun findStudentsWithDown(): ResponseEntity<String> {
        val students: List<Student> = listOf(
            Student(name = "A", email = "a@example.com", birth = Date()),
            Student(name = "B", email = "b@example.com", birth = Date()),
            Student(name = "C", email = "c@example.com", birth = Date())
        )
        val downloadUrl = excelExporter.exportAsDownloadUrl(ExcelMeta.create(students), "students")

        return if (downloadUrl != null) ResponseEntity.ok().body(downloadUrl) else ResponseEntity.badRequest().build()
    }
}