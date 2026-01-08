package io.proteus.core.provider

import io.proteus.core.platform.DefaultDispatcher
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class SynchronousFeatureConfigProvider(
    private val suspendProvider: FeatureConfigProvider,
    private val timeout: Duration = 30.seconds,
    private val coroutineContext: CoroutineContext = DefaultDispatcher.context
) {

    fun getBoolean(featureKey: String): Boolean {
        return runBlocking(coroutineContext) {
            withTimeout(timeout) {
                suspendProvider.getBoolean(featureKey)
            }
        }
    }

    fun getString(featureKey: String): String {
        return runBlocking(coroutineContext) {
            withTimeout(timeout) {
                suspendProvider.getString(featureKey)
            }
        }
    }

    fun getLong(featureKey: String): Long {
        return runBlocking(coroutineContext) {
            withTimeout(timeout) {
                suspendProvider.getLong(featureKey)
            }
        }
    }

    fun getDouble(featureKey: String): Double {
        return runBlocking(coroutineContext) {
            withTimeout(timeout) {
                suspendProvider.getDouble(featureKey)
            }
        }
    }
}