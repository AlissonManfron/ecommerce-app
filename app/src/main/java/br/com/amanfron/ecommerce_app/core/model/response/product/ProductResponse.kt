package br.com.amanfron.ecommerce_app.core.model.response.product

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductResponse(
    @Json(name = "rankedProducts")
    val rankedProductList: List<RankedProductResponse>
)

@JsonClass(generateAdapter = true)
data class RankedProductResponse(
    @Json(name = "category_name")
    val categoryName: String,

    @Json(name = "products")
    val products: List<Product>
)