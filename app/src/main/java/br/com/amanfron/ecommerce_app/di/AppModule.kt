package br.com.amanfron.ecommerce_app.di

import br.com.amanfron.ecommerce_app.core.interceptor.AuthorizationInterceptor
import br.com.amanfron.ecommerce_app.core.model.AppService
import br.com.amanfron.ecommerce_app.core.network.ResponseHandler
import br.com.amanfron.ecommerce_app.core.network.ResponseHandlerImpl
import br.com.amanfron.ecommerce_app.core.repository.AuthRepository
import br.com.amanfron.ecommerce_app.core.repository.ProductRepository
import br.com.amanfron.ecommerce_app.core.repository.UserRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


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
            .addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            )
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
    fun provideUserService(retrofit: Retrofit): AppService {
        return retrofit.create(AppService::class.java)
    }

    @Provides
    fun provideResponseHandler(): ResponseHandler {
        return ResponseHandlerImpl()
    }

    @Provides
    fun provideAuthRepository(
        service: AppService,
        responseHandler: ResponseHandler
    ): AuthRepository {
        return AuthRepository(service, responseHandler)
    }

    @Provides
    fun provideProductRepository(
        service: AppService,
        responseHandler: ResponseHandler
    ): ProductRepository {
        return ProductRepository(service, responseHandler)
    }

    @Provides
    @Singleton
    fun provideUserRepository(): UserRepository {
        return UserRepository()
    }
}
