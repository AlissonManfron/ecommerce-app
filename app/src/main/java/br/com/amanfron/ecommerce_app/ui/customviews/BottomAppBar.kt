package br.com.amanfron.ecommerce_app.ui.customviews

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import br.com.amanfron.ecommerce_app.navigation.NavRoutes

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BottomNavigationBar(
    navController: NavController,
    navigationState: NavigationState
) {
    NavigationBar {
        BottomNavigationItem()
            .bottomNavigationItems()
            .forEachIndexed { index, navigationItem ->
                NavigationBarItem(
                    selected = index == navigationState.selectedItemIndex.value,
                    label = {
                        Text(navigationItem.label)
                    },
                    icon = {
                        Icon(
                            navigationItem.icon,
                            contentDescription = navigationItem.label
                        )
                    },
                    onClick = {
                        navigationState.selectedItemIndex.value = index
                        navController.navigate(navigationItem.route) {
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

data class BottomNavigationItem(
    val label: String = "",
    val icon: ImageVector = Icons.Filled.Home,
    val route: String = ""
) {
    fun bottomNavigationItems(): List<BottomNavigationItem> {
        return listOf(
            BottomNavigationItem(
                label = "Home",
                icon = Icons.Filled.Home,
                route = NavRoutes.HOME
            ),
            BottomNavigationItem(
                label = "Carrinho",
                icon = Icons.Filled.ShoppingCart,
                route = NavRoutes.SHOPPING_CART
            ),
            BottomNavigationItem(
                label = "Conta",
                icon = Icons.Filled.AccountCircle,
                route = NavRoutes.HOME
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    BottomNavigationBar(NavController(LocalContext.current), NavigationState())
}