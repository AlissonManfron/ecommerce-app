package br.com.amanfron.ecommerce_app.core.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest(
    @Json(name = "email")
    val email: String,

    @Json(name = "password")
    val password: String
)
