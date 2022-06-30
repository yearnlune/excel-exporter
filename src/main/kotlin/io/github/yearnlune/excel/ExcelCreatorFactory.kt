package io.github.yearnlune.excel

/**
 * Factory class that declared 'Excel creator' with [ExcelCreator]
 *
 */
object ExcelCreatorFactory {

    private val factory: Map<String, ExcelCreatable> = ExcelCreator::class.getDeclaredClassList()
        .map { it.loadClass().getDeclaredConstructor().newInstance() as ExcelCreatable }
        .associateBy {
            val creatorClass = it.javaClass
            val excelCreatorAnnotation = it.javaClass.getAnnotation(ExcelCreator::class.java)

            excelCreatorAnnotation.name.ifBlank { creatorClass.simpleName }
        }

    /**
     * Gets the excel creator
     *
     * @param name excel creator name
     * @return [ExcelCreatable] excel creator
     * @throws NotFoundCreatorException
     */
    @kotlin.jvm.Throws(NotFoundCreatorException::class)
    fun getCreator(name: String): ExcelCreatable = this.factory[name] ?: throw NotFoundCreatorException("COULD NOT FOUND [$name] Excel Creator")
}

