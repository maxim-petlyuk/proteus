package io.proteus.sample

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import io.proteus.core.data.AssetsFeatureBookDataSource
import io.proteus.core.provider.Proteus
import io.proteus.firebase.FirebaseOnlyProviderFactory

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)

        val config = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(60)
            .build()
        config.setConfigSettingsAsync(configSettings)
        config.fetchAndActivate()

        Proteus.Builder(this)
            .registerConfigProviderFactory(FirebaseOnlyProviderFactory())
            .registerFeatureBookDataSource(
                AssetsFeatureBookDataSource(
                    context = this,
                    jsonFilePath = "featurebook.json"
                )
            )
            .build()
    }
}
