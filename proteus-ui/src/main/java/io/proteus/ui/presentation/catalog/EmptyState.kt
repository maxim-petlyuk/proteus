package io.proteus.ui.presentation.catalog

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.proteus.ui.R

@Composable
internal fun EmptySearchState(
    query: String,
    modifier: Modifier = Modifier
) {
    EmptyStateLayout(
        modifier = modifier,
        icon = Icons.Default.Search,
        title = stringResource(R.string.feature_book_search_placeholder_title),
        message = stringResource(R.string.feature_book_search_placeholder_msg, query),
        enableAnimation = false
    )
}

@Composable
internal fun EmptyFeatureListState(
    modifier: Modifier = Modifier,
    message: String
) {
    EmptyStateLayout(
        modifier = modifier,
        icon = Icons.Default.Build,
        title = stringResource(R.string.feature_book_placeholder_title),
        message = message,
        enableAnimation = true
    )
}

@Composable
internal fun EmptyFeatureState(
    modifier: Modifier = Modifier,
    message: String
) {
    EmptyStateLayout(
        modifier = modifier,
        icon = Icons.Default.Build,
        title = stringResource(R.string.feature_editor_placeholder_title),
        message = message,
        enableAnimation = true
    )
}

@Composable
private fun EmptyStateLayout(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    message: String,
    enableAnimation: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition(label = "emptyState")

    val alpha by infiniteTransition.animateFloat(
        initialValue = if (enableAnimation) 0.3f else 0.4f,
        targetValue = if (enableAnimation) 0.7f else 0.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "iconPulse"
    )

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (enableAnimation) 5f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "iconRotation"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
                .alpha(alpha)
                .graphicsLayer {
                    rotationZ = rotation
                },
            tint = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            textAlign = TextAlign.Center,
            lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.4f
        )
    }
}