package io.proteus.ui.presentation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import io.proteus.ui.presentation.catalog.FeatureCatalogScreen
import io.proteus.ui.presentation.catalog.FeatureCatalogViewModel
import kotlinx.serialization.Serializable

@Serializable
internal data object FeatureBookScreen

//@Serializable
//private data class FeatureConfiguratorScreen(val walletOperation: WalletOperationReport)

internal fun NavGraphBuilder.featureBookRoute(
    navController: NavHostController,
    onBack: () -> Unit = {}
) {
    composable<FeatureBookScreen> { navBackStackEntry ->
        val viewModel: FeatureCatalogViewModel = viewModel(
            viewModelStoreOwner = navBackStackEntry,
            factory = FeatureCatalogViewModel.Factory
        )

        FeatureCatalogScreen(
            viewModel = viewModel,
            onBack = onBack
        )
    }
}