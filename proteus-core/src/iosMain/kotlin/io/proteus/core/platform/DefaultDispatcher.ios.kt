package io.proteus.core.platform

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

actual object DefaultDispatcher {
    actual val context: CoroutineContext = Dispatchers.Default
}