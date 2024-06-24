package br.com.amanfron.ecommerce_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.amanfron.ecommerce_app.features.createaccount.CreateAccountScreen
import br.com.amanfron.ecommerce_app.features.home.HomeScreen
import br.com.amanfron.ecommerce_app.features.login.LoginScreen
import br.com.amanfron.ecommerce_app.features.productdetail.ProductDetailScreen
import br.com.amanfron.ecommerce_app.ui.theme.EcommerceAppTheme
import br.com.amanfron.ecommerce_app.utils.NavRoutes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcommerceAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    val navController = rememberNavController()
                    val keyboardController = LocalSoftwareKeyboardController.current
                    NavHost(navController = navController, startDestination = NavRoutes.LOGIN) {
                        composable(NavRoutes.LOGIN) {
                            LoginScreen(
                                keyboardController,
                                navController,
                                hiltViewModel()
                            )
                        }

                        composable(NavRoutes.CREATE_ACCOUNT) {
                            CreateAccountScreen(
                                keyboardController,
                                hiltViewModel(),
                                navController
                            )
                        }

                        composable(NavRoutes.HOME) {
                            HomeScreen(
                                navController,
                                hiltViewModel()
                            )
                        }

                        composable(
                            NavRoutes.PRODUCT_DETAIL,
                            getProductIdArgument()
                        ) {
                            ProductDetailScreen(
                                navController,
                                hiltViewModel()
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getProductIdArgument() = listOf(
        navArgument("productId") {
            type = NavType.IntType
        }
    )
}