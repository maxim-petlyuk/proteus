package io.proteus.firebase

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import io.proteus.core.provider.FeatureConfigProvider

class FirebaseFeatureConfigProvider : FeatureConfigProvider {

    private val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    override fun getBoolean(featureKey: String): Boolean {
        return firebaseRemoteConfig.getBoolean(featureKey)
    }

    override fun getString(featureKey: String): String {
        return firebaseRemoteConfig.getString(featureKey)
    }

    override fun getLong(featureKey: String): Long {
        return firebaseRemoteConfig.getLong(featureKey)
    }

    override fun getDouble(featureKey: String): Double {
        return firebaseRemoteConfig.getDouble(featureKey)
    }
}
