package io.github.yearnlune.excel

import org.hamcrest.MatcherAssert
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test


class ClassHandlerTest {

    @Test
    @DisplayName("annotation이 선언된 클래스 리스트 가져오기")
    fun getDeclaredClassList() {
        /* WHEN */
        val excelCreators =
            ExcelCreator::class.getDeclaredClassList()
                .map { it.loadClass().getDeclaredConstructor().newInstance() as ExcelCreatable }
                .associateBy { it.javaClass.simpleName }

        /* THEN */
        MatcherAssert.assertThat(excelCreators["TestExcelCreator"]?.javaClass?.simpleName, `is`(TestExcelCreator::class.java.simpleName))
        MatcherAssert.assertThat(excelCreators["StandardExcelCreator"]?.javaClass?.simpleName, `is`(StandardExcelCreator::class.java.simpleName))
    }
}
