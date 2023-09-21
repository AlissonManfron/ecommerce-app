package br.com.amanfron.ecommerce_app.core.model.response.user

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponse(
    @Json(name = "name")
    val name: String,

    @Json(name = "email")
    val email: String,

    @Json(name = "token")
    val token: String
)