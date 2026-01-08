package io.proteus.core.platform

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import java.io.File

actual object DataStoreFactory {

    actual fun createDataStore(name: String, context: PlatformContext): DataStore<Preferences> {
        val file = context.filesDir.resolve("$name.preferences_pb")

        return PreferenceDataStoreFactory.create(
            produceFile = { File(file.absolutePath) }
        )
    }
}