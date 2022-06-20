package io.github.yearnlune.excel

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("test")
@ConfigurationProperties(prefix = "test.s3")
open class AppProperties(

    var host: String = "",

    var port: Int = 0,

    var bucket: String = ""
)