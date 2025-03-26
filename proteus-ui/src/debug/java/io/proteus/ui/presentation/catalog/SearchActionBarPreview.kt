package io.proteus.ui.presentation.catalog

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
private fun EmptySearchPreview() {
    SearchActionBar(
        searchQuery = ""
    )
}

@Preview
@Composable
private fun FilledSearchPreview() {
    SearchActionBar(
        searchQuery = "sol"
    )
}
