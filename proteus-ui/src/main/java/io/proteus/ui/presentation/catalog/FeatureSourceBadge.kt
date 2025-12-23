package io.proteus.ui.presentation.catalog

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.proteus.ui.R
import io.proteus.ui.presentation.theme.MockBadgeBackgroundDark
import io.proteus.ui.presentation.theme.MockBadgeBackgroundLight
import io.proteus.ui.presentation.theme.MockBadgeContentDark
import io.proteus.ui.presentation.theme.MockBadgeContentLight
import io.proteus.ui.presentation.theme.RemoteBadgeBackgroundDark
import io.proteus.ui.presentation.theme.RemoteBadgeBackgroundLight
import io.proteus.ui.presentation.theme.RemoteBadgeContentDark
import io.proteus.ui.presentation.theme.RemoteBadgeContentLight
import kotlinx.coroutines.delay

@Composable
internal fun FeatureSourceBadge(
    modifier: Modifier = Modifier,
    isRemote: Boolean = false,
    triggerPulse: Boolean = false,
    paddingValues: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
) {
    var isPulsing by remember { mutableStateOf(false) }

    LaunchedEffect(triggerPulse) {
        if (triggerPulse) {
            isPulsing = true
            delay(600)
            isPulsing = false
        }
    }

    val pulseScale by animateFloatAsState(
        targetValue = if (isPulsing) 1.15f else 1.0f,
        animationSpec = tween(300),
        label = "pulse_scale"
    )

    val isDarkTheme = isSystemInDarkTheme()

    val backgroundColor by animateColorAsState(
        targetValue = when {
            isRemote && isDarkTheme -> RemoteBadgeBackgroundDark
            isRemote -> RemoteBadgeBackgroundLight
            isDarkTheme -> MockBadgeBackgroundDark
            else -> MockBadgeBackgroundLight
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "background_color"
    )

    val contentColor by animateColorAsState(
        targetValue = when {
            isRemote && isDarkTheme -> RemoteBadgeContentDark
            isRemote -> RemoteBadgeContentLight
            isDarkTheme -> MockBadgeContentDark
            else -> MockBadgeContentLight
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "content_color"
    )

    val icon = when {
        isRemote -> painterResource(R.drawable.ic_cloud)
        else -> painterResource(R.drawable.ic_devices_wearables)
    }

    val text = stringResource(if (isRemote) R.string.feature_book_source_remote else R.string.feature_book_source_mock)

    Surface(
        modifier = modifier.scale(pulseScale),
        shape = RoundedCornerShape(50),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier.padding(paddingValues),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = icon,
                contentDescription = "$text source",
                tint = contentColor,
                modifier = Modifier.size(16.dp)
            )

            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = contentColor,
                letterSpacing = 0.5.sp
            )
        }
    }
}