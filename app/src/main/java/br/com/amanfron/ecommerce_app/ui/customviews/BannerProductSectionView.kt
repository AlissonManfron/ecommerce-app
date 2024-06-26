package br.com.amanfron.ecommerce_app.ui.customviews

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.amanfron.ecommerce_app.core.model.response.product.Product
import br.com.amanfron.ecommerce_app.ui.theme.EcommerceAppTheme
import coil.compose.AsyncImage
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BannerProductSectionView(
    modifier: Modifier = Modifier,
    productList: List<Product>,
    pagerState: PagerState = rememberPagerState { productList.size },
    onProductClick: (product: Product) -> Unit
) {
    val totalPages = productList.size

    LaunchedEffect(pagerState.currentPage) {
        while (true) {
            delay(3000)
            pagerState.scrollToPage((pagerState.currentPage + 1) % totalPages)
        }
    }

    BannerProductSectionView(
        modifier,
        CardDefaults.cardColors(),
        productList,
        pagerState,
        onProductClick
    )
    PageIndicator(totalPages, pagerState.currentPage)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BannerProductSectionView(
    modifier: Modifier = Modifier,
    cardColors: CardColors,
    productList: List<Product>,
    pagerState: PagerState,
    onProductClick: (product: Product) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = cardColors,
        elevation = CardDefaults.elevatedCardElevation(8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
        ) { page ->
            Surface(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable { onProductClick.invoke(productList[page]) },
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = productList[page].imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(220.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = productList[page].title,
                        style = TextStyle(fontSize = 16.sp)
                    )
                }
            }
        }
    }
}

@Composable
fun PageIndicator(totalPages: Int, currentPage: Int) {
    Row(
        modifier = Modifier.padding(5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(totalPages) { index ->
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .padding(5.dp)
                    .background(
                        if (index == currentPage) MaterialTheme.colorScheme.primary else Color.Gray,
                        shape = RoundedCornerShape(10.dp)
                    )
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun BannerProductSectionViewPreview() = EcommerceAppTheme {
    BannerProductSectionView(
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
        onProductClick = {}
    )
}