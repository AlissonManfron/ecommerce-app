package br.com.amanfron.ecommerce_app.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import br.com.amanfron.ecommerce_app.features.cart.ShoppingCartScreen
import br.com.amanfron.ecommerce_app.features.createaccount.CreateAccountScreen
import br.com.amanfron.ecommerce_app.features.home.HomeScreen
import br.com.amanfron.ecommerce_app.features.login.LoginScreen
import br.com.amanfron.ecommerce_app.features.login.LoginViewModel
import br.com.amanfron.ecommerce_app.features.productdetail.ProductDetailScreen
import br.com.amanfron.ecommerce_app.features.profile.ProfileScreen

@Composable
fun EcommerceAppNavHost(
    innerPadding: PaddingValues,
    navController: NavHostController,
    directions: EcommerceDirections
) = NavHost(navController = navController, startDestination = LoginRoute) {
    composable<LoginRoute> {
        val viewModel = hiltViewModel<LoginViewModel>()
        LoginScreen(
            uiState = viewModel.state.collectAsStateWithLifecycle().value,
            navigateToHome = directions.navigateToHome,
            navigateToCreateAccount = directions.navigateToCreateAccount,
            onEmailChanged = viewModel::setEmail,
            onPasswordChanged = viewModel::setPassword,
            onLoginButtonClick = viewModel::onButtonLoginClick
        )
    }

    composable<CreateAccountRoute> {
        CreateAccountScreen(
            navigateToHome = directions.navigateToHome,
        )
    }

    composable<HomeRoute> {
        val context = LocalContext.current
        HomeScreen(
            modifier = Modifier.padding(innerPadding),
            navigateToSeeMore = { category ->
                Toast.makeText(context, category, Toast.LENGTH_SHORT).show()
            },
            navigateToProductDetail = {
                directions.navigateToProductDetail(it)
            }
        )
    }

    composable<ShoppingCartRoute> {
        ShoppingCartScreen(
            modifier = Modifier.padding(innerPadding),
        )
    }

    composable<ProfileRoute> {
        ProfileScreen(
            modifier = Modifier.padding(innerPadding),
        )
    }

    composable<ProductDetailsRoute> { backStackEntry ->
        val productDetailsArgs: ProductDetailsRoute = backStackEntry.toRoute()
        ProductDetailScreen(
            productId = productDetailsArgs.productId,
            onBackClick = directions.navigateBack
        )
    }
}