package br.com.amanfron.ecommerce_app.ui.customviews

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.amanfron.ecommerce_app.core.model.response.product.Product
import br.com.amanfron.ecommerce_app.core.model.response.product.ProductCategoryResponse
import br.com.amanfron.ecommerce_app.ui.theme.EcommerceAppTheme
import br.com.amanfron.ecommerce_app.ui.theme.Typography

@Composable
fun ProductSectionView(
    modifier: Modifier = Modifier,
    rankedProductList: List<ProductCategoryResponse>,
    onSeeMoreClick: (categoryName: String) -> Unit,
    onProductClick: (product: Product) -> Unit
) {
    rankedProductList.forEach {
        ProductSectionView(
            modifier = modifier,
            categoryName = it.categoryName,
            productList = it.products,
            onSeeMoreClick = onSeeMoreClick,
            onProductClick = onProductClick
        )
    }
}

@Composable
fun ProductSectionView(
    modifier: Modifier = Modifier,
    categoryName: String,
    productList: List<Product>,
    onSeeMoreClick: (categoryName: String) -> Unit,
    onProductClick: (product: Product) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 16.dp, start = 16.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = categoryName,
                textAlign = TextAlign.Start,
                fontSize = Typography.bodyLarge.fontSize
            )

            TextButton(
                onClick = { },
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Text(
                    text = "Ver mais",
                    textAlign = TextAlign.End,
                    fontSize = Typography.bodyMedium.fontSize,
                    modifier = Modifier.clickable(
                        onClick = { onSeeMoreClick.invoke(categoryName) }
                    )
                )
            }
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            state = rememberLazyListState()
        ) {
            items(productList.size) { index ->
                ProductItem(
                    product = productList[index],
                    modifier = Modifier.padding(end = 8.dp),
                    onProductClick = onProductClick
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProductSectionPreview() = EcommerceAppTheme {
    ProductSectionView(
        categoryName = "Teste",
        productList = listOf(
            Product(
                0,
                "Title",
                "Description",
                "",
                "20.0",
                1,
                ""
            ),
        ),
        onSeeMoreClick = {},
        onProductClick = {}
    )
}