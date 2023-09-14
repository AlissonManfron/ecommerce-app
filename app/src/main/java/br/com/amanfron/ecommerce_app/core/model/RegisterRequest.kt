package br.com.amanfron.ecommerce_app.core.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterRequest(
    @Json(name = "name")
    val name: String,

    @Json(name = "email")
    val email: String,

    @Json(name = "password")
    val password: String
)