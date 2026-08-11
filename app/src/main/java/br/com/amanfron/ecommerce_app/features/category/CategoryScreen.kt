package br.com.amanfron.ecommerce_app.features.category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import br.com.amanfron.ecommerce_app.features.category.CategoryEffect.NavigateToProductDetail
import br.com.amanfron.ecommerce_app.ui.customviews.LoadingContentView
import br.com.amanfron.ecommerce_app.ui.customviews.ProductItem
import br.com.amanfron.ecommerce_app.ui.utils.ObserveAsEvents

@Composable
fun CategoryScreen(
    categoryId: Int,
    viewModel: CategoryViewModel = hiltViewModel(),
    navigateToProductDetail: (Int) -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    viewModel.effect.ObserveAsEvents { effect ->
        when (effect) {
            is NavigateToProductDetail -> navigateToProductDetail(effect.productId)
        }
    }

    LaunchedEffect(categoryId) {
        viewModel.onIntent(CategoryIntent.LoadProducts(categoryId))
    }

    when (val state = uiState) {
        is UiState.Loading -> {
            LoadingContentView(shouldShowLoading = true)
        }

        is UiState.Success -> {
            CategoryContent(
                state = state.data,
                onProductClick = { productId ->
                    viewModel.onIntent(CategoryIntent.OnProductClick(productId))
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
                Text(text = context.getString(R.string.try_again_message))
            }
        }
    }
}

@Composable
private fun CategoryContent(
    state: CategoryViewState,
    onProductClick: (Int) -> Unit
) {
    val context = LocalContext.current

    Scaffold { innerPadding ->
        if (state.products.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = context.getString(R.string.category_screen_not_found_product_message))
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(state.products) { product ->
                    ProductItem(
                        product = product,
                        onProductClick = onProductClick
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CategoryScreenPreview() {
    CategoryContent(
        state = CategoryViewState(
            products = listOf(
                Product(
                    id = 0,
                    title = "Title",
                    description = "Description",
                    imageUrl = "",
                    price = "20.0",
                    categoryId = 1,
                    categoryName = "Livros",
                    quantity = 1
                )
            )
        ),
        onProductClick = {}
    )
}