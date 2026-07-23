package br.com.amanfron.ecommerce_app.core.domain.usecase

import br.com.amanfron.ecommerce_app.core.model.response.user.LoginResponse
import br.com.amanfron.ecommerce_app.core.repository.AuthRepository
import br.com.amanfron.ecommerce_app.core.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    operator fun invoke(email: String, password: String): Flow<LoginResponse> {
        return authRepository.login(email, password).onEach { response ->
            userRepository.setUser(response.name, response.email, response.token)
        }
    }
}
