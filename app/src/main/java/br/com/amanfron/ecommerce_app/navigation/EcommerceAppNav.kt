package br.com.amanfron.ecommerce_app.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun EcommerceAppNav() {
    val navController = rememberNavController()
    val ecommerceDirections = remember { EcommerceDirections(navController) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            if (currentDestination.isRouteInBottomBar()) {
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