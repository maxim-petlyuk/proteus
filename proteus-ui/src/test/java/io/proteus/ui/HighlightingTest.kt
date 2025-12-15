package io.proteus.ui

import io.proteus.ui.domain.SearchHighlighter
import org.junit.Test
import org.junit.Assert.*

class HighlightingTest {

    private val searchHighlighter = SearchHighlighter()

    @Test
    fun `findHighlightRanges finds single match`() {
        val result = searchHighlighter.findHighlightRanges("user_authentication", "auth")
        assertEquals(listOf(5 until 9), result)
    }

    @Test
    fun `findHighlightRanges finds multiple matches`() {
        val result = searchHighlighter.findHighlightRanges("test_feature_test", "test")
        assertEquals(listOf(0 until 4, 13 until 17), result)
    }

    @Test
    fun `findHighlightRanges is case insensitive`() {
        val result = searchHighlighter.findHighlightRanges("User_Authentication", "AUTH")
        assertEquals(listOf(5 until 9), result)
    }

    @Test
    fun `findHighlightRanges returns empty for no matches`() {
        val result = searchHighlighter.findHighlightRanges("feature_flag", "xyz")
        assertTrue(result.isEmpty())
    }

    @Test
    fun `findHighlightRanges handles overlapping searches correctly`() {
        val result = searchHighlighter.findHighlightRanges("aaaa", "aa")
        // Should find overlapping matches: positions 0-2, 1-3, 2-4
        assertEquals(listOf(0 until 2, 1 until 3, 2 until 4), result)
    }
}