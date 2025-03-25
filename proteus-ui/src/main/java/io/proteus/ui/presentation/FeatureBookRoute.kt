package io.proteus.ui.presentation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import io.proteus.ui.presentation.catalog.FeatureCatalogScreen
import io.proteus.ui.presentation.catalog.FeatureCatalogViewModel
import io.proteus.ui.presentation.configurator.FeatureConfiguratorScreen
import io.proteus.ui.presentation.configurator.FeatureConfiguratorViewModel
import kotlinx.serialization.Serializable

@Serializable
internal data object FeatureBookPage

@Serializable
private data class FeatureConfiguratorPage(val featureKey: String)

private fun NavController.navigateToFeatureConfigurator(featureKey: String) {
    navigate(FeatureConfiguratorPage(featureKey))
}

internal fun NavGraphBuilder.featureBookRoute(
    navController: NavHostController,
    onBack: () -> Unit = {}
) {
    composable<FeatureBookPage> { navBackStackEntry ->
        val viewModel: FeatureCatalogViewModel = viewModel(
            viewModelStoreOwner = navBackStackEntry,
            factory = FeatureCatalogViewModel.Factory
        )

        FeatureCatalogScreen(
            viewModel = viewModel,
            onBack = onBack,
            openFeatureConfigurator = { featureKey ->
                navController.navigateToFeatureConfigurator(featureKey)
            }
        )
    }

    composable<FeatureConfiguratorPage> { navBackStackEntry ->
        val featureDescriptor = navBackStackEntry.toRoute<FeatureConfiguratorPage>()

        val viewModel: FeatureConfiguratorViewModel = viewModel(
            viewModelStoreOwner = navBackStackEntry,
            factory = FeatureConfiguratorViewModel.Factory(featureDescriptor.featureKey)
        )

        FeatureConfiguratorScreen(
            viewModel = viewModel,
            onBack = {
                navController.popBackStack()
            }
        )
    }
}