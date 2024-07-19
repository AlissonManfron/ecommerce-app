package br.com.amanfron.ecommerce_app.features.home

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import br.com.amanfron.ecommerce_app.R
import br.com.amanfron.ecommerce_app.core.model.response.product.Product
import br.com.amanfron.ecommerce_app.core.model.response.product.ProductCategoryResponse
import br.com.amanfron.ecommerce_app.features.home.HomeViewModel.HomeViewState
import br.com.amanfron.ecommerce_app.ui.customviews.BannerProductSectionView
import br.com.amanfron.ecommerce_app.ui.customviews.BottomNavigationBar
import br.com.amanfron.ecommerce_app.ui.customviews.LoadingContentView
import br.com.amanfron.ecommerce_app.ui.customviews.ProductSectionView

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel
) {
    val context = LocalContext.current
    val state = viewModel.state.value

    BottomNavigationBar(navController = navController) {
        HomeScreen(
            it,
            state,
            onSeeMoreClick = { category ->
                Toast.makeText(context, category, Toast.LENGTH_SHORT).show()
            },
            onProductClick = { product ->
                navController.navigate("product_detail/${product.id}")
            }
        )
    }

    LaunchedEffect(state) {
        when {
            state.shouldShowDefaultError -> {
                state.shouldShowDefaultError = false
                Toast.makeText(context, R.string.try_again_message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeScreen(
    paddingValues: PaddingValues,
    state: HomeViewState,
    onSeeMoreClick: (categoryName: String) -> Unit,
    onProductClick: (product: Product) -> Unit
) {
    LoadingContentView(shouldShowLoading = state.shouldShowLoading) {
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            BannerProductSectionView(
                productList = state.bannerProductList,
                onProductClick = onProductClick
            )
            ProductSectionView(
                rankedProductList = state.rankedProductList,
                onSeeMoreClick = onSeeMoreClick,
                onProductClick = onProductClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        paddingValues = PaddingValues(),
        state = HomeViewState(
            shouldShowLoading = false,
            shouldShowDefaultError = false,
            rankedProductList = listOf(
                ProductCategoryResponse(
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