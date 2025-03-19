package io.proteus.firebase

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import io.proteus.core.provider.Feature
import io.proteus.core.provider.FeatureConfigProvider

class FirebaseFeatureConfigProvider : FeatureConfigProvider {

    private val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    override fun getBoolean(feature: Feature<Boolean>): Boolean {
        return firebaseRemoteConfig.getBoolean(feature.key)
    }

    override fun getString(feature: Feature<String>): String {
        return firebaseRemoteConfig.getString(feature.key)
    }

    override fun getLong(feature: Feature<Long>): Long {
        return firebaseRemoteConfig.getLong(feature.key)
    }

    override fun getDouble(feature: Feature<Double>): Double {
        return firebaseRemoteConfig.getDouble(feature.key)
    }
}
