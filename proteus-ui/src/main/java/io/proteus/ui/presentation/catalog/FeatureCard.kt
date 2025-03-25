package io.proteus.ui.presentation.catalog

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import io.proteus.ui.R
import io.proteus.ui.domain.entity.FeatureNote
import io.proteus.ui.presentation.theme.ProteusTheme

internal fun LazyListScope.featureCatalog(
    featureBook: List<FeatureNote<*>>,
    onFeatureNoteClick: (FeatureNote<*>) -> Unit = {},
) {
    items(
        items = featureBook,
        key = { it.feature.key }
    ) {
        FeatureCard(
            featureNote = it,
            onFeatureNoteClick = onFeatureNoteClick
        )
    }
}

@Composable
internal fun FeatureCard(
    modifier: Modifier = Modifier,
    featureNote: FeatureNote<*>,
    onFeatureNoteClick: ((FeatureNote<*>) -> Unit)? = null
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable(
                indication = ripple(color = Color.Gray),
                interactionSource = remember { MutableInteractionSource() },
                onClick = { onFeatureNoteClick?.invoke(featureNote) },
                enabled = onFeatureNoteClick != null
            )
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth()
        ) {
            val (
                keyTitle,
                keyValue,
                overrideTitle,
                overrideIcon,
                remoteConfigTitle,
                remoteConfigValue,
                localConfigTitle,
                localConfigValue,
                defaultConfigTitle,
                defaultConfigValue,
                featureOwnerTitle,
                featureOwnerValue
            ) = createRefs()

            val verticalBarrier = createEndBarrier(
                keyTitle,
                overrideTitle,
                remoteConfigTitle,
                localConfigTitle,
                defaultConfigTitle,
                featureOwnerTitle
            )

            FeatureAttributeKey(
                title = R.string.feature_book_key_title,
                modifier = Modifier.constrainAs(keyTitle) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
            )

            FeatureAttributeTextValue(
                text = featureNote.feature.key,
                modifier = Modifier.constrainAs(keyValue) {
                    baseline.linkTo(keyTitle.baseline)
                    start.linkTo(verticalBarrier, margin = 8.dp)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
            )

            FeatureAttributeKey(
                title = R.string.feature_book_overridden_title,
                modifier = Modifier.constrainAs(overrideTitle) {
                    top.linkTo(keyTitle.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                }
            )

            val activatedStatusIcon = if (featureNote.isOverrideActivated) {
                R.drawable.ic_remote_feature_override_activated
            } else {
                R.drawable.ic_remote_feature_override_deactivated
            }

            Image(
                modifier = Modifier
                    .size(16.dp)
                    .constrainAs(overrideIcon) {
                        bottom.linkTo(overrideTitle.bottom)
                        start.linkTo(verticalBarrier, margin = 8.dp)
                    },
                painter = painterResource(id = activatedStatusIcon),
                contentDescription = "Diamonds"
            )

            FeatureAttributeKey(
                title = R.string.feature_book_remote_value_title,
                modifier = Modifier.constrainAs(remoteConfigTitle) {
                    top.linkTo(overrideTitle.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                }
            )

            FeatureAttributeTextValue(
                text = featureNote.remoteConfigValue,
                modifier = Modifier.constrainAs(remoteConfigValue) {
                    baseline.linkTo(remoteConfigTitle.baseline)
                    start.linkTo(verticalBarrier, margin = 8.dp)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
            )

            FeatureAttributeKey(
                title = R.string.feature_book_local_value_title,
                modifier = Modifier.constrainAs(localConfigTitle) {
                    top.linkTo(remoteConfigTitle.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                }
            )

            FeatureAttributeTextValue(
                text = featureNote.mockedConfigValue ?: "-/-",
                modifier = Modifier.constrainAs(localConfigValue) {
                    baseline.linkTo(localConfigTitle.baseline)
                    start.linkTo(verticalBarrier, margin = 8.dp)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
            )

            FeatureAttributeKey(
                title = R.string.feature_book_default_value_title,
                modifier = Modifier.constrainAs(defaultConfigTitle) {
                    top.linkTo(localConfigTitle.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                }
            )

            FeatureAttributeTextValue(
                text = featureNote.feature.defaultValue.toString(),
                modifier = Modifier.constrainAs(defaultConfigValue) {
                    baseline.linkTo(defaultConfigTitle.baseline)
                    start.linkTo(verticalBarrier, margin = 8.dp)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
            )

            FeatureAttributeKey(
                title = R.string.feature_book_owner_title,
                modifier = Modifier.constrainAs(featureOwnerTitle) {
                    top.linkTo(defaultConfigTitle.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                }
            )

            FeatureAttributeTextValue(
                text = featureNote.feature.owner.toString(),
                modifier = Modifier.constrainAs(featureOwnerValue) {
                    baseline.linkTo(featureOwnerTitle.baseline)
                    start.linkTo(verticalBarrier, margin = 8.dp)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
            )
        }

        HorizontalDivider(
            thickness = 0.5.dp,
            color = Color.Gray,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .alpha(0.5f)
        )
    }
}

@Composable
private fun FeatureAttributeKey(
    modifier: Modifier,
    @StringRes title: Int
) {
    Text(
        text = stringResource(id = title),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier
    )
}

@Composable
private fun FeatureAttributeTextValue(
    modifier: Modifier,
    text: String,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Start,
        modifier = modifier,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

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
