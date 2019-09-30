package apptentive.com.android.feedback.test

import apptentive.com.android.feedback.DependencyProviderRule
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule

// FIXME: all the test dependencies should be grouped in the core module
open class TestCase(
    private val logMessages: Boolean = false,
    private val logStackTraces: Boolean = false
) {
    @get:Rule
    val dependencyRule = DependencyProviderRule()

    private val results = mutableListOf<Any>()

    //region Before/After

    @Before
    open fun setUp() {
        results.clear()
    }

    //endregion

    //region Resources

    protected fun readText(path: String): String {
        return javaClass.classLoader!!.getResourceAsStream(path).bufferedReader().readText()
    }

    //endregion

    //region Results

    protected fun addResult(result: Any) {
        results.add(result)
    }

    protected fun assertResults(vararg expected: Any, clearResults: Boolean = true) {
        assertEquals(expected.toList(), results)
        if (clearResults) {
            results.clear()
        }
    }

    //endregion
}