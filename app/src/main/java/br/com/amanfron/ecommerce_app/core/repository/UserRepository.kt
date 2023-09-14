package br.com.amanfron.ecommerce_app.core.repository

import br.com.amanfron.ecommerce_app.core.model.RegisterRequest
import br.com.amanfron.ecommerce_app.core.model.RegisterResponse
import br.com.amanfron.ecommerce_app.core.model.UserService
import br.com.amanfron.ecommerce_app.core.network.ResponseHandler
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val service: UserService,
    private val responseHandler: ResponseHandler
) {
    fun register(name: String, email: String, password: String): Flow<RegisterResponse> {
        return responseHandler.handleResponseFlow {
            service.doRegister(RegisterRequest(name, email, password))
        }
    }
}
