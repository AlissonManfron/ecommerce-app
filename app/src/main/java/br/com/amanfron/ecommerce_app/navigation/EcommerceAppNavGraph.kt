package br.com.amanfron.ecommerce_app.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
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
import br.com.amanfron.ecommerce_app.ui.customviews.BottomNavigationBar
import br.com.amanfron.ecommerce_app.ui.customviews.NavigationState

@Composable
fun EcommerceAppNavGraph(
    navController: NavHostController,
    navigationState: NavigationState,
    keyboardController: SoftwareKeyboardController?
) = NavHost(navController = navController, startDestination = NavRoutes.LOGIN) {
    composable(NavRoutes.LOGIN) {
        LoginScreen(keyboardController, navController)
    }

    composable(NavRoutes.CREATE_ACCOUNT) {
        CreateAccountScreen(keyboardController, navController)
    }

    makeComposable(NavRoutes.HOME, navController, navigationState) {
        HomeScreen(navController)
    }

    makeComposable(NavRoutes.SHOPPING_CART, navController, navigationState) {
        ShoppingCartScreen(navController)
    }

    composable(
        route = NavRoutes.PRODUCT_DETAIL,
        arguments = listOf(
            navArgument(NavRoutes.PRODUCT_ID_PARAM) {
                type = NavType.IntType
            }
        )
    ) {
        ProductDetailScreen(navController)
    }
}

fun NavGraphBuilder.makeComposable(
    route: String,
    navController: NavController,
    navigationState: NavigationState,
    content: @Composable () -> Unit
) {
    composable(route = route) {
        Scaffold(
            bottomBar = {
                BottomNavigationBar(
                    navController = navController,
                    navigationState = navigationState
                )
            }
        ) { _ ->
            content()
        }
    }
}