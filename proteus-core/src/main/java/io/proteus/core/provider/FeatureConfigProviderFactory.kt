package io.proteus.core.provider

import io.proteus.core.exceptions.IllegalConfigOwnerException

interface FeatureConfigProviderFactory {

    @Throws(IllegalConfigOwnerException::class)
    fun getProvider(owner: FeatureConfigOwner): FeatureConfigProvider
}
