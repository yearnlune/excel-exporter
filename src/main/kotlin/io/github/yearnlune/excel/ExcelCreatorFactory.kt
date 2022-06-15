package io.github.yearnlune.excel

class ExcelCreatorFactory {

    private val factory: Map<String, ExcelCreatable> = ExcelCreator::class.getDeclaredClassList()
        .map { it.loadClass().getDeclaredConstructor().newInstance() as ExcelCreatable }
        .associateBy {
            val creatorClass = it.javaClass
            val excelCreatorAnnotation = it.javaClass.getAnnotation(ExcelCreator::class.java)

            excelCreatorAnnotation.name.ifBlank { creatorClass.simpleName }
        }

    fun getCreator(name: String): ExcelCreatable? = this.factory[name]
}

