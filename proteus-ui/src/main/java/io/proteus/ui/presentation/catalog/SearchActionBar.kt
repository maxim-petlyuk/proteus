package io.proteus.ui.presentation.catalog

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SearchActionBar(
    modifier: Modifier = Modifier,
    searchQuery: String,
    onQueryChanged: (String) -> Unit = {},
    isLoading: Boolean = false,
    onBack: () -> Unit = {}
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomEnd
    ) {
        SearchActionBar(
            searchQuery = searchQuery,
            onQueryChanged = onQueryChanged,
            onBack = onBack
        )

        val progressAlpha: Float by animateFloatAsState(
            targetValue = if (isLoading) 1f else 0f,
            animationSpec = tween(
                durationMillis = 500,
                easing = LinearEasing
            ),
            label = ""
        )

        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer(alpha = progressAlpha)
                .height(2.dp)
        )
    }
}

@Composable
private fun SearchActionBar(
    modifier: Modifier = Modifier,
    searchQuery: String,
    onQueryChanged: (String) -> Unit = {},
    onBack: () -> Unit = {}
) {
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp),
        value = searchQuery,
        singleLine = true,
        onValueChange = {
            onQueryChanged.invoke(it)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.clickable {
                    onBack.invoke()
                }
            )
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
            disabledTextColor = MaterialTheme.colorScheme.onBackground,
            focusedPlaceholderColor = MaterialTheme.colorScheme.onBackground
                .copy(ALPHA_FOCUSED_PLACEHOLDER),
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onBackground
                .copy(ALPHA_UNFOCUSED_PLACEHOLDER)
        ),
        placeholder = {
            Text(
                text = "Search",
                textAlign = TextAlign.Center
            )
        }
    )
}

@Preview
@Composable
fun PreviewSearchBar() {
    SearchActionBar(
        searchQuery = ""
    )
}

private const val ALPHA_UNFOCUSED_PLACEHOLDER = 0.6f
private const val ALPHA_FOCUSED_PLACEHOLDER = 0.85f
