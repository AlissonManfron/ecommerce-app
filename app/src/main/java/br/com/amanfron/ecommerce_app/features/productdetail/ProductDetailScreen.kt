package br.com.amanfron.ecommerce_app.features.productdetail

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.amanfron.ecommerce_app.R
import br.com.amanfron.ecommerce_app.core.architecture.UiState
import br.com.amanfron.ecommerce_app.core.domain.model.Product
import br.com.amanfron.ecommerce_app.features.productdetail.ProductDetailViewModel.ProductDetailEffect.NavigateBack
import br.com.amanfron.ecommerce_app.features.productdetail.ProductDetailViewModel.ProductDetailEffect.NavigateToCart
import br.com.amanfron.ecommerce_app.features.productdetail.ProductDetailViewModel.ProductDetailEffect.ShowAddToCartToast
import br.com.amanfron.ecommerce_app.features.productdetail.ProductDetailViewModel.ProductDetailEffect.ShowErrorToast
import br.com.amanfron.ecommerce_app.features.productdetail.ProductDetailViewModel.ProductDetailViewState
import br.com.amanfron.ecommerce_app.ui.customviews.LoadingContentView
import br.com.amanfron.ecommerce_app.ui.utils.ObserveAsEvents
import coil.compose.AsyncImage

@Composable
fun ProductDetailScreen(
    viewModel: ProductDetailViewModel = hiltViewModel(),
    productId: Int,
    onGoToCart: () -> Unit,
    onBackClick: () -> Boolean
) {
    val context = LocalContext.current
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    viewModel.effect.ObserveAsEvents { effect ->
        when (effect) {
            is NavigateToCart -> onGoToCart()
            is NavigateBack -> onBackClick()
            is ShowErrorToast -> {
                Toast.makeText(context, R.string.try_again_message, Toast.LENGTH_SHORT).show()
            }
            is ShowAddToCartToast -> {
                Toast.makeText(context, R.string.add_product_to_cart_message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    when (val state = uiState) {
        is UiState.Loading -> {
            LoadingContentView(shouldShowLoading = true) {}
        }
        is UiState.Success -> {
            ProductDetailScreen(
                state.data,
                onPurchaseProductClick = {
                    viewModel.onIntent(ProductDetailViewModel.ProductDetailIntent.OnBuyClick)
                },
                onAddProductToCartClick = {
                    viewModel.onIntent(ProductDetailViewModel.ProductDetailIntent.OnAddToCartClick)
                }
            )
        }
        is UiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Ocorreu um erro ao carregar os detalhes do produto.")
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onIntent(ProductDetailViewModel.ProductDetailIntent.LoadProduct(productId))
    }
}

@Composable
private fun ProductDetailScreen(
    state: ProductDetailViewState,
    onPurchaseProductClick: () -> Unit,
    onAddProductToCartClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 24.dp,
                end = 24.dp
            )
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = state.product.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = state.product.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(300.dp)
                    .padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth(),
            elevation = CardDefaults.elevatedCardElevation(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Descrição:",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = state.product.description,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth(),
            elevation = CardDefaults.elevatedCardElevation(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Preço:",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "R$ ${state.product.price}",
                    style = MaterialTheme.typography.headlineSmall,
                    lineHeight = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onPurchaseProductClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = "Comprar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onAddProductToCartClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = "Adicionar ao carrinho")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun ProductDetailScreenPreview() {
    ProductDetailScreen(
        state = ProductDetailViewState(
            product = Product(
                id = 0,
                title = "Title",
                description = "Description",
                imageUrl = "",
                price = "20.0",
                categoryId = 1,
                categoryName = "Livros",
                quantity = 1
            )
        ),
        onPurchaseProductClick = {},
        onAddProductToCartClick = {}
    )
}
