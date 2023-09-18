package br.com.amanfron.ecommerce_app.di

import br.com.amanfron.ecommerce_app.core.interceptor.AuthorizationInterceptor
import br.com.amanfron.ecommerce_app.core.model.UserService
import br.com.amanfron.ecommerce_app.core.network.ResponseHandler
import br.com.amanfron.ecommerce_app.core.network.ResponseHandlerImpl
import br.com.amanfron.ecommerce_app.core.repository.AuthRepository
import br.com.amanfron.ecommerce_app.core.repository.UserRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "https://ecommerce-api-zn1k.onrender.com/"

    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    fun provideOkHttpClient(
        userRepository: UserRepository
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(40, TimeUnit.SECONDS)
            .addInterceptor(AuthorizationInterceptor(userRepository))
            .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    fun provideResponseHandler(): ResponseHandler {
        return ResponseHandlerImpl()
    }

    @Provides
    fun provideAuthRepository(
        userService: UserService,
        responseHandler: ResponseHandler
    ): AuthRepository {
        return AuthRepository(userService, responseHandler)
    }
}
