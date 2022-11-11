package io.github.yearnlune.excel

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
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

    val contents: Array<Content> = arrayOf(),

    val metadata: Map<String, Any> = mutableMapOf()
) {

    companion object {

        /**
         * Creates [ExcelMeta] using data
         *
         * @param dataList data
         * @param predefinedHeaders predefined headers
         * @return excel metadata
         */
        fun create(dataList: List<*>, predefinedHeaders: Set<String> = emptySet(), metadata: Map<String, Any> = mutableMapOf()): ExcelMeta {
            if (dataList.isEmpty()) {
                return ExcelMeta()
            } else {
                val objectMapper = jacksonObjectMapper()
                    .registerModule(JavaTimeModule())
                    .setDateFormat(DateFormat.getDateTimeInstance())
                val flattenMaps = dataList
                    .map {
                        objectMapper.convertValue(it, object: TypeReference<Map<String, Any?>>() {})
                            .filter { field -> field.value != null }
                            .flatten()
                    }

                val headers = predefinedHeaders.map { Header(it) }.toMutableSet()
                if (predefinedHeaders.isEmpty()) {
                    flattenMaps.forEach { data ->
                        data.keys.map { headers.add(Header(it as String)) }
                    }
                }

                val contents = flattenMaps
                    .map {
                        headers
                            .map { header -> it[header.name] }
                            .toTypedArray()
                            .let { Content(it) }
                    }
                    .filter { it.values.isNotEmpty() }
                    .toTypedArray()

                return ExcelMeta(
                    headers = headers.toTypedArray(),
                    contents = contents,
                    metadata = metadata
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
    data class Header(

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
