package br.com.amanfron.ecommerce_app.core.model

import br.com.amanfron.ecommerce_app.core.model.request.user.LoginRequest
import br.com.amanfron.ecommerce_app.core.model.request.user.RegisterRequest
import br.com.amanfron.ecommerce_app.core.model.response.product.Product
import br.com.amanfron.ecommerce_app.core.model.response.product.ProductResponse
import br.com.amanfron.ecommerce_app.core.model.response.user.LoginResponse
import br.com.amanfron.ecommerce_app.core.model.response.user.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AppService {
    @POST("api/auth/authenticate")
    suspend fun doLogin(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("api/auth/register")
    suspend fun doRegister(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @GET("api/products/rankeds")
    suspend fun getRankedProducts(): Response<ProductResponse>

    @GET("api/products")
    suspend fun getProductDetail(@Query("id") id: Int): Response<Product>
}