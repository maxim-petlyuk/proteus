package io.proteus.sample.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Demo Screen Material 3 Color System
 *
 * Based on Dark Elegant design with perfect light theme inversion.
 * Inspired by proteus-ui beige palette but optimized for demo screen aesthetics.
 *
 * Primary: Deep brown/charcoal for sophisticated look
 * Secondary: Warm beige for elegance
 * Tertiary: Accent colors for highlights
 *
 * Colors follow Material 3 naming conventions and contrast ratios.
 */

// === Core Color Palette ===
val DemoGray50 = Color(0xFFF8F7F5)
val DemoGray100 = Color(0xFFF0EFED)
val DemoGray200 = Color(0xFFE1DDD8)
val DemoGray300 = Color(0xFFCAC4BC)
val DemoGray400 = Color(0xFFADA49A)
val DemoGray500 = Color(0xFF8F847A)
val DemoGray600 = Color(0xFF726960)
val DemoGray700 = Color(0xFF5A514A)
val DemoGray800 = Color(0xFF3E362F)
val DemoGray900 = Color(0xFF2A231D)
val DemoGray950 = Color(0xFF1A1611)

val DemoBeige50 = Color(0xFFFCF9F6)
val DemoBeige100 = Color(0xFFF7F1EA)
val DemoBeige200 = Color(0xFFEEE2D4)
val DemoBeige300 = Color(0xFFE0CDBA)
val DemoBeige400 = Color(0xFFCFB399)
val DemoBeige500 = Color(0xFFBE9B7E)
val DemoBeige600 = Color(0xFFAA8467)
val DemoBeige700 = Color(0xFF8F6D56)
val DemoBeige800 = Color(0xFF745849)
val DemoBeige900 = Color(0xFF5E483D)

val DemoAccent50 = Color(0xFFFFF8E1)
val DemoAccent100 = Color(0xFFFFECB3)
val DemoAccent200 = Color(0xFFFFE082)
val DemoAccent300 = Color(0xFFFFD54F)
val DemoAccent400 = Color(0xFFFFCA28)
val DemoAccent500 = Color(0xFFFFC107)
val DemoAccent600 = Color(0xFFFFB300)
val DemoAccent700 = Color(0xFFF57C00)
val DemoAccent800 = Color(0xFFEF6C00)
val DemoAccent900 = Color(0xFFE65100)

// === Green Colors for REMOTE badge ===
val DemoGreen50 = Color(0xFFE8F5E8)
val DemoGreen100 = Color(0xFFC8E6C8)
val DemoGreen200 = Color(0xFFA5D6A5)
val DemoGreen300 = Color(0xFF81C784)
val DemoGreen400 = Color(0xFF66BB6A)
val DemoGreen500 = Color(0xFF4CAF50)
val DemoGreen600 = Color(0xFF43A047)
val DemoGreen700 = Color(0xFF388E3C)
val DemoGreen800 = Color(0xFF2E7D32)
val DemoGreen900 = Color(0xFF1B5E20)

// === Brown Colors for Configurator Button ===
val ConfiguratorBrown600 = Color(0xFF795548) // Brown base
val ConfiguratorBrown700 = Color(0xFF8D6E63) // Brown gradient end
val ConfiguratorBrownDark = Color(0xFF3E2723) // Dark brown for tooltip

// === Semantic Colors for MaterialTheme integration ===
// These will be accessed via MaterialTheme.colorScheme.primary (for REMOTE)
// and MaterialTheme.colorScheme.error (for MOCK)

// === DARK THEME (Primary Theme for Demo Screen) ===
val primaryDemoDark = DemoBeige300
val onPrimaryDemoDark = DemoGray900
val primaryContainerDemoDark = DemoGray800
val onPrimaryContainerDemoDark = DemoBeige100

val secondaryDemoDark = DemoBeige400
val onSecondaryDemoDark = DemoGray900
val secondaryContainerDemoDark = DemoGray700
val onSecondaryContainerDemoDark = DemoBeige200

