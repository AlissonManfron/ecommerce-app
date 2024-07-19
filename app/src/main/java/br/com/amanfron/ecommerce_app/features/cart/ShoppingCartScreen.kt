package br.com.amanfron.ecommerce_app.features.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import br.com.amanfron.ecommerce_app.ui.customviews.BottomNavigationBar
import br.com.amanfron.ecommerce_app.ui.customviews.LoadingContentView

@Composable
fun ShoppingCartScreen(
    navController: NavHostController,
    viewModel: ShoppingCartViewModel
) {

    val state = viewModel.state.collectAsState()

    BottomNavigationBar(navController = navController) { paddingValues ->
        LoadingContentView(shouldShowLoading = state.value.shouldShowLoading) {
            Column(
                modifier = androidx.compose.ui.Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                Text(text = "Lorem Ipsum")
            }
        }
    }
}