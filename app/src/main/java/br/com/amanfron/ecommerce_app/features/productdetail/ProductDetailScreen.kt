package br.com.amanfron.ecommerce_app.features.productdetail

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import br.com.amanfron.ecommerce_app.R
import br.com.amanfron.ecommerce_app.features.productdetail.ProductDetailViewModel.ProductDetailViewState
import br.com.amanfron.ecommerce_app.ui.customviews.BottomNavigationBar
import br.com.amanfron.ecommerce_app.ui.customviews.LoadingView
import coil.compose.AsyncImage

@Composable
fun ProductDetailScreen(
    navController: NavController,
    viewModel: ProductDetailViewModel
) {
    val context = LocalContext.current
    val state = viewModel.state.value

    BottomNavigationBar(navController = navController) { paddingValues ->
        ProductDetailScreen(paddingValues = paddingValues, state)
    }

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
fun ProductDetailScreen(
    paddingValues: PaddingValues,
    state: ProductDetailViewState
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = paddingValues)
    ) {
        item {
            AsyncImage(
                model = state.product?.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = state.product?.title ?: "",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = state.product?.description ?: "",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Categoria: ${state.product?.categoryName}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Preço: ${state.product?.price}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }

    if (state.shouldShowLoading) {
        LoadingView()
    }
}

@Preview(showBackground = true)
@Composable
fun ProductDetailScreenPreview() {
    ProductDetailScreen(
        paddingValues = PaddingValues(),
        state = ProductDetailViewState()
    )
}