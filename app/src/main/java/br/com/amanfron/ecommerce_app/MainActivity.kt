package br.com.amanfron.ecommerce_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.amanfron.ecommerce_app.core.model.response.product.Product
import br.com.amanfron.ecommerce_app.features.createaccount.CreateAccountScreen
import br.com.amanfron.ecommerce_app.features.createaccount.CreateAccountViewModel
import br.com.amanfron.ecommerce_app.features.home.HomeScreen
import br.com.amanfron.ecommerce_app.features.home.HomeViewModel
import br.com.amanfron.ecommerce_app.features.login.LoginScreen
import br.com.amanfron.ecommerce_app.features.login.LoginViewModel
import br.com.amanfron.ecommerce_app.features.productdetail.ProductDetailScreen
import br.com.amanfron.ecommerce_app.ui.theme.EcommerceAppTheme
import br.com.amanfron.ecommerce_app.utils.NavRoutes
import com.google.gson.Gson
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
                            val viewModel: LoginViewModel by viewModels()
                            LoginScreen(
                                keyboardController,
                                navController,
                                viewModel
                            )
                        }

                        composable(NavRoutes.CREATE_ACCOUNT) {
                            val viewModel: CreateAccountViewModel by viewModels()
                            CreateAccountScreen(
                                keyboardController,
                                viewModel,
                                navController
                            )
                        }

                        composable(NavRoutes.HOME) {
                            val viewModel: HomeViewModel by viewModels()
                            HomeScreen(
                                navController,
                                viewModel
                            )
                        }

                        composable(
                            NavRoutes.PRODUCT_DETAIL,
                            arguments = listOf(navArgument("product") { type = ProductType() })
                        ) { navBackStackEntry ->
                            val product =
                                navBackStackEntry.arguments?.getParcelable<Product>("product")
                            product?.let {
                                ProductDetailScreen(navController, it)
                            }
                        }
                    }
                }
            }
        }
    }

    class ProductType : NavType<Product>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): Product? {
            return bundle.getParcelable(key)
        }

        override fun parseValue(value: String): Product {
            return Gson().fromJson(value, Product::class.java)
        }

        override fun put(bundle: Bundle, key: String, value: Product) {
            bundle.putParcelable(key, value)
        }
    }
}