val tertiaryDemoDark = DemoAccent300
val onTertiaryDemoDark = DemoGray900
val tertiaryContainerDemoDark = DemoGray700
val onTertiaryContainerDemoDark = DemoAccent100

val errorDemoDark = Color(0xFFFFB4AB)
val onErrorDemoDark = Color(0xFF690005)
val errorContainerDemoDark = Color(0xFF93000A)
val onErrorContainerDemoDark = Color(0xFFFFDAD6)

val backgroundDemoDark = DemoGray950
val onBackgroundDemoDark = DemoBeige100

val surfaceDemoDark = DemoGray950
val onSurfaceDemoDark = DemoBeige100
val surfaceVariantDemoDark = DemoGray800
val onSurfaceVariantDemoDark = DemoBeige200

val outlineDemoDark = DemoGray600
val outlineVariantDemoDark = DemoGray700

val scrimDemoDark = Color(0xFF000000)

val inverseSurfaceDemoDark = DemoBeige100
val inverseOnSurfaceDemoDark = DemoGray900
val inversePrimaryDemoDark = DemoGray700

val surfaceDimDemoDark = DemoGray950
val surfaceBrightDemoDark = DemoGray700
val surfaceContainerLowestDemoDark = Color(0xFF0F0D0A)
val surfaceContainerLowDemoDark = DemoGray950
val surfaceContainerDemoDark = DemoGray800
val surfaceContainerHighDemoDark = DemoGray700
val surfaceContainerHighestDemoDark = DemoGray600

// === LIGHT THEME (Inverted for Light Mode) ===
val primaryDemoLight = DemoGray700
val onPrimaryDemoLight = Color(0xFFFFFFFF)
val primaryContainerDemoLight = DemoBeige200
val onPrimaryContainerDemoLight = DemoGray900

val secondaryDemoLight = DemoGray600
val onSecondaryDemoLight = Color(0xFFFFFFFF)
val secondaryContainerDemoLight = DemoBeige100
val onSecondaryContainerDemoLight = DemoGray800

val tertiaryDemoLight = DemoAccent700
val onTertiaryDemoLight = Color(0xFFFFFFFF)
val tertiaryContainerDemoLight = DemoAccent100
val onTertiaryContainerDemoLight = DemoAccent900

val errorDemoLight = Color(0xFFBA1A1A)
val onErrorDemoLight = Color(0xFFFFFFFF)
val errorContainerDemoLight = Color(0xFFFFDAD6)
val onErrorContainerDemoLight = Color(0xFF410002)

val backgroundDemoLight = Color(0xFFFFFFFF)
val onBackgroundDemoLight = DemoGray900

val surfaceDemoLight = Color(0xFFFFFFFF)
val onSurfaceDemoLight = DemoGray900
val surfaceVariantDemoLight = DemoBeige100
val onSurfaceVariantDemoLight = DemoGray700

val outlineDemoLight = DemoGray500
val outlineVariantDemoLight = DemoGray400

val scrimDemoLight = Color(0xFF000000)

val inverseSurfaceDemoLight = DemoGray800
val inverseOnSurfaceDemoLight = DemoBeige100
val inversePrimaryDemoLight = DemoBeige300

val surfaceDimDemoLight = DemoGray100
val surfaceBrightDemoLight = Color(0xFFFFFFFF)
val surfaceContainerLowestDemoLight = Color(0xFFFFFFFF)
val surfaceContainerLowDemoLight = DemoGray50
val surfaceContainerDemoLight = DemoGray100
val surfaceContainerHighDemoLight = DemoGray200
val surfaceContainerHighestDemoLight = DemoGray300

// === ORIGINAL THEME COLORS (Keep for compatibility) ===
val Purple80 = Color(0xFFACF1AC)
val PurpleGrey80 = Color(0xFF94DC43)
val Pink80 = Color(0xFFCDDC39)

val Purple40 = Color(0xFFA450A0)
val PurpleGrey40 = Color(0xFF5F4ABB)
val Pink40 = Color(0xFF7D5260)