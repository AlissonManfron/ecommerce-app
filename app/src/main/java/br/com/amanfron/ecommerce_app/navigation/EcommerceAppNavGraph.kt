package br.com.amanfron.ecommerce_app.navigation

import android.widget.Toast
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
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
    navigationState: NavigationState
) = NavHost(navController = navController, startDestination = NavRoutes.LOGIN) {

    composable(NavRoutes.LOGIN) {
        LoginScreen(
            navigateToHome = {
                navController.navigate(
                    NavRoutes.HOME,
                    navOptions = NavOptions.Builder()
                        .setPopUpTo(NavRoutes.LOGIN, true)
                        .build()
                )
            },
            navigateToCreateAccount = {
                navController.navigate(NavRoutes.CREATE_ACCOUNT)
            }
        )
    }

    composable(NavRoutes.CREATE_ACCOUNT) {
        CreateAccountScreen(
            navigateToHome = {
                navController.navigate(
                    NavRoutes.HOME,
                    navOptions = NavOptions.Builder()
                        .setPopUpTo(NavRoutes.LOGIN, true)
                        .build()
                )
            }
        )
    }

    makeComposable(NavRoutes.HOME, navController, navigationState) {
        val context = LocalContext.current
        HomeScreen(
            navigateToSeeMore = { category ->
                Toast.makeText(context, category, Toast.LENGTH_SHORT).show()
            },
            navigateToProductDetail = { productId ->
                navController.navigate("${NavRoutes.PRODUCT_DETAIL}/$productId")
            }
        )
    }

    makeComposable(NavRoutes.SHOPPING_CART, navController, navigationState) {
        ShoppingCartScreen()
    }

    composable(
        route = NavRoutes.PRODUCT_DETAIL_WITH_ID,
        arguments = listOf(
            navArgument(NavRoutes.PRODUCT_ID_PARAM) {
                type = NavType.IntType
            }
        )
    ) {
        ProductDetailScreen()
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