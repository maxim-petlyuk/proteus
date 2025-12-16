package io.proteus.sample

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
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
                    onOpenConfigurator = { }
                )
            }

//            SampleConfigTheme {
//                Scaffold(
//                    modifier = Modifier.fillMaxSize(),
//                ) { innerPadding ->
//                    val featureName = "optional_server"
//
//                    var featureConfigValue by remember {
//                        mutableStateOf(featureConfigProvider.getString("optional_server"))
//                    }
//
//                    LifecycleResumeEffect(Unit) {
//                        featureConfigValue = featureConfigProvider.getString(featureName)
//
//                        onPauseOrDispose { }
//                    }
//
//                    ScreenContent(
//                        modifier = Modifier.padding(innerPadding),
//                        featureName = featureName,
//                        featureConfigValue = featureConfigValue,
//                        onOpenFeatureCatalog = ::onOpenFeatureCatalog
//                    )
//                }
//            }
        }
    }

    private fun onOpenFeatureCatalog() {
        startActivity(Intent(this, FeatureBookActivity::class.java))
    }
}

@Composable
private fun ScreenContent(
    modifier: Modifier = Modifier,
    featureName: String,
    featureConfigValue: String,
    onOpenFeatureCatalog: () -> Unit = { }
) {
    Column(modifier = modifier.fillMaxSize()) {
        FeatureCard(
            modifier = Modifier.fillMaxWidth(),
            featureName = featureName,
            featureConfigValue = featureConfigValue
        )

        FilledTonalButton(
            onClick = onOpenFeatureCatalog,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Open Feature Catalog")
        }
    }
}

@Composable
private fun FeatureCard(
    modifier: Modifier = Modifier,
    featureName: String,
    featureConfigValue: String,
) {
    Column(modifier = modifier) {
        Text(
            text = "Imagine that this is some widget which used feature config.",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        val title = buildAnnotatedString {
            append("Feature name: ")
            withStyle(style = SpanStyle(Color.Blue)) {
                append(featureName)
            }
        }

        val configValue = buildAnnotatedString {
            append("Feature config value: ")
            withStyle(style = SpanStyle(Color.Magenta)) {
                append(featureConfigValue)
            }
        }

        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Text(
            text = configValue,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ScreenContentPreview() {
    SampleConfigTheme {
        ScreenContent(
            featureName = "Feature A",
            featureConfigValue = "505"
        )
    }
}