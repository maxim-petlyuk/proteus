package io.proteus.firebase

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import io.proteus.core.provider.FeatureConfigProvider
import io.proteus.core.provider.FeatureContext

class FirebaseFeatureConfigProvider : FeatureConfigProvider {

    private val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    override fun getBoolean(feature: FeatureContext<Boolean>): Boolean {
        return firebaseRemoteConfig.getBoolean(feature.key)
    }

    override fun getString(feature: FeatureContext<String>): String {
        return firebaseRemoteConfig.getString(feature.key)
    }

    override fun getLong(feature: FeatureContext<Long>): Long {
        return firebaseRemoteConfig.getLong(feature.key)
    }

    override fun getDouble(feature: FeatureContext<Double>): Double {
        return firebaseRemoteConfig.getDouble(feature.key)
    }
}
