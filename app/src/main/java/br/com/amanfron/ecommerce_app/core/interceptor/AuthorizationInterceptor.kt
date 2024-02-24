package br.com.amanfron.ecommerce_app.core.interceptor

import br.com.amanfron.ecommerce_app.core.repository.UserRepository
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthorizationInterceptor @Inject constructor(
    private val userRepository: UserRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        if (userRepository.token.isNotEmpty()) {
            request = request.newBuilder()
                .addHeader("Authorization", "Bearer ${userRepository.token}")
                .build()
        }

        return chain.proceed(request)
    }
}