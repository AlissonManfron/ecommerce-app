package br.com.amanfron.ecommerce_app.core.domain.model

data class RankedProducts(
    val bannerProducts: List<Product>,
    val rankedProducts: List<ProductCategory>
)
