package br.com.amanfron.ecommerce_app.core.model.response.product

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Product(
    @Json(name = "id")
    val id: Int,

    @Json(name = "title")
    val title: String,

    @Json(name = "description")
    val description: String,

    @Json(name = "imageurl")
    val imageUrl: String,

    @Json(name = "price")
    val price: String,

    @Json(name = "category_id")
    val categoryId: Int,

    @Json(name = "category_name")
    val categoryName: String
) : Parcelable