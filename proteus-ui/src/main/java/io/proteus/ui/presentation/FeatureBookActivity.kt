package io.proteus.ui.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import io.proteus.ui.presentation.theme.ProteusTheme

class FeatureBookActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            ProteusTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = FeatureBookPage,
                ) {
                    featureBookRoute(
                        navController = navController,
                        onBack = { finish() }
                    )
                }
            }
        }
    }
}
