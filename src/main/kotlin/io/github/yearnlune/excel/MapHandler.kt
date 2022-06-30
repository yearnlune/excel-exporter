package io.github.yearnlune.excel

import java.util.*

/**
 * Converts map into flatten map
 *
 * @return flatten map
 */
fun Map<*, *>.flatten(): Map<*, *> {
    return this.entries
        .flatMap { it.flatting() }
        .associate { it.key to it.value }
}

/**
 * Flatten the map recursively
 *
 * @return map entries
 */
private fun Map.Entry<*, *>.flatting(): Iterable<Map.Entry<*, *>> {
    return if (this.value is Map<*, *>) {
        (this.value as Map<*, *>)
            .map { AbstractMap.SimpleEntry("${this.key}.${it.key}", it.value) }
            .flatMap { it.flatting() }
    } else listOf(this)
}