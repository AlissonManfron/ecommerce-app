package br.com.amanfron.ecommerce_app.features.home

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.amanfron.ecommerce_app.R
import br.com.amanfron.ecommerce_app.core.model.response.product.Product
import br.com.amanfron.ecommerce_app.core.model.response.product.ProductCategoryResponse
import br.com.amanfron.ecommerce_app.features.home.HomeViewModel.HomeViewState
import br.com.amanfron.ecommerce_app.ui.customviews.LoadingContentView
import br.com.amanfron.ecommerce_app.ui.customviews.ProductSectionBannerView
import br.com.amanfron.ecommerce_app.ui.customviews.ProductSectionView

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToSeeMore: (categoryName: String) -> Unit,
    navigateToProductDetail: (productId: Int) -> Unit
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreen(
        state,
        onSeeMoreClick = navigateToSeeMore,
        onProductClick = navigateToProductDetail
    )

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
    state: HomeViewState,
    onSeeMoreClick: (categoryName: String) -> Unit,
    onProductClick: (productId: Int) -> Unit
) {
    LoadingContentView(shouldShowLoading = state.shouldShowLoading) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            ProductSectionBannerView(
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
        state = HomeViewState(
            shouldShowLoading = false,
            shouldShowDefaultError = false,
            rankedProductList = listOf(
                ProductCategoryResponse(
                    categoryName = "",
                    products = listOf(
                        Product(
                            id = 0,
                            title = "Title",
                            description = "Description",
                            imageUrl = "",
                            price = "20.0",
                            categoryId = 1,
                            categoryName = ""
                        )
                    )
                )
            )
        ),
        onSeeMoreClick = {},
        onProductClick = {}
    )
}