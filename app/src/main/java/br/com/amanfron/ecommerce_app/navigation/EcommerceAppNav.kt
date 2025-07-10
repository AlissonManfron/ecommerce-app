package br.com.amanfron.ecommerce_app.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun EcommerceAppNav() {
    val navController = rememberNavController()
    val ecommerceDirections = remember { EcommerceDirections(navController) }
    val currentRoute = navController.currentBackStackEntryAsState()
        .value?.destination?.route
        ?.split(".")
        ?.last()

    Scaffold(
        bottomBar = {
            if (bottomNavItemsList.any { it.route.toString() == currentRoute }) {
                EcommerceAppNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        EcommerceAppNavHost(
            navController = navController,
            directions = ecommerceDirections,
            innerPadding = innerPadding
        )
    }
}