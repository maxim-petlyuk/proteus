package io.proteus.ui.presentation.catalog

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.proteus.ui.R

@Composable
internal fun SearchActionBar(
    modifier: Modifier = Modifier,
    searchQuery: String,
    onQueryChanged: (String) -> Unit = {},
    onClearSearch: () -> Unit = {},
    isLoading: Boolean = false,
    onBack: () -> Unit = {},
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomEnd
    ) {
        SearchField(
            searchQuery = searchQuery,
            onQueryChanged = onQueryChanged,
            onBack = onBack,
            onClearSearch = onClearSearch,
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
private fun SearchField(
    modifier: Modifier = Modifier,
    searchQuery: String,
    placeholder: String = "Search",
    onQueryChanged: (String) -> Unit = {},
    onBack: () -> Unit = {},
    onClearSearch: () -> Unit = {},
) {
    val trailingIconRes = if (searchQuery.isEmpty()) {
        R.drawable.ic_search
    } else {
        R.drawable.ic_close
    }

    val focusRequester = remember { FocusRequester() }

    TextField(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .focusRequester(focusRequester),
        value = searchQuery,
        singleLine = true,
        onValueChange = {
            onQueryChanged.invoke(it)
        },
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.clickable {
                    onBack.invoke()
                }
            )
        },
        trailingIcon = {
            Icon(
                painter = painterResource(trailingIconRes),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.clickable {
                    if (searchQuery.isEmpty()) {
                        focusRequester.requestFocus()
                    } else {
                        onClearSearch.invoke()
                    }
                }
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
                .copy(ALPHA_UNFOCUSED_PLACEHOLDER),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        placeholder = {
            Text(
                text = placeholder,
                textAlign = TextAlign.Center
            )
        }
    )
}

private const val ALPHA_UNFOCUSED_PLACEHOLDER = 0.6f
private const val ALPHA_FOCUSED_PLACEHOLDER = 0.85f
