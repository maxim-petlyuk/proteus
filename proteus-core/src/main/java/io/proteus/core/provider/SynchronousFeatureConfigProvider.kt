package io.proteus.core.provider

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class SynchronousFeatureConfigProvider(
    private val suspendProvider: FeatureConfigProvider,
    private val coroutineContext: CoroutineContext = Dispatchers.IO,
    private val timeout: Duration = 30.seconds
) {

    fun getBoolean(featureKey: String): Boolean {
        return runBlocking(context = coroutineContext) {
            withTimeout(timeout) {
                suspendProvider.getBoolean(featureKey)
            }
        }
    }

    fun getString(featureKey: String): String {
        return runBlocking(context = coroutineContext) {
            withTimeout(timeout) {
                suspendProvider.getString(featureKey)
            }
        }
    }

    fun getLong(featureKey: String): Long {
        return runBlocking(context = coroutineContext) {
            withTimeout(timeout) {
                suspendProvider.getLong(featureKey)
            }
        }
    }

    fun getDouble(featureKey: String): Double {
        return runBlocking(context = coroutineContext) {
            withTimeout(timeout) {
                suspendProvider.getDouble(featureKey)
            }
        }
    }
}