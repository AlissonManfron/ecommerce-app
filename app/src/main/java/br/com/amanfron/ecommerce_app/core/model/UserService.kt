package br.com.amanfron.ecommerce_app.core.model

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("api/auth/authenticate")
    suspend fun doLogin(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("api/auth/register")
    suspend fun doRegister(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

}