package br.com.amanfron.ecommerce_app.core.model

import br.com.amanfron.ecommerce_app.core.model.request.user.LoginRequest
import br.com.amanfron.ecommerce_app.core.model.request.user.RegisterRequest
import br.com.amanfron.ecommerce_app.core.model.response.product.ProductResponse
import br.com.amanfron.ecommerce_app.core.model.response.user.LoginResponse
import br.com.amanfron.ecommerce_app.core.model.response.user.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AppService {
    @POST("api/auth/authenticate")
    suspend fun doLogin(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("api/auth/register")
    suspend fun doRegister(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @GET("api/products/search/rankeds")
    suspend fun getProductsRankeds(): Response<ProductResponse>
}