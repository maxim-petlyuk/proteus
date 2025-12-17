package io.proteus.sample.ui.screens.demo.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.proteus.sample.R
import io.proteus.sample.data.FeatureSource
import io.proteus.sample.ui.theme.DemoGreen500
import io.proteus.sample.ui.theme.SampleConfigTheme

@Composable
fun SourceBadge(
    source: FeatureSource,
    triggerPulse: Boolean = false,
    modifier: Modifier = Modifier
) {
    var isPulsing by remember { mutableStateOf(false) }

    LaunchedEffect(triggerPulse) {
        if (triggerPulse) {
            isPulsing = true
            kotlinx.coroutines.delay(600) // Duration of pulse
            isPulsing = false
        }
    }

    val pulseScale by animateFloatAsState(
        targetValue = if (isPulsing) 1.15f else 1.0f,
        animationSpec = tween(300),
        label = "pulse_scale"
    )
    val backgroundColor by animateColorAsState(
        targetValue = when (source) {
            FeatureSource.REMOTE -> DemoGreen500.copy(alpha = 0.65f)
            FeatureSource.MOCK -> MaterialTheme.colorScheme.errorContainer
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "background_color"
    )

    val contentColor by animateColorAsState(
        targetValue = when (source) {
            FeatureSource.REMOTE -> Color.White
            FeatureSource.MOCK -> MaterialTheme.colorScheme.onErrorContainer
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "content_color"
    )

    val icon = when (source) {
        FeatureSource.REMOTE -> painterResource(R.drawable.ic_cloud)
        FeatureSource.MOCK -> painterResource(R.drawable.ic_devices_wearables)
    }

    Surface(
        modifier = modifier.scale(pulseScale),
        shape = RoundedCornerShape(50),
        color = backgroundColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = icon,
                contentDescription = "${source.name} source",
                tint = contentColor,
                modifier = Modifier.size(16.dp)
            )

            Text(
                text = source.name,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = contentColor,
                letterSpacing = 0.5.sp
            )
        }
    }
}

private class SourcePreviewProvider : PreviewParameterProvider<FeatureSource> {
    override val values = sequenceOf(
        FeatureSource.REMOTE,
        FeatureSource.MOCK
    )
}

@Preview(
    name = "Source Badge",
    showBackground = true
)
@Composable
private fun SourceBadgePreview(
    @PreviewParameter(SourcePreviewProvider::class) source: FeatureSource
) {
    SampleConfigTheme {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SourceBadge(source = source)
        }
    }
}

@Preview(
    name = "Source Badge Dark",
    showBackground = true
)
@Composable
private fun SourceBadgeDarkPreview() {
    SampleConfigTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SourceBadge(source = FeatureSource.REMOTE)
            SourceBadge(source = FeatureSource.MOCK)
        }
    }
}