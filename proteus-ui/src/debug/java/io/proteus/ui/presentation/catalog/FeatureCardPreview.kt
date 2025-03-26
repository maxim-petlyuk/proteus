package io.proteus.ui.presentation.catalog

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import io.proteus.ui.domain.entity.FeatureNote
import io.proteus.ui.presentation.theme.ProteusTheme

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun StringFeatureNotePreview(
    @PreviewParameter(StringFeatureNotePreviewProvider::class)
    featureNote: FeatureNote<*>
) {
    ProteusTheme {
        FeatureCard(
            modifier = Modifier,
            featureNote = featureNote
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun LongFeatureNotePreview(
    @PreviewParameter(LongFeatureNotePreviewProvider::class)
    featureNote: FeatureNote<*>
) {
    ProteusTheme {
        FeatureCard(
            modifier = Modifier,
            featureNote = featureNote
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun FeatureBookPreview(
    @PreviewParameter(FeatureBookPreviewProvider::class)
    featureBook: List<FeatureNote<*>>
) {
    ProteusTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            featureCatalog(featureBook = featureBook)
        }
    }
}