package io.github.yearnlune.excel

/**
 * Thrown when [ExcelCreator] could not be found.
 *
 * @property message
 */
class NotFoundCreatorException(

    override val message: String?
) : RuntimeException(message)