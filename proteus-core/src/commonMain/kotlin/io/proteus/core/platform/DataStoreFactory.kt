package io.proteus.core.platform

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

expect object DataStoreFactory {

    fun createDataStore(name: String, context: PlatformContext): DataStore<Preferences>
}