package io.github.yearnlune.excel

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.text.DateFormat

class ExcelMeta(

    val headers: Array<Header> = arrayOf(),

    val contents: Array<Content> = arrayOf()
) {

    companion object {

        inline fun <reified T> create(dataList: List<T>): ExcelMeta {
            if (dataList.isEmpty()) {
                return ExcelMeta()
            } else {
                val objectMapper = jacksonObjectMapper()
                    .setDateFormat(DateFormat.getDateTimeInstance())

                val flattenMaps = dataList
                    .map { objectMapper.readValue(objectMapper.writeValueAsString(it), Map::class.java).flatten() }

                val headers = flattenMaps.first().keys
                    .map {
                        Header(it as String)
                    }
                    .toTypedArray()
                val contents = flattenMaps
                    .map {
                        headers
                            .filter { header -> it[header.name] != null }
                            .map { header -> it[header.name] }
                            .toTypedArray()
                            .let { Content(it) }
                    }
                    .filter { it.values.isNotEmpty() }
                    .toTypedArray()

                return ExcelMeta(
                    headers = headers,
                    contents = contents
                )
            }
        }
    }

    class Header(

        val name: String,

        val width: Int? = null,
    )

    class Content(

        val values: Array<Any?>
    )
}
