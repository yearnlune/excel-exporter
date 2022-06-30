package io.github.yearnlune.excel

/**
 * Indicates that an annotated class is "ExcelCreator".
 *
 * @property name ExcelCreator name, default class simple name
 */
@MustBeDocumented
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ExcelCreator(

    val name: String = ""
)
