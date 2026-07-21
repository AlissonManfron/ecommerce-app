package br.com.amanfron.ecommerce_app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import kotlinx.serialization.Serializable

@Serializable
data object LoginRoute

@Serializable
data object CreateAccountRoute

@Serializable
data object HomeRoute {
    val title = "Home"
    val icon = Icons.Filled.Home
}

@Serializable
data object ShoppingCartRoute {
    val title = "Carrinho"
    val icon = Icons.Filled.ShoppingCart
}

@Serializable
data object ProfileRoute {
    val title = "Conta"
    val icon = Icons.Filled.AccountCircle
}

@Serializable
data class ProductDetailsRoute(val productId: Int) {
    companion object {
        const val title: String = "Product Details"
    }
}

interface BottomNavItem {
    val route: Any
    val title: String
    val icon: ImageVector
}

@Serializable
object HomeNavItem : BottomNavItem {
    override val route = HomeRoute
    override val title = HomeRoute.title
    override val icon = HomeRoute.icon
}

@Serializable
object ShoppingCartNavItem : BottomNavItem {
    override val route = ShoppingCartRoute
    override val title = ShoppingCartRoute.title
    override val icon = ShoppingCartRoute.icon
}

@Serializable
object ProfileNavItem : BottomNavItem {
    override val route = ProfileRoute
    override val title = ProfileRoute.title
    override val icon = ProfileRoute.icon
}

val bottomNavItemsList = listOf(
    HomeNavItem,
    ShoppingCartNavItem,
    ProfileNavItem
)

fun NavDestination?.isRouteInBottomBar(): Boolean {
    return bottomNavItemsList.any { this?.hasRoute(it.route::class) == true }
}
