package io.proteus.ui.presentation.catalog

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.atLeastWrapContent
import io.proteus.ui.R
import io.proteus.ui.domain.entity.FeatureNote
import io.proteus.ui.utils.rememberHapticFeedback
import io.proteus.ui.utils.safeSharedTransition

internal fun LazyListScope.featureCatalog(
    state: FeatureCatalogState,
    onFeatureNoteClick: (FeatureNote<*>) -> Unit = {},
) {
    items(
        items = state.featureBook,
        key = { it.feature.key }
    ) { featureNote ->
        FeatureCard(
            modifier = Modifier.safeSharedTransition(key = "feature-${featureNote.feature.key}"),
            featureNote = featureNote,
            highlightRanges = state.getHighlightRanges(featureNote.feature.key),
            onFeatureNoteClick = onFeatureNoteClick
        )
    }
}

@Composable
internal fun FeatureCard(
    modifier: Modifier = Modifier,
    featureNote: FeatureNote<*>,
    highlightRanges: List<IntRange> = emptyList(),
    onFeatureNoteClick: ((FeatureNote<*>) -> Unit)? = null,
    horizontalPadding: Dp = 16.dp,
    verticalPadding: Dp = 12.dp,
    rowPaddingVertical: Dp = 8.dp,
    columnPaddingHorizontal: Dp = 8.dp,
    dividerColor: Color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.8f),
    titleBackgroundColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
) {
    val haptic = rememberHapticFeedback()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .dropShadow(
                shape = MaterialTheme.shapes.small,
                shadow = Shadow(
                    radius = 8.dp,
                    spread = 2.dp,
                    color = Color(0x40000000),
                    offset = DpOffset(x = 4.dp, 4.dp)
                )
            )
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = MaterialTheme.shapes.small
            )
            .clickable(
                indication = ripple(color = Color.Gray),
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    haptic.cardTap()
                    onFeatureNoteClick?.invoke(featureNote)
                },
                enabled = onFeatureNoteClick != null
            )
            .clip(MaterialTheme.shapes.small)
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxWidth()
        ) {
            val (keyTitle, keyValue, keyBg, keyDivider) = createRefs()
            val (overrideTitle, overrideIcon, overrideBg, overrideDivider) = createRefs()
            val (remoteConfigTitle, remoteConfigValue, remoteConfigBg, remoteConfigDivider) = createRefs()
            val (localConfigTitle, localConfigValue, localConfigBg, localConfigDivider) = createRefs()
            val (featureOwnerTitle, featureOwnerValue, featureOwnerBg, titlesVerticalDivider) = createRefs()

            val titlesEndBarrier = createEndBarrier(
                keyTitle,
                overrideTitle,
                remoteConfigTitle,
                localConfigTitle,
                featureOwnerTitle
            )

            val keyBottomBarrier = createBottomBarrier(
                keyTitle,
                keyValue
            )

            val overrideBottomBarrier = createBottomBarrier(
                overrideTitle,
                overrideIcon
            )

            val remoteConfigBottomBarrier = createBottomBarrier(
                remoteConfigTitle,
                remoteConfigValue
            )

            val localConfigBottomBarrier = createBottomBarrier(
                localConfigTitle,
                localConfigValue
            )

            VerticalDivider(
                thickness = 0.5.dp,
                color = dividerColor,
                modifier = Modifier
                    .constrainAs(titlesVerticalDivider) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(titlesEndBarrier)
                        height = Dimension.fillToConstraints
                    }
            )

            /* section key */
            Box(
                modifier = Modifier
                    .background(titleBackgroundColor)
                    .constrainAs(keyBg) {
                        top.linkTo(parent.top)
                        bottom.linkTo(keyDivider.top)
                        start.linkTo(parent.start)
                        end.linkTo(titlesVerticalDivider.start)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
            )

            FeatureAttributeKey(
                title = R.string.feature_book_key_title,
                modifier = Modifier
                    .padding(
                        start = horizontalPadding,
                        end = columnPaddingHorizontal,
                        top = rowPaddingVertical,
                        bottom = rowPaddingVertical
                    )
                    .constrainAs(keyTitle) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
            )

            HighlightedText(
                text = featureNote.feature.key,
                highlightRanges = highlightRanges,
                modifier = Modifier.constrainAs(keyValue) {
                    baseline.linkTo(keyTitle.baseline)
                    start.linkTo(titlesVerticalDivider.end, margin = columnPaddingHorizontal)
                    end.linkTo(parent.end, margin = horizontalPadding)
                    width = Dimension.fillToConstraints
                }
            )

            HorizontalDivider(
                thickness = 0.5.dp,
                color = dividerColor,
                modifier = Modifier
                    .constrainAs(keyDivider) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(keyBottomBarrier)
                    }
            )

            /* section override */
            Box(
                modifier = Modifier
                    .background(titleBackgroundColor)
                    .constrainAs(overrideBg) {
                        top.linkTo(keyDivider.bottom)
                        bottom.linkTo(overrideDivider.top)
                        start.linkTo(parent.start)
                        end.linkTo(titlesVerticalDivider.start)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
            )

            FeatureAttributeKey(
                title = R.string.feature_book_overridden_title,
                modifier = Modifier
                    .padding(
                        start = horizontalPadding,
                        end = columnPaddingHorizontal,
                        top = rowPaddingVertical,
                        bottom = rowPaddingVertical
                    )
                    .constrainAs(overrideTitle) {
                        top.linkTo(keyDivider.bottom)
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
                        top.linkTo(overrideTitle.top)
                        bottom.linkTo(overrideTitle.bottom)
                        start.linkTo(titlesVerticalDivider.end, margin = columnPaddingHorizontal)
                    },
                painter = painterResource(id = activatedStatusIcon),
                contentDescription = "Diamonds"
            )

            HorizontalDivider(
                thickness = 0.5.dp,
                color = dividerColor,
                modifier = Modifier
                    .constrainAs(overrideDivider) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(overrideBottomBarrier)
                    }
            )

            /* section remote value */
            Box(
                modifier = Modifier
                    .background(titleBackgroundColor)
                    .constrainAs(remoteConfigBg) {
                        top.linkTo(overrideDivider.bottom)
                        bottom.linkTo(remoteConfigDivider.top)
                        start.linkTo(parent.start)
                        end.linkTo(titlesVerticalDivider.start)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
            )

            FeatureAttributeKey(
                title = R.string.feature_book_remote_value_title,
                modifier = Modifier
                    .padding(
                        start = horizontalPadding,
                        end = columnPaddingHorizontal,
                        top = rowPaddingVertical,
                        bottom = rowPaddingVertical
                    )
                    .constrainAs(remoteConfigTitle) {
                        top.linkTo(overrideDivider.bottom)
                        start.linkTo(parent.start)
                    }
            )

            FeatureAttributeTextValue(
                text = featureNote.remoteConfigValue,
                modifier = Modifier.constrainAs(remoteConfigValue) {
                    baseline.linkTo(remoteConfigTitle.baseline)
                    start.linkTo(titlesVerticalDivider.end, margin = columnPaddingHorizontal)
                    end.linkTo(parent.end, margin = horizontalPadding)
                    width = Dimension.fillToConstraints
                }
            )

            HorizontalDivider(
                thickness = 0.5.dp,
                color = dividerColor,
                modifier = Modifier
                    .constrainAs(remoteConfigDivider) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(remoteConfigBottomBarrier)
                    }
            )

            /* section local value */
            Box(
                modifier = Modifier
                    .background(titleBackgroundColor)
                    .constrainAs(localConfigBg) {
                        top.linkTo(remoteConfigDivider.bottom)
                        bottom.linkTo(localConfigDivider.top)
                        start.linkTo(parent.start)
                        end.linkTo(titlesVerticalDivider.start)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
            )

            FeatureAttributeKey(
                title = R.string.feature_book_local_value_title,
                modifier = Modifier
                    .padding(start = horizontalPadding, end = columnPaddingHorizontal)
                    .constrainAs(localConfigTitle) {
                        top.linkTo(remoteConfigDivider.bottom, margin = rowPaddingVertical)
                        start.linkTo(parent.start)
                        end.linkTo(titlesEndBarrier)
                        width = Dimension.fillToConstraints.atLeastWrapContent
                    }
            )

            FeatureAttributeTextValue(
                text = featureNote.mockedConfigValue ?: "-/-",
                modifier = Modifier.constrainAs(localConfigValue) {
                    baseline.linkTo(localConfigTitle.baseline)
                    start.linkTo(titlesVerticalDivider.end, margin = columnPaddingHorizontal)
                    end.linkTo(parent.end, margin = horizontalPadding)
                    width = Dimension.fillToConstraints
                }
            )

            HorizontalDivider(
                thickness = 0.5.dp,
                color = dividerColor,
                modifier = Modifier
                    .constrainAs(localConfigDivider) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(localConfigBottomBarrier, margin = rowPaddingVertical)
                    }
            )

            /* section feature owner */
            Box(
                modifier = Modifier
                    .background(titleBackgroundColor)
                    .constrainAs(featureOwnerBg) {
                        top.linkTo(localConfigDivider.bottom)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(titlesVerticalDivider.start)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
            )

            FeatureAttributeKey(
                title = R.string.feature_book_owner_title,
                modifier = Modifier
                    .padding(start = horizontalPadding, end = columnPaddingHorizontal)
                    .constrainAs(featureOwnerTitle) {
                        top.linkTo(localConfigDivider.bottom, margin = rowPaddingVertical)
                        bottom.linkTo(parent.bottom, margin = verticalPadding)
                        start.linkTo(parent.start)
                        width = Dimension.fillToConstraints.atLeastWrapContent
                    }
            )

            FeatureAttributeTextValue(
                text = featureNote.serviceOwner,
                modifier = Modifier.constrainAs(featureOwnerValue) {
                    baseline.linkTo(featureOwnerTitle.baseline)
                    start.linkTo(titlesVerticalDivider.end, margin = columnPaddingHorizontal)
                    end.linkTo(parent.end, margin = horizontalPadding)
                    width = Dimension.fillToConstraints
                }
            )
        }
    }
}

@Composable
private fun FeatureAttributeKey(
    modifier: Modifier,
    @StringRes title: Int
) {
    Text(
        text = stringResource(id = title),
        style = MaterialTheme.typography.labelLarge,
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
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Start,
        modifier = modifier,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}
