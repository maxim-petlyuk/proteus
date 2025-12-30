package io.proteus.firebase

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import io.proteus.core.provider.FeatureConfigProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class FirebaseFeatureConfigProvider(
    private val coroutineContext: CoroutineContext = Dispatchers.IO
) : FeatureConfigProvider {

    private val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    override suspend fun getBoolean(featureKey: String): Boolean = withContext(coroutineContext) {
        firebaseRemoteConfig.getBoolean(featureKey)
    }

    override suspend fun getString(featureKey: String): String = withContext(coroutineContext) {
        firebaseRemoteConfig.getString(featureKey)
    }

    override suspend fun getLong(featureKey: String): Long = withContext(coroutineContext) {
        firebaseRemoteConfig.getLong(featureKey)
    }

    override suspend fun getDouble(featureKey: String): Double = withContext(coroutineContext) {
        firebaseRemoteConfig.getDouble(featureKey)
    }
}
