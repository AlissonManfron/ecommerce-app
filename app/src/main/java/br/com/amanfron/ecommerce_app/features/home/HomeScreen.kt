package br.com.amanfron.ecommerce_app.features.home

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.amanfron.ecommerce_app.R
import br.com.amanfron.ecommerce_app.core.architecture.UiState
import br.com.amanfron.ecommerce_app.core.model.response.product.ProductCategoryResponse
import br.com.amanfron.ecommerce_app.core.model.response.product.ProductResponse
import br.com.amanfron.ecommerce_app.features.home.HomeViewModel.HomeEffect.NavigateToProductDetail
import br.com.amanfron.ecommerce_app.features.home.HomeViewModel.HomeEffect.NavigateToSeeMore
import br.com.amanfron.ecommerce_app.features.home.HomeViewModel.HomeEffect.ShowErrorToast
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
    navigateToSeeMore: (categoryName: String) -> Unit,
    navigateToProductDetail: (productId: Int) -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    viewModel.effect.ObserveAsEvents { effect ->
        when (effect) {
            is ShowErrorToast -> {
                Toast.makeText(context, R.string.try_again_message, Toast.LENGTH_SHORT)
                    .show()
            }

            is NavigateToProductDetail -> {
                navigateToProductDetail(effect.productId)
            }

            is NavigateToSeeMore -> {
                navigateToSeeMore(effect.categoryName)
            }
        }
    }

    when (val state = uiState) {
        is UiState.Loading -> {
            LoadingContentView(shouldShowLoading = true) {}
        }
        is UiState.Success -> {
            HomeScreen(
                modifier = modifier,
                state = state.data,
                onSeeMoreClick = { categoryName ->
                    viewModel.onIntent(HomeIntent.OnSeeMoreClick(categoryName))
                },
                onProductClick = { productId ->
                    viewModel.onIntent(HomeIntent.OnProductClick(productId))
                }
            )
        }
        is UiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Ocorreu um erro ao carregar os produtos.")
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeViewState,
    onSeeMoreClick: (categoryName: String) -> Unit,
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
    HomeScreen(
        state = HomeViewState(
            rankedProductList = listOf(
                ProductCategoryResponse(
                    categoryName = "",
                    products = listOf(
                        ProductResponse(
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
