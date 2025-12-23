package io.proteus.ui.presentation.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.proteus.ui.presentation.theme.ProteusTheme

@Preview(name = "Source Badge", showBackground = true)
@Composable
private fun FeatureSourceBadgePreview() {
    ProteusTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FeatureSourceBadge(isRemote = true)
        }
    }
}

@Preview(name = "Source Badge Dark", showBackground = true)
@Composable
private fun FeatureSourceBadgeDarkPreview() {
    ProteusTheme(darkTheme = true) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FeatureSourceBadge(isRemote = true)
            FeatureSourceBadge(isRemote = false)
        }
    }
}