package io.proteus.core.platform

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path.Companion.toPath
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

actual object DataStoreFactory {

    @OptIn(ExperimentalForeignApi::class)
    actual fun createDataStore(name: String, context: PlatformContext): DataStore<Preferences> {
        val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )

        requireNotNull(documentDirectory) { "Could not get document directory" }

        val dataStoreFile = documentDirectory.path + "/$name.preferences_pb"

        return PreferenceDataStoreFactory.createWithPath(
            produceFile = { dataStoreFile.toPath() }
        )
    }
}