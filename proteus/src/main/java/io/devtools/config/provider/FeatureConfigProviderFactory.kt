package io.devtools.config.provider

import io.devtools.config.exceptions.IllegalConfigOwnerException

interface FeatureConfigProviderFactory {

    @Throws(IllegalConfigOwnerException::class)
    fun getProvider(owner: FeatureConfigOwner): FeatureConfigProvider
}
