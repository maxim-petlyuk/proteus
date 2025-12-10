package io.proteus.ui.presentation.catalog

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import io.proteus.ui.domain.entity.FeatureNote
import io.proteus.ui.presentation.theme.ProteusTheme

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun StringFeatureNotePreview(
    @PreviewParameter(StringFeatureNotePreviewProvider::class)
    featureNote: FeatureNote<*>
) {
    PreviewContainer {
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
    PreviewContainer {
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
    PreviewContainer {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            featureCatalog(featureBook = featureBook)
        }
    }
}

@Composable
private fun PreviewContainer(
    content: @Composable BoxScope.() -> Unit
) {
    ProteusTheme {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            content = content
        )
    }
}