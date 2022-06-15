package io.github.yearnlune.excel

@MustBeDocumented
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ExcelCreator(

    val name: String = ""
)
