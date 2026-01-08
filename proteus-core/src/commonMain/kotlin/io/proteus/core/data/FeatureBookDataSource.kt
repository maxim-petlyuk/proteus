package io.proteus.core.data

import io.proteus.core.domain.FeatureContext

interface FeatureBookDataSource {

    suspend fun getFeatureBook(): Result<List<FeatureContext<*>>>
}

class StaticFeatureBookDataSource(
    private val features: List<FeatureContext<*>>
) : FeatureBookDataSource {

    override suspend fun getFeatureBook(): Result<List<FeatureContext<*>>> {
        return Result.success(features)
    }
}
