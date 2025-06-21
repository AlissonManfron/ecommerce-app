package br.com.amanfron.ecommerce_app.navigation

import androidx.navigation.NavController
import androidx.navigation.navOptions

class EcommerceDirections(
    navController: NavController
) {
    val navigateToHome = {
        navController.navigate(
            HomeRoute,
            navOptions = navOptions {
                popUpTo(LoginRoute) {
                    inclusive = true
                }
            }
        )
    }

    val navigateToProductDetail: (productId: Int) -> Unit = { productId ->
        navController.navigate(ProductDetailsRoute(productId))
    }

    val navigateToCreateAccount = {
        navController.navigate(CreateAccountRoute)
    }
}