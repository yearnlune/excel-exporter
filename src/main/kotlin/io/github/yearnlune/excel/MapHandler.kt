package io.github.yearnlune.excel

import java.util.AbstractMap

fun Map<*, *>.flatten(): Map<*, *> {
    return this.entries
        .flatMap { it.flatting() }
        .associate { it.key to it.value }
}

fun Map.Entry<*, *>.flatting(): Iterable<Map.Entry<*, *>> {
    return if (this.value is Map<*, *>) {
        (this.value as Map<*, *>)
            .map { AbstractMap.SimpleEntry("${this.key}.${it.key}", it.value) }
            .flatMap { it.flatting() }
    } else listOf(this)
}