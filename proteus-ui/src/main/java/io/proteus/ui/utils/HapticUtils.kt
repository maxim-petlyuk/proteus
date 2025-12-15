package io.proteus.ui.utils

import android.os.Build
import android.view.HapticFeedbackConstants
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView

@Composable
fun rememberHapticFeedback(): HapticFeedback {
    val view = LocalView.current
    return remember(view) {
        HapticFeedback { type ->
            view.performHapticFeedback(type)
        }
    }
}

class HapticFeedback(private val perform: (Int) -> Unit) {

    fun cardTap() = perform(HapticFeedbackConstants.CONTEXT_CLICK)

    fun success() = perform(safeFeedbackAction({ HapticFeedbackConstants.CONFIRM }))

    fun error() = perform(safeFeedbackAction({ HapticFeedbackConstants.REJECT }))

    private fun safeFeedbackAction(hapticType: () -> Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            hapticType.invoke()
        } else {
            HapticFeedbackConstants.CONTEXT_CLICK
        }
    }
}
