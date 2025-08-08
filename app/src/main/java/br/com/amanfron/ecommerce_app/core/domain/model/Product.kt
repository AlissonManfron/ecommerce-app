package br.com.amanfron.ecommerce_app.core.domain.model

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String,
    val price: String,
    val categoryId: Int,
    val categoryName: String,
    var quantity: Int = 1
)