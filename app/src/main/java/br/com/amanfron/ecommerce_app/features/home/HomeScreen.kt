package br.com.amanfron.ecommerce_app.features.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.amanfron.ecommerce_app.R
import br.com.amanfron.ecommerce_app.core.architecture.UiState
import br.com.amanfron.ecommerce_app.core.domain.model.Product
import br.com.amanfron.ecommerce_app.core.domain.model.ProductCategory
import br.com.amanfron.ecommerce_app.features.home.HomeViewModel.HomeEffect.NavigateToProductDetail
import br.com.amanfron.ecommerce_app.features.home.HomeViewModel.HomeEffect.NavigateToSeeMore
import br.com.amanfron.ecommerce_app.features.home.HomeViewModel.HomeIntent
import br.com.amanfron.ecommerce_app.features.home.HomeViewModel.HomeViewState
import br.com.amanfron.ecommerce_app.ui.customviews.LoadingContentView
import br.com.amanfron.ecommerce_app.ui.customviews.ProductSectionBannerView
import br.com.amanfron.ecommerce_app.ui.customviews.ProductSectionView
import br.com.amanfron.ecommerce_app.ui.utils.ObserveAsEvents

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToSeeMore: (categoryId: Int) -> Unit,
    navigateToProductDetail: (productId: Int) -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    viewModel.effect.ObserveAsEvents { effect ->
        when (effect) {
            is NavigateToProductDetail -> {
                navigateToProductDetail(effect.productId)
            }

            is NavigateToSeeMore -> {
                navigateToSeeMore(effect.categoryId)
            }
        }
    }

    when (val state = uiState) {
        is UiState.Loading -> {
            LoadingContentView(shouldShowLoading = true)
        }

        is UiState.Success -> {
            HomeContent(
                modifier = modifier,
                state = state.data,
                onSeeMoreClick = { categoryId ->
                    viewModel.onIntent(HomeIntent.OnSeeMoreClick(categoryId))
                },
                onProductClick = { productId ->
                    viewModel.onIntent(HomeIntent.OnProductClick(productId))
                }
            )
        }

        is UiState.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = context.getString(R.string.home_screen_not_found_products_message))
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    state: HomeViewState,
    onSeeMoreClick: (categoryId: Int) -> Unit,
    onProductClick: (productId: Int) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        item {
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
    HomeContent(
        state = HomeViewState(
            rankedProductList = listOf(
                ProductCategory(
                    categoryId = 1,
                    categoryName = "Eletrônicos",
                    products = listOf(
                        Product(
                            id = 0,
                            title = "Celular",
                            description = "Description",
                            imageUrl = "",
                            price = "2000.0",
                            categoryId = 1,
                            categoryName = "Eletrônicos"
                        )
                    )
                )
            )
        ),
        onSeeMoreClick = {},
        onProductClick = {}
    )
}
