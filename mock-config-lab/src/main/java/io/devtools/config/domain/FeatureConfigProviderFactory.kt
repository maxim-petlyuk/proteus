package io.devtools.config.domain

import io.devtools.config.exceptions.IllegalConfigOwnerException

interface FeatureConfigProviderFactory {

    @Throws(IllegalConfigOwnerException::class)
    fun getProvider(owner: FeatureConfigOwner): FeatureConfigProvider
}
