package io.proteus.ui.utils

import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.proteus.ui.presentation.LocalAnimatedContentScope
import io.proteus.ui.presentation.LocalSharedTransitionScope

@Composable
internal fun Modifier.safeSharedTransition(key: String): Modifier {
    val sharedTransitionScope = LocalSharedTransitionScope.current
    val animatedContentScope = LocalAnimatedContentScope.current

    return if (sharedTransitionScope != null && animatedContentScope != null) {
        with(sharedTransitionScope) {
            this@safeSharedTransition.then(
                Modifier.sharedElement(
                    sharedContentState = rememberSharedContentState(key = key),
                    animatedVisibilityScope = animatedContentScope,
                    boundsTransform = { _, _ ->
                        tween(durationMillis = 600)
                    }
                )
            )
        }
    } else {
        this
    }
}
