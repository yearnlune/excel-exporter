package io.github.yearnlune.excel.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "test.s3")
class AppProperties(

    var host: String = "",

    var port: Int = 0,

    var bucket: String = ""
)