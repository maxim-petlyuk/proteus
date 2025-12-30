# Proteus UI

Material Design 3 UI for runtime configuration overrides in Proteus.

## Overview

The `proteus-ui` module provides a polished, user-friendly interface for viewing and overriding configuration values during development and
testing. Built with Jetpack Compose and Material Design 3, it features a distinctive beige theme and comprehensive search capabilities.

## Installation

```kotlin
dependencies {
    implementation("io.github.maxim-petlyuk:proteus-ui:$version")
}
```

Or using the BOM (recommended):

```kotlin
dependencies {
    implementation(platform("io.github.maxim-petlyuk:proteus-bom:$version"))
    implementation("io.github.maxim-petlyuk:proteus-core")
    implementation("io.github.maxim-petlyuk:proteus-ui")
}
```

### Requirements

- Android SDK 23+
- Jetpack Compose BOM 2024.04.01 or higher
- Material 3 components

## Quick Start

### Launch the Feature Book UI

```kotlin
// From any Activity or Context
startActivity(Intent(this, FeatureBookActivity::class.java))
```

### Add to Your App's Debug Menu

```kotlin
// In your debug menu or developer settings
debugOptions.add(
    DebugOption(
        title = "Feature Flags",
        action = {
            startActivity(Intent(context, FeatureBookActivity::class.java))
        }
    )
)
```

## UI Components

### FeatureBookActivity

The main entry point - a full-screen activity that hosts the configuration UI.

Features:

- Automatic feature discovery from registered data sources
- Search functionality across all features
- Override status indicators
- Pull-to-refresh for configuration reload

## Theming

### Material Design 3 with Beige Palette

The UI features a distinctive beige color scheme:

```kotlin
// Primary colors
val Beige = Color(0xFFF5E6D3)
val DarkBeige = Color(0xFFD4A574)
val DeepBrown = Color(0xFF8B4513)

// Surface colors
val SurfaceBeige = Color(0xFFFAF6F0)
val SurfaceVariantBeige = Color(0xFFEDE0D4)
```

### Typography

Uses the Roboto font family throughout:

```kotlin
val Typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto_bold)),
        fontSize = 32.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.roboto_regular)),
        fontSize = 16.sp
    )
    // ...
)
```

## UI Features

### Search Functionality

- Real-time search across feature titles and descriptions
- Highlighted search matches
- Clear search button
- Keyboard handling with IME actions

### Override Indicators

- Visual badges for overridden features
- Different states: Overridden, Modified
- Color coding for quick identification

### Value Editing

- **Boolean**: Toggle switch
- **Long/Double**: Numeric input with validation
- **String**: Text field with multiline support

## Security Considerations

⚠️ **Important**: The override UI should only be available in debug builds.

## Screenshots

### Feature Catalog

- List view of all features
- Search bar at the top
- Override badges on modified features

### Configuration Editor

- Feature title and description
- Current value display
- Input control based on type
- Save/Reset/Cancel actions
- Validation feedback

### Empty States

- No features found messaging
- Search no results state
- Error states with retry options

## Performance Considerations

- **Lazy loading**: Feature lists use `LazyColumn` for efficient scrolling
- **Search debouncing**: 300ms delay for search queries
- **Image caching**: Icons and images are cached
- **State preservation**: Configuration state survives configuration changes

## Troubleshooting

### UI Not Showing

- Verify Proteus is initialized with feature data source
- Check that activities are registered in AndroidManifest.xml
- Ensure Compose dependencies are properly configured

### Theme Issues

- Verify Material 3 dependencies are included
- Check for theme conflicts with your app theme
- Ensure font resources are included

### Search Not Working

- Check that feature titles and descriptions are populated
- Verify search implementation has proper state management

## License

```
Copyright 2025 Maxim Petlyuk

Licensed under the Apache License, Version 2.0
```