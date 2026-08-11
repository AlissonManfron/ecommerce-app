package br.com.amanfron.ecommerce_app.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import br.com.amanfron.ecommerce_app.features.cart.ShoppingCartScreen
import br.com.amanfron.ecommerce_app.features.category.CategoryScreen
import br.com.amanfron.ecommerce_app.features.createaccount.CreateAccountScreen
import br.com.amanfron.ecommerce_app.features.home.HomeScreen
import br.com.amanfron.ecommerce_app.features.login.LoginScreen
import br.com.amanfron.ecommerce_app.features.productdetail.ProductDetailScreen
import br.com.amanfron.ecommerce_app.features.profile.ProfileScreen

@Composable
fun EcommerceAppNavHost(
    innerPadding: PaddingValues,
    navController: NavHostController,
    directions: EcommerceDirections
) = NavHost(navController = navController, startDestination = LoginRoute) {
    composable<LoginRoute> {
        LoginScreen(
            navigateToHome = directions.navigateToHome,
            navigateToCreateAccount = directions.navigateToCreateAccount
        )
    }

    composable<CreateAccountRoute> {
        CreateAccountScreen(
            navigateToHome = directions.navigateToHome,
        )
    }

    composable<HomeRoute> {
        HomeScreen(
            modifier = Modifier.padding(innerPadding),
            navigateToSeeMore = directions.navigateToCategory,
            navigateToProductDetail = directions.navigateToProductDetail
        )
    }

    composable<CategoryRoute> { backStackEntry ->
        val route: CategoryRoute = backStackEntry.toRoute()
        CategoryScreen(
            categoryId = route.categoryId,
            navigateToProductDetail = directions.navigateToProductDetail
        )
    }

    composable<ShoppingCartRoute> {
        ShoppingCartScreen(
            modifier = Modifier.padding(innerPadding)
        )
    }

    composable<ProfileRoute> {
        ProfileScreen(
            modifier = Modifier.padding(innerPadding)
        )
    }

    composable<ProductDetailsRoute> { backStackEntry ->
        val route: ProductDetailsRoute = backStackEntry.toRoute()
        ProductDetailScreen(
            productId = route.productId,
            onBackClick = directions.navigateBack,
            onGoToCart = directions.navigateToCart
        )
    }
}
