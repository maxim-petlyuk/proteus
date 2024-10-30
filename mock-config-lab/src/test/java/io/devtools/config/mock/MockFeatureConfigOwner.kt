package io.devtools.config.mock

import io.devtools.config.domain.FeatureConfigOwner

internal sealed class MockFeatureConfigOwner : FeatureConfigOwner {

    data object Firebase : MockFeatureConfigOwner()

    data object CleverTap : MockFeatureConfigOwner()
}
