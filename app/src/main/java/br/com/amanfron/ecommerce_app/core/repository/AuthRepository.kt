package br.com.amanfron.ecommerce_app.core.repository

import br.com.amanfron.ecommerce_app.core.model.AppService
import br.com.amanfron.ecommerce_app.core.model.request.user.LoginRequest
import br.com.amanfron.ecommerce_app.core.model.request.user.RegisterRequest
import br.com.amanfron.ecommerce_app.core.model.response.user.LoginResponse
import br.com.amanfron.ecommerce_app.core.model.response.user.RegisterResponse
import br.com.amanfron.ecommerce_app.core.network.ResponseHandler
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val service: AppService,
    private val responseHandler: ResponseHandler
) {
    fun login(email: String, password: String): Flow<LoginResponse> {
        return responseHandler.handleResponseFlow {
            service.doLogin(LoginRequest(email, password))
        }
    }

    fun register(name: String, email: String, password: String): Flow<RegisterResponse> {
        return responseHandler.handleResponseFlow {
            service.doRegister(RegisterRequest(name, email, password))
        }
    }
}