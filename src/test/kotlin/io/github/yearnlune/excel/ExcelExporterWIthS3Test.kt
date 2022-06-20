package io.github.yearnlune.excel

import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime
import java.util.*

@Suppress("SpringJavaInjectionPointsAutowiringInspection")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ExcelExporterWIthS3Test {

    @Autowired
    private lateinit var appProperties: AppProperties

    @Autowired
    private lateinit var excelExporter: ExcelExporter

    @Test
    @DisplayName("S3를 통한 다운로드 링크로 엑셀 추출하기")
    fun exportAsDownloadUrl() {
        /* GIVEN */
        val excelMeta = ExcelMeta(
            headers = arrayOf(
                ExcelMeta.Header("아이디"),
                ExcelMeta.Header("나이"),
                ExcelMeta.Header("가입일")
            ),
            contents = arrayOf(ExcelMeta.Content(arrayOf("Identification", 22, LocalDateTime.now())))
        )
        val objectKey = UUID.randomUUID().toString()

        /* WHEN */
        val downloadUrl = excelExporter.exportAsDownloadUrl(excelMeta, objectKey)

        /* THEN */
        MatcherAssert.assertThat(
            downloadUrl,
            CoreMatchers.`is`("http://${appProperties.host}:${appProperties.port}/${appProperties.bucket}/excel/$objectKey.xlsx")
        )
    }
}