package io.github.yearnlune.excel

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.text.DateFormat

/**
 * Excel metadata that creates excel
 *
 * @property headers excel headers
 * @property contents excel contents
 */
class ExcelMeta(

    val headers: Array<Header> = arrayOf(),

    val contents: Array<Content> = arrayOf()
) {

    companion object {

        /**
         * Creates [ExcelMeta] using data
         *
         * @param T
         * @param dataList data
         * @return excel metadata
         */
        inline fun <reified T> create(dataList: List<T>): ExcelMeta {
            if (dataList.isEmpty()) {
                return ExcelMeta()
            } else {
                val objectMapper = jacksonObjectMapper().setDateFormat(DateFormat.getDateTimeInstance())
                val flattenMaps = dataList
                    .map {
                        objectMapper.readValue(objectMapper.writeValueAsString(it), Map::class.java).flatten()
                    }

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

    /**
     * Excel header metadata
     *
     * @property name header name
     * @property width header width
     */
    class Header(

        val name: String,

        val width: Int? = null,
    )

    /**
     * Excel content
     *
     * @property values
     */
    class Content(

        val values: Array<Any?>
    )
}
