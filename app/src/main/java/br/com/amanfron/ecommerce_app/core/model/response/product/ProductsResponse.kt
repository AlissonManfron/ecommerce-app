package br.com.amanfron.ecommerce_app.core.model.response.product

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductsResponse(
    @Json(name = "bannerProducts")
    val bannerProductList: List<ProductResponse>,
    @Json(name = "rankedProducts")
    val rankedProductList: List<ProductCategoryResponse>,
)

@JsonClass(generateAdapter = true)
data class ProductCategoryResponse(
    @Json(name = "category_name")
    val categoryName: String,

    @Json(name = "category_id")
    val categoryId: Int,

    @Json(name = "products")
    val products: List<ProductResponse>
)