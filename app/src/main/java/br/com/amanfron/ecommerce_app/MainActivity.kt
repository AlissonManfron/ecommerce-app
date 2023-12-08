package br.com.amanfron.ecommerce_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.amanfron.ecommerce_app.features.createaccount.CreateAccountScreen
import br.com.amanfron.ecommerce_app.features.createaccount.CreateAccountViewModel
import br.com.amanfron.ecommerce_app.features.home.HomeScreen
import br.com.amanfron.ecommerce_app.features.home.HomeViewModel
import br.com.amanfron.ecommerce_app.features.login.LoginScreen
import br.com.amanfron.ecommerce_app.features.login.LoginViewModel
import br.com.amanfron.ecommerce_app.ui.theme.EcommerceappTheme
import br.com.amanfron.ecommerce_app.utils.NavRoutes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcommerceappTheme {
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
                }
            }
        }
    }
}