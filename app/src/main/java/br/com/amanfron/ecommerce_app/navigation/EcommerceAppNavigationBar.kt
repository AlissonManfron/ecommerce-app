package br.com.amanfron.ecommerce_app.navigation

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import br.com.amanfron.ecommerce_app.features.cart.ShoppingCartViewModel

@Composable
fun EcommerceAppNavigationBar(
    navController: NavHostController,
    shoppingCartViewModel: ShoppingCartViewModel = hiltViewModel()
) {
    val state by shoppingCartViewModel.state.collectAsStateWithLifecycle()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    LaunchedEffect(true) {
        shoppingCartViewModel.getProductsCount()
    }

    NavigationBar {
        bottomNavItemsList.forEach { navItem ->
            NavigationBarItem(
                icon = {
                    if (navItem == ShoppingCartNavItem) {
                        BadgedBox(
                            badge = {
                                if (state.cartItemCount > 0) {
                                    Badge {
                                        Text(
                                            text = state.cartItemCount.toString(),
                                            modifier = Modifier.semantics {
                                                contentDescription =
                                                    "${state.cartItemCount} novos itens no ${navItem.title}"
                                            }
                                        )
                                    }
                                }
                            }
                        ) {
                            Icon(navItem.icon, contentDescription = navItem.title)
                        }
                    } else {
                        Icon(navItem.icon, contentDescription = navItem.title)
                    }
                },
                label = { Text(navItem.title) },
                selected = currentDestination?.hasRoute(navItem.route::class) == true,
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
