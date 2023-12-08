package br.com.amanfron.ecommerce_app.ui.customviews

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.amanfron.ecommerce_app.ui.theme.Typography
import coil.compose.AsyncImage

@Composable
fun ProductItem(
    modifier: Modifier = Modifier,
    productId: Int,
    imageUrl: String,
    title: String,
    price: String,
    onProductClick: (productId: Int) -> Unit
) {
    Column(
        modifier
            .width(120.dp)
            .height(180.dp)
            .shadow(1.dp, shape = RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(10.dp)
            .clickable { onProductClick.invoke(productId) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .weight(1f)
                .padding(top = 5.dp),
            text = title,
            fontSize = Typography.bodySmall.fontSize,
            color = Color.Black,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 14.sp
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .weight(0.6f),
            text = "R$ $price",
            fontWeight = FontWeight.Bold,
            fontSize = Typography.bodySmall.fontSize,
            color = Color.Black,
            lineHeight = 14.sp
        )
    }
}