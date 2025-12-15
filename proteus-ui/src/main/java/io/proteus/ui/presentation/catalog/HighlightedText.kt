package io.proteus.ui.presentation.catalog

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
    modifier: Modifier = Modifier,
    text: String,
    highlightRanges: List<IntRange> = emptyList(),
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    val baseHighlightColor = if (isSystemInDarkTheme()) HighlightDark else HighlightLight
    val baseHighlightTextColor = if (isSystemInDarkTheme()) OnHighlightDark else OnHighlightLight

    val highlightAlpha by animateFloatAsState(
        targetValue = if (highlightRanges.isNotEmpty()) 1f else 0f,
        animationSpec = tween(
            durationMillis = 300,
            easing = FastOutSlowInEasing
        ),
        label = "highlightFade"
    )

    val highlightColor = baseHighlightColor.copy(alpha = highlightAlpha)
    val highlightTextColor = baseHighlightTextColor.copy(alpha = highlightAlpha)

    val annotatedString = buildAnnotatedString {
        append(text)
        if (highlightRanges.isNotEmpty()) {
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
    }

    Text(
        text = annotatedString,
        style = style.copy(color = color),
        modifier = modifier,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}