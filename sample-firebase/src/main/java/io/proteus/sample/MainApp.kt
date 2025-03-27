package io.proteus.sample

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import io.proteus.core.data.FeatureBookDataSource
import io.proteus.core.domain.Feature
import io.proteus.core.domain.FeatureContext
import io.proteus.core.provider.Proteus
import io.proteus.firebase.FirebaseOnlyProviderFactory

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase SDK before initializing Proteus
        FirebaseApp.initializeApp(this)

        val config = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(60)
            .build()
        config.setConfigSettingsAsync(configSettings)
        config.fetchAndActivate()

        Proteus.Builder(this)
            .registerConfigProviderFactory(FirebaseOnlyProviderFactory())
//            .registerFeatureBookDataSource(
//                AssetsFeatureBookDataSource(
//                    context = this,
//                    jsonFilePath = "featurebook.json"
//                )
//            )
            .registerFeatureBookDataSource(provideStaticFeatureDataSource())
            .build()
    }

    private fun provideStaticFeatureDataSource(): FeatureBookDataSource {
        return StaticFeatureBookDataSource(
            listOf(
                Feature(
                    key = "primary_server",
                    defaultValue = "https://google.com",
                    valueClass = String::class,
                    owner = "firebase"
                ),
                Feature(
                    key = "optional_server",
                    defaultValue = "https://test.com",
                    valueClass = String::class,
                    owner = "firebase"
                ),
                Feature(
                    key = "max_group_chat_size",
                    defaultValue = 25,
                    valueClass = Long::class,
                    owner = "firebase"
                ),
                Feature(
                    key = "anim_threshold",
                    defaultValue = 0.5,
                    valueClass = Double::class,
                    owner = "firebase"
                ),
                Feature(
                    key = "multifactor_login",
                    defaultValue = false,
                    valueClass = Boolean::class,
                    owner = "firebase"
                ),
                Feature(
                    key = "design_v2",
                    defaultValue = false,
                    valueClass = Boolean::class,
                    owner = "firebase"
                ),
                Feature(
                    key = "flexible_navigation",
                    defaultValue = false,
                    valueClass = Boolean::class,
                    owner = "firebase"
                ),
                Feature(
                    key = "profile_icon_alpha",
                    defaultValue = 1.0,
                    valueClass = Double::class,
                    owner = "firebase"
                ),
            )
        )
    }

    private class StaticFeatureBookDataSource(private val featureBook: List<FeatureContext<*>>) : FeatureBookDataSource {

        override suspend fun getFeatureBook(): Result<List<FeatureContext<*>>> {
            return Result.success(featureBook)
        }
    }
}
