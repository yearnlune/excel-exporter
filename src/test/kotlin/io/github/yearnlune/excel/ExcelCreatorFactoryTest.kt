package io.github.yearnlune.excel

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class ExcelCreatorFactoryTest {

    private val excelCreatorFactory: ExcelCreatorFactory = ExcelCreatorFactory()

    @Test
    @DisplayName("standard creator 가져오기")
    fun getCreator_withStandard() {
        /* GIVEN */
        val creatorName = "YEARNLUNE"

        /* WHEN */
        val excelCreator = excelCreatorFactory.getCreator(creatorName)

        /* THEN */
        assertThat(excelCreator?.javaClass?.simpleName, `is`(StandardExcelCreator::class.simpleName))
    }

    @Test
    @DisplayName("test creator 가져오기")
    fun getCreator_withTest() {
        /* GIVEN */
        val creatorName = "TestExcelCreator"

        /* WHEN */
        val excelCreator = excelCreatorFactory.getCreator(creatorName)

        /* THEN */
        assertThat(excelCreator?.javaClass?.simpleName, `is`(creatorName))
    }
}