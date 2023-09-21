package br.com.amanfron.ecommerce_app.core.model.response.product

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductResponse(
    @Json(name = "products")
    val products: List<Product>
)