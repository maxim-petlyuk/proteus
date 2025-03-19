package io.config.sample

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.FirebaseApp
import io.config.sample.ui.theme.MockConfigLabTheme
import io.proteus.core.data.AssetsFeatureBookDataSource
import io.proteus.core.provider.Proteus
import io.proteus.firebase.FirebaseOnlyProviderFactory
import io.proteus.ui.presentation.FeatureBookActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(application)

        Proteus.Builder(application)
            .registerConfigProviderFactory(FirebaseOnlyProviderFactory())
            .registerFeatureBookDataSource(
                AssetsFeatureBookDataSource(
                    context = this,
                    jsonFilePath = "featurebook.json"
                )
            )
            .build()

        enableEdgeToEdge()
        setContent {
            MockConfigLabTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ScreenContent(
                        modifier = Modifier.padding(innerPadding),
                        onOpenFeatureCatalog = ::onOpenFeatureCatalog
                    )
                }
            }
        }
    }

    private fun onOpenFeatureCatalog() {
        startActivity(Intent(this, FeatureBookActivity::class.java))
    }
}

@Composable
private fun ScreenContent(
    modifier: Modifier = Modifier,
    onOpenFeatureCatalog: () -> Unit = { }
) {
    Box(modifier = modifier) {
        FilledTonalButton(
            onClick = onOpenFeatureCatalog,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Feature Catalog")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenContentPreview() {
    MockConfigLabTheme {
        ScreenContent()
    }
}