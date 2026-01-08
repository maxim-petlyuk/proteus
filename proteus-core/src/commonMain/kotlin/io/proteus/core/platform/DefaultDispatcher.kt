package io.proteus.core.platform

import kotlin.coroutines.CoroutineContext

expect object DefaultDispatcher {

    val context: CoroutineContext
}