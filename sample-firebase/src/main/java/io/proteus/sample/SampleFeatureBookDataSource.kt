package io.proteus.sample

import io.proteus.core.data.FeatureBookDataSource
import io.proteus.core.domain.Feature
import io.proteus.core.domain.FeatureContext

class SampleFeatureBookDataSource : FeatureBookDataSource {

    override suspend fun getFeatureBook(): Result<List<FeatureContext<*>>> {
        return Result.success(getFeatures())
    }

    private fun getFeatures(): List<FeatureContext<*>> {
        return listOf(
            Feature(
                key = "primary_server",
                defaultValue = "https://google.com",
                valueClass = String::class,
            ),
            Feature(
                key = "optional_server",
                defaultValue = "https://test.com",
                valueClass = String::class,
            ),
            Feature(
                key = "max_group_chat_size",
                defaultValue = 25,
                valueClass = Long::class,
            ),
            Feature(
                key = "anim_threshold",
                defaultValue = 0.5,
                valueClass = Double::class,
            ),
            Feature(
                key = "multifactor_login",
                defaultValue = false,
                valueClass = Boolean::class,
            ),
            Feature(
                key = "design_v2",
                defaultValue = false,
                valueClass = Boolean::class,
            ),
            Feature(
                key = "flexible_navigation",
                defaultValue = false,
                valueClass = Boolean::class,
            ),
            Feature(
                key = "profile_icon_alpha",
                defaultValue = 1.0,
                valueClass = Double::class,
            )
        )
    }
}