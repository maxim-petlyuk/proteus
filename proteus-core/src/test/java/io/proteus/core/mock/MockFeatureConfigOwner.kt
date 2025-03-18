package io.proteus.core.mock

import io.proteus.core.provider.FeatureConfigOwner

internal sealed class MockFeatureConfigOwner : FeatureConfigOwner {

    data object Firebase : MockFeatureConfigOwner()

    data object CleverTap : MockFeatureConfigOwner()
}
