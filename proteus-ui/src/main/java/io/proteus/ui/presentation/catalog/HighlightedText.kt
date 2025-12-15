package io.proteus.ui.presentation.catalog

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import io.proteus.ui.presentation.theme.HighlightDark
import io.proteus.ui.presentation.theme.HighlightLight
import io.proteus.ui.presentation.theme.OnHighlightDark
import io.proteus.ui.presentation.theme.OnHighlightLight

@Composable
internal fun HighlightedText(
    text: String,
    highlightRanges: List<IntRange> = emptyList(),
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = MaterialTheme.colorScheme.onBackground,
    modifier: Modifier = Modifier
) {
    val highlightColor = if (isSystemInDarkTheme()) HighlightDark else HighlightLight
    val highlightTextColor = if (isSystemInDarkTheme()) OnHighlightDark else OnHighlightLight

    if (highlightRanges.isEmpty()) {
        Text(
            text = text,
            style = style,
            color = color,
            modifier = modifier,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    } else {
        val annotatedString = buildAnnotatedString {
            append(text)
            highlightRanges.forEach { range ->
                if (range.first >= 0 && range.last < text.length) {
                    addStyle(
                        style = SpanStyle(
                            background = highlightColor,
                            color = highlightTextColor
                        ),
                        start = range.first,
                        end = range.last + 1
                    )
                }
            }
        }

        Text(
            text = annotatedString,
            style = style,
            modifier = modifier,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}