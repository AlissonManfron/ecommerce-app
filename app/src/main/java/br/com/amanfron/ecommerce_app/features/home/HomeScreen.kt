package br.com.amanfron.ecommerce_app.features.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import br.com.amanfron.ecommerce_app.R
import br.com.amanfron.ecommerce_app.core.model.response.product.Product
import br.com.amanfron.ecommerce_app.core.model.response.product.RankedProductResponse
import br.com.amanfron.ecommerce_app.features.home.HomeViewModel.HomeViewState
import br.com.amanfron.ecommerce_app.ui.customviews.LoadingView
import br.com.amanfron.ecommerce_app.ui.customviews.ProductSectionView
import br.com.amanfron.ecommerce_app.ui.theme.LightGray

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel
) {
    val context = LocalContext.current
    val state = viewModel.state.value

    HomeScreen(state,
        onSeeMoreClick = { category ->
            Toast.makeText(context, category, Toast.LENGTH_SHORT).show()
        },
        onProductClick = { productId ->
            Toast.makeText(context, productId.toString(), Toast.LENGTH_SHORT).show()
        }
    )

    DisposableEffect(state) {
        when {
            state.shouldShowDefaultError -> {
                state.shouldShowDefaultError = false
                Toast.makeText(context, R.string.try_again_message, Toast.LENGTH_SHORT).show()
            }
        }
        onDispose {
            state.shouldShowDefaultError = false
        }
    }
}

@Composable
fun HomeScreen(
    state: HomeViewState,
    onSeeMoreClick: (categoryName: String) -> Unit,
    onProductClick: (productId: Int) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGray)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        state.rankedProductList.forEach {
            ProductSectionView(
                categoryName = it.categoryName,
                productList = it.products,
                onSeeMoreClick = onSeeMoreClick,
                onProductClick = onProductClick
            )
        }
    }

    if (state.shouldShowLoading) {
        LoadingView()
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        state = HomeViewState(
            shouldShowLoading = false,
            shouldShowDefaultError = false,
            rankedProductList = listOf(
                RankedProductResponse(
                    "", listOf(
                        Product(
                            0,
                            "Title",
                            "Description",
                            "",
                            "20.0",
                            1,
                            ""
                        )
                    )
                )
            )
        ),
        onSeeMoreClick = {},
        onProductClick = {}
    )
}