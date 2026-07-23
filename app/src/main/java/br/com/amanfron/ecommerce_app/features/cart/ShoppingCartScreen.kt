package br.com.amanfron.ecommerce_app.features.cart

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.amanfron.ecommerce_app.R
import br.com.amanfron.ecommerce_app.core.domain.model.Product
import br.com.amanfron.ecommerce_app.features.cart.ShoppingCartViewModel.ShoppingCartEffect.ShowErrorToast
import br.com.amanfron.ecommerce_app.features.cart.ShoppingCartViewModel.ShoppingCartViewState
import br.com.amanfron.ecommerce_app.ui.customviews.LoadingContentView
import br.com.amanfron.ecommerce_app.ui.theme.EcommerceAppTheme
import br.com.amanfron.ecommerce_app.ui.theme.Typography
import br.com.amanfron.ecommerce_app.ui.utils.ObserveAsEvents
import coil.compose.AsyncImage

@Composable
fun ShoppingCartScreen(
    modifier: Modifier = Modifier,
    viewModel: ShoppingCartViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    viewModel.effect.ObserveAsEvents { effect ->
        when (effect) {
            is ShowErrorToast -> {
                Toast.makeText(context, R.string.try_again_message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    ShoppingCartContent(
        modifier = modifier,
        state = state,
        onDecreaseQuantityClick = viewModel::onDecreaseQuantityClick,
        onIncreaseQuantityClick = viewModel::onIncreaseQuantityClick
    )
}

@Composable
fun ShoppingCartContent(
    modifier: Modifier = Modifier,
    state: ShoppingCartViewState,
    onDecreaseQuantityClick: (product: Product) -> Unit,
    onIncreaseQuantityClick: (product: Product) -> Unit,
) {
    LoadingContentView(shouldShowLoading = state.shouldShowLoading) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Spacer(modifier = Modifier.size(size = 8.dp))
                    Text(
                        text = "Seu carrinho",
                        fontSize = Typography.bodyLarge.fontSize,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.size(size = 8.dp))
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.secondaryContainer
                    )
                }
            },
            bottomBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.secondaryContainer)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total",
                            fontSize = Typography.bodyLarge.fontSize,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = state.totalPrice,
                            fontSize = Typography.bodyLarge.fontSize,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.size(size = 16.dp))
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.background
                    )
                    Spacer(modifier = Modifier.size(size = 16.dp))
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        onClick = { }
                    ) {
                        Text("Finalizar")
                    }
                    Spacer(modifier = Modifier.size(size = 16.dp))
                }
            }
        ) { paddingValues ->
            if (state.products.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_shopping_cart_off),
                        contentDescription = null,
                        modifier = Modifier.size(size = 128.dp),
                        tint = MaterialTheme.colorScheme.secondaryContainer
                    )
                    Text(
                        text = "Sem produtos no carrinho,\nadicione um produto!",
                        fontSize = Typography.bodyLarge.fontSize,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp,
                        bottom = 8.dp
                    )
                ) {
                    items(items = state.products, key = { it.id }) { product ->
                        ProductItem(
                            product = product,
                            onProductClick = {},
                            onDecreaseQuantityClick = onDecreaseQuantityClick,
                            onIncreaseQuantityClick = onIncreaseQuantityClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItem(
    modifier: Modifier = Modifier,
    product: Product,
    onProductClick: (productId: Int) -> Unit,
    onDecreaseQuantityClick: (product: Product) -> Unit,
    onIncreaseQuantityClick: (product: Product) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onProductClick.invoke(product.id) },
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(8.dp),
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .width(96.dp)
                    .height(96.dp)
                    .padding(all = 8.dp),
                model = product.imageUrl,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = product.title,
                    fontSize = Typography.bodySmall.fontSize,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 14.sp
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "R$ ${product.price}",
                    fontWeight = FontWeight.Bold,
                    fontSize = Typography.bodySmall.fontSize,
                    lineHeight = 14.sp
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedIconButton(
                        onClick = {
                            onDecreaseQuantityClick(product)
                        },
                        modifier = Modifier.size(12.dp),
                        shape = CircleShape,
                        border = BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                        ),
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_remove),
                            contentDescription = "Diminuir quantidade",
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = product.quantity.toString(),
                        fontSize = Typography.bodySmall.fontSize
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    OutlinedIconButton(
                        onClick = {
                            onIncreaseQuantityClick(product)
                        },
                        modifier = Modifier.size(12.dp),
                        shape = CircleShape,
                        border = BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Aumentar quantidade",
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShoppingCartScreenPreview() = EcommerceAppTheme {
    ShoppingCartContent(
        modifier = Modifier.fillMaxSize(),
        state = ShoppingCartViewState(
            products = listOf(
                Product(
                    id = 1,
                    title = "Product 1",
                    price = "100,00",
                    description = "Description 1",
                    imageUrl = "https://via.placeholder.com/150",
                    categoryName = "Category 1",
                    categoryId = 1,
                    quantity = 1
                ),
                Product(
                    id = 2,
                    title = "Product 2",
                    price = "170,00",
                    description = "Description 2",
                    imageUrl = "https://via.placeholder.com/167",
                    categoryName = "Category 3",
                    categoryId = 2,
                    quantity = 1
                ),
            ),
            totalPrice = "R$ 270,00"
        ),
        onDecreaseQuantityClick = {},
        onIncreaseQuantityClick = {}
    )
}
