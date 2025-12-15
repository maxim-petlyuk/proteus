package io.proteus.ui.domain

internal class SearchHighlighter {

    fun findHighlightRanges(text: String, query: String): List<IntRange> {
        val ranges = mutableListOf<IntRange>()
        var startIndex = 0

        while (true) {
            val index = text.indexOf(query, startIndex, ignoreCase = true)
            if (index == -1) break

            ranges.add(index until (index + query.length))
            startIndex = index + 1
        }

        return ranges
    }
}