package io.proteus.sample

import android.app.Application
import com.google.firebase.FirebaseApp
import io.proteus.core.data.AssetsFeatureBookDataSource
import io.proteus.core.provider.Proteus
import io.proteus.firebase.FirebaseOnlyProviderFactory

class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
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
