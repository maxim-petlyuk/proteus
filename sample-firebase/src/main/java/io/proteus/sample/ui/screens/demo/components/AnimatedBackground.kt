package io.proteus.sample.ui.screens.demo.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.proteus.sample.ui.theme.DemoGray950
import io.proteus.sample.ui.theme.DemoGray900
import io.proteus.sample.ui.theme.DemoBeige400
import io.proteus.sample.ui.theme.SampleConfigTheme
import kotlin.math.cos
import kotlin.math.sin

/**
 * Animated background component for the demo screen.
 *
 * Features:
 * - Dark gradient from #1A1611 to #2A231D
 * - Two floating circular gradients that rotate
 * - 20-second infinite rotation animation
 * - Subtle pattern overlay for depth
 */
@Composable
fun AnimatedBackground(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "background_animation")

    // 20-second rotation animation
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 20_000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    // Counter-rotation for second shape
    val counterRotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 25_000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "counter_rotation"
    )

    val density = LocalDensity.current

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .clip(RectangleShape)
    ) {
        // Base gradient background
        drawBaseGradient()

        // Floating circular gradients
        drawFloatingShape1(rotationAngle, density)
        drawFloatingShape2(counterRotationAngle, density)

        // Subtle pattern overlay
        drawPatternOverlay()
    }
}

/**
 * Draw the base dark gradient background
 */
private fun DrawScope.drawBaseGradient() {
    val gradient = Brush.verticalGradient(
        colors = listOf(
            DemoGray950, // #1A1611
            DemoGray900  // #2A231D
        ),
        startY = 0f,
        endY = size.height
    )

    drawRect(
        brush = gradient,
        size = size
    )
}

/**
 * Draw the first floating circular gradient shape
 */
private fun DrawScope.drawFloatingShape1(
    rotationAngle: Float,
    density: androidx.compose.ui.unit.Density
) {
    rotate(rotationAngle) {
        val centerX = size.width * 0.25f
        val centerY = size.height * 0.3f
        val radius = with(density) { 200.dp.toPx() }

        val gradient = Brush.radialGradient(
            colors = listOf(
                DemoBeige400.copy(alpha = 0.08f),
                DemoBeige400.copy(alpha = 0.04f),
                Color.Transparent
            ),
            center = Offset(centerX, centerY),
            radius = radius
        )

        drawCircle(
            brush = gradient,
            radius = radius,
            center = Offset(centerX, centerY)
        )
    }
}

/**
 * Draw the second floating circular gradient shape
 */
private fun DrawScope.drawFloatingShape2(
    counterRotationAngle: Float,
    density: androidx.compose.ui.unit.Density
) {
    rotate(counterRotationAngle) {
        val centerX = size.width * 0.75f
        val centerY = size.height * 0.7f
        val radius = with(density) { 150.dp.toPx() }

        val gradient = Brush.radialGradient(
            colors = listOf(
                DemoBeige400.copy(alpha = 0.06f),
                DemoBeige400.copy(alpha = 0.03f),
                Color.Transparent
            ),
            center = Offset(centerX, centerY),
            radius = radius
        )

        drawCircle(
            brush = gradient,
            radius = radius,
            center = Offset(centerX, centerY)
        )
    }
}

/**
 * Draw subtle pattern overlay for depth
 */
private fun DrawScope.drawPatternOverlay() {
    val patternColor = Color.White.copy(alpha = 0.02f)
    val spacing = 60f

    // Diagonal lines pattern
    var x = 0f
    while (x < size.width + spacing) {
        drawLine(
            color = patternColor,
            start = Offset(x, 0f),
            end = Offset(x + size.height, size.height),
            strokeWidth = 1f
        )
        x += spacing
    }
}

@Preview(
    showBackground = true,
    widthDp = 360,
    heightDp = 640
)
@Composable
private fun AnimatedBackgroundPreview() {
    SampleConfigTheme(darkTheme = true) {
        AnimatedBackground()
    }
}

@Preview(
    name = "Light Theme",
    showBackground = true,
    widthDp = 360,
    heightDp = 640
)
@Composable
private fun AnimatedBackgroundLightPreview() {
    SampleConfigTheme(darkTheme = false) {
        AnimatedBackground()
    }
}