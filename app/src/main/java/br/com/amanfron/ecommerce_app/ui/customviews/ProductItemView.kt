package br.com.amanfron.ecommerce_app.ui.customviews

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.amanfron.ecommerce_app.core.model.response.product.Product
import br.com.amanfron.ecommerce_app.ui.theme.EcommerceAppTheme
import br.com.amanfron.ecommerce_app.ui.theme.Typography
import coil.compose.AsyncImage

@Composable
fun ProductItem(
    modifier: Modifier = Modifier,
    product: Product,
    onProductClick: (product: Product) -> Unit
) {
    Card(
        modifier = modifier
            .width(120.dp)
            .height(180.dp)
            .clickable { onProductClick.invoke(product) },
        shape = MaterialTheme.shapes.medium,
        elevation =  CardDefaults.cardElevation(8.dp),
    ) {
        Column(
            Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3f)
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .weight(1f)
                    .padding(top = 5.dp),
                text = product.title,
                fontSize = Typography.bodySmall.fontSize,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 14.sp
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .weight(0.6f),
                text = "R$ ${product.price}",
                fontWeight = FontWeight.Bold,
                fontSize = Typography.bodySmall.fontSize,
                lineHeight = 14.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductItemPreview() = EcommerceAppTheme {
    ProductItem(
        product = Product(
            0,
            "Title",
            "Description",
            "",
            "20.0",
            1,
            ""
        ),
        onProductClick = {}
    )
}