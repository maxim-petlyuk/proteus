package io.proteus.sample.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DemoDarkColorScheme = darkColorScheme(
    primary = primaryDemoDark,
    onPrimary = onPrimaryDemoDark,
    primaryContainer = primaryContainerDemoDark,
    onPrimaryContainer = onPrimaryContainerDemoDark,
    secondary = secondaryDemoDark,
    onSecondary = onSecondaryDemoDark,
    secondaryContainer = secondaryContainerDemoDark,
    onSecondaryContainer = onSecondaryContainerDemoDark,
    tertiary = tertiaryDemoDark,
    onTertiary = onTertiaryDemoDark,
    tertiaryContainer = tertiaryContainerDemoDark,
    onTertiaryContainer = onTertiaryContainerDemoDark,
    error = errorDemoDark,
    onError = onErrorDemoDark,
    errorContainer = errorContainerDemoDark,
    onErrorContainer = onErrorContainerDemoDark,
    background = backgroundDemoDark,
    onBackground = onBackgroundDemoDark,
    surface = surfaceDemoDark,
    onSurface = onSurfaceDemoDark,
    surfaceVariant = surfaceVariantDemoDark,
    onSurfaceVariant = onSurfaceVariantDemoDark,
    outline = outlineDemoDark,
    outlineVariant = outlineVariantDemoDark,
    scrim = scrimDemoDark,
    inverseSurface = inverseSurfaceDemoDark,
    inverseOnSurface = inverseOnSurfaceDemoDark,
    inversePrimary = inversePrimaryDemoDark,
    surfaceDim = surfaceDimDemoDark,
    surfaceBright = surfaceBrightDemoDark,
    surfaceContainerLowest = surfaceContainerLowestDemoDark,
    surfaceContainerLow = surfaceContainerLowDemoDark,
    surfaceContainer = surfaceContainerDemoDark,
    surfaceContainerHigh = surfaceContainerHighDemoDark,
    surfaceContainerHighest = surfaceContainerHighestDemoDark,
)

private val DemoLightColorScheme = lightColorScheme(
    primary = primaryDemoLight,
    onPrimary = onPrimaryDemoLight,
    primaryContainer = primaryContainerDemoLight,
    onPrimaryContainer = onPrimaryContainerDemoLight,
    secondary = secondaryDemoLight,
    onSecondary = onSecondaryDemoLight,
    secondaryContainer = secondaryContainerDemoLight,
    onSecondaryContainer = onSecondaryContainerDemoLight,
    tertiary = tertiaryDemoLight,
    onTertiary = onTertiaryDemoLight,
    tertiaryContainer = tertiaryContainerDemoLight,
    onTertiaryContainer = onTertiaryContainerDemoLight,
    error = errorDemoLight,
    onError = onErrorDemoLight,
    errorContainer = errorContainerDemoLight,
    onErrorContainer = onErrorContainerDemoLight,
    background = backgroundDemoLight,
    onBackground = onBackgroundDemoLight,
    surface = surfaceDemoLight,
    onSurface = onSurfaceDemoLight,
    surfaceVariant = surfaceVariantDemoLight,
    onSurfaceVariant = onSurfaceVariantDemoLight,
    outline = outlineDemoLight,
    outlineVariant = outlineVariantDemoLight,
    scrim = scrimDemoLight,
    inverseSurface = inverseSurfaceDemoLight,
    inverseOnSurface = inverseOnSurfaceDemoLight,
    inversePrimary = inversePrimaryDemoLight,
    surfaceDim = surfaceDimDemoLight,
    surfaceBright = surfaceBrightDemoLight,
    surfaceContainerLowest = surfaceContainerLowestDemoLight,
    surfaceContainerLow = surfaceContainerLowDemoLight,
    surfaceContainer = surfaceContainerDemoLight,
    surfaceContainerHigh = surfaceContainerHighDemoLight,
    surfaceContainerHighest = surfaceContainerHighestDemoLight,
)

// Original color schemes (keep for compatibility)
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun SampleConfigTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    useDemoTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        useDemoTheme && darkTheme -> DemoDarkColorScheme
        useDemoTheme && !darkTheme -> DemoLightColorScheme
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}