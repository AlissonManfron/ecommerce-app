package br.com.amanfron.ecommerce_app.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

@Composable
fun EcommerceAppNavigationBar(navController: NavHostController) {
    NavigationBar {
        bottomNavItemsList.forEach { navItem ->
            NavigationBarItem(
                icon = { Icon(navItem.icon, contentDescription = navItem.title) },
                label = { Text(navItem.title) },
                selected = navController.currentDestination.isRoute(navItem.route),
                onClick = {
                    navController.navigate(navItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

fun <T : Any> androidx.navigation.NavDestination?.isRoute(route: T): Boolean {
    return this?.route == route::class.qualifiedName ||  // Para rotas de objeto simples
            this?.route?.startsWith(route::class.qualifiedName.orEmpty()) == true // Para rotas com args
}