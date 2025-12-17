package io.proteus.sample

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import io.proteus.core.provider.Proteus
import io.proteus.sample.ui.screens.demo.DemoScreen
import io.proteus.sample.ui.theme.SampleConfigTheme
import io.proteus.ui.presentation.FeatureBookActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // simple implementation of DI just for demo, better to create a single instance via
        // your DI framework - dagger/hilt, koin, etc..
        val featureConfigProvider = (application as MainApp).getFeatureConfigProvider()

        enableEdgeToEdge()
        setContent {
            SampleConfigTheme {
                DemoScreen(
                    provider = featureConfigProvider,
                    remoteConfigProviderFactory = Proteus.getInstance().getRemoteConfigProviderFactory(),
                    onOpenConfigurator = ::onOpenFeatureCatalog
                )
            }
        }
    }

    private fun onOpenFeatureCatalog() {
        startActivity(Intent(this, FeatureBookActivity::class.java))
    }
}