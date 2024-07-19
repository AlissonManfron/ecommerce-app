package br.com.amanfron.ecommerce_app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.amanfron.ecommerce_app.features.cart.ShoppingCartScreen
import br.com.amanfron.ecommerce_app.features.createaccount.CreateAccountScreen
import br.com.amanfron.ecommerce_app.features.home.HomeScreen
import br.com.amanfron.ecommerce_app.features.login.LoginScreen
import br.com.amanfron.ecommerce_app.features.productdetail.ProductDetailScreen

@Composable
fun EcommerceAppNavGraph(
    navController: NavHostController,
    keyboardController: SoftwareKeyboardController?
) = NavHost(navController = navController, startDestination = NavRoutes.LOGIN) {
    composable(NavRoutes.LOGIN) {
        LoginScreen(
            keyboardController, navController, hiltViewModel()
        )
    }

    composable(NavRoutes.CREATE_ACCOUNT) {
        CreateAccountScreen(
            keyboardController, navController, hiltViewModel()
        )
    }

    composable(NavRoutes.HOME) {
        HomeScreen(
            navController, hiltViewModel()
        )
    }

    composable(NavRoutes.SHOPPING_CART) {
        ShoppingCartScreen(
            navController, hiltViewModel()
        )
    }

    composable(
        route = NavRoutes.PRODUCT_DETAIL,
        arguments = listOf(
            navArgument("productId") {
                type = NavType.IntType
            }
        )
    ) {
        ProductDetailScreen(
            navController, hiltViewModel()
        )
    }
}