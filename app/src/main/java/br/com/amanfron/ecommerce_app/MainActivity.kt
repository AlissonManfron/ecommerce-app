package br.com.amanfron.ecommerce_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.compose.rememberNavController
import br.com.amanfron.ecommerce_app.navigation.EcommerceAppNavGraph
import br.com.amanfron.ecommerce_app.ui.customviews.NavigationState
import br.com.amanfron.ecommerce_app.ui.theme.EcommerceAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcommerceAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    EcommerceAppNavGraph(
                        navController = rememberNavController(),
                        navigationState = rememberNavigationState(),
                        keyboardController = LocalSoftwareKeyboardController.current
                    )
                }
            }
        }
    }
}

@Composable
fun rememberNavigationState(): NavigationState {
    return remember { NavigationState() }
}

