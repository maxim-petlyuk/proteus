package io.proteus.core.mock

import io.proteus.core.mock.MockFeatureConfigOwner.entries

internal enum class MockFeatureConfigOwner(val serviceOwner: String) {
    Firebase("firebase"),
    CleverTap("clevertap");

    companion object {

        fun fromServiceOwner(serviceOwner: String): MockFeatureConfigOwner? {
            return entries.firstOrNull { it.serviceOwner.equals(serviceOwner, ignoreCase = true) }
        }
    }
}
