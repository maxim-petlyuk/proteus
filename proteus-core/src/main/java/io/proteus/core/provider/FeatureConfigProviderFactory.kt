package io.proteus.core.provider

import io.proteus.core.exceptions.IllegalConfigOwnerException

interface FeatureConfigProviderFactory {

    @Throws(IllegalConfigOwnerException::class)
    fun getProvider(featureKey: String): FeatureConfigProvider

    @Throws(IllegalConfigOwnerException::class)
    fun getProviderTag(featureKey: String): String
}
