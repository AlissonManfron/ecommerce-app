package br.com.amanfron.ecommerce_app.features.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.amanfron.ecommerce_app.core.model.response.user.LoginResponse
import br.com.amanfron.ecommerce_app.core.repository.AuthRepository
import br.com.amanfron.ecommerce_app.core.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginViewState())
    val state: StateFlow<LoginViewState> = _state.asStateFlow()

    fun setEmail(newEmail: String) {
        _state.update {
            it.copy(email = newEmail)
        }
        checkFieldErrors()
    }

    fun setPassword(newPassword: String) {
        _state.update {
            it.copy(password = newPassword)
        }
        checkFieldErrors()
    }

    private fun checkFieldErrors() {
        _state.update {
            it.copy(
                isEmailError = _state.value.email.isEmpty(),
                isPasswordError = _state.value.password.isEmpty()
            )
        }
    }

    fun onButtonLoginClick() {
        val email = _state.value.email
        val password = _state.value.password

        if (email.isNotEmpty() && password.isNotEmpty()) {
            viewModelScope.launch {
                authRepository.login(email, password)
                    .catch { onLoginError() }
                    .onStart { shouldShowLoading(true) }
                    .onCompletion { shouldShowLoading(false) }
                    .collect(::onLoginSuccess)
            }
        }
    }

    private fun onLoginSuccess(response: LoginResponse) {
        userRepository.setUser(response.name, response.email, response.token)
        _state.update {
            it.copy(isSuccessLogin = true)
        }
    }

    private fun onLoginError() {
        _state.update {
            it.copy(shouldShowDefaultError = true)
        }
    }

    private fun shouldShowLoading(should: Boolean) {
        _state.update {
            it.copy(shouldShowLoading = should)
        }
    }

    data class LoginViewState(
        var email: String = "a@a.com",
        var password: String = "12345",
        var shouldShowLoading: Boolean = false,
        var shouldShowDefaultError: Boolean = false,
        var isEmailError: Boolean = false,
        var isPasswordError: Boolean = false,
        var isSuccessLogin: Boolean = false
    )
}