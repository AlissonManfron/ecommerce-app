package br.com.amanfron.ecommerce_app.core.domain.model

data class ProductCategory(
    val categoryId: Int,
    val categoryName: String,
    val products: List<Product>
)
