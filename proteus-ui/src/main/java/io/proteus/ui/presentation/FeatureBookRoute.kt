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

private fun NavController.backToCatalog() {
    popBackStack()
    currentBackStackEntry?.savedStateHandle?.set(FORCE_REFRESH_CATALOG, true)
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

        val forceRefresh = navBackStackEntry.savedStateHandle.get<Boolean>(FORCE_REFRESH_CATALOG)
            ?.also { navBackStackEntry.savedStateHandle.remove<Boolean>(FORCE_REFRESH_CATALOG) } == true

        FeatureCatalogScreen(
            viewModel = viewModel,
            onBack = onBack,
            forceRefresh = forceRefresh,
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
                navController.backToCatalog()
            }
        )
    }
}

private const val FORCE_REFRESH_CATALOG = "FORCE_REFRESH_CATALOG"
