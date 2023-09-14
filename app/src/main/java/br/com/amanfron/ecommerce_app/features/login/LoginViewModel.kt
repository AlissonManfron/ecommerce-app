package br.com.amanfron.ecommerce_app.features.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.amanfron.ecommerce_app.core.model.LoginResponse
import br.com.amanfron.ecommerce_app.core.repository.AuthRepository
import br.com.amanfron.ecommerce_app.core.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = mutableStateOf(LoginViewState())
    val state: State<LoginViewState> = _state

    fun setEmail(newEmail: String) {
        _state.value = state.value.copy(
            email = newEmail
        )
        verifyErrorFields()
    }

    fun setPassword(newPassword: String) {
        _state.value = state.value.copy(
            password = newPassword
        )
        verifyErrorFields()
    }

    private fun verifyErrorFields() {
        _state.value = state.value.copy(
            isEmailError = _state.value.email.isEmpty(),
            isPasswordError = _state.value.password.isEmpty()
        )
    }

    fun onButtonLoginClick() {
        val email = _state.value.email
        val password = _state.value.password

        if (email.isNotEmpty() && password.isNotEmpty()) {
            viewModelScope.launch {
                repository.login(email, password)
                    .catch { onLoginError() }
                    .onStart { shouldShowLoading(true) }
                    .onCompletion { shouldShowLoading(false) }
                    .collect(::onLoginSuccess)
            }
        }
    }

    private fun onLoginSuccess(response: LoginResponse) {
        authRepository.setUser(response.name, response.email, response.token)
        _state.value = state.value.copy(
            isSuccessLogin = true
        )
    }

    private fun onLoginError() {
        _state.value = state.value.copy(
            shouldShowDefaultError = true
        )
    }

    private fun shouldShowLoading(should: Boolean) {
        _state.value = state.value.copy(
            shouldShowLoading = should
        )
    }

    data class LoginViewState(
        var email: String = "",
        var password: String = "",
        var shouldShowLoading: Boolean = false,
        var shouldShowDefaultError: Boolean = false,
        var isEmailError: Boolean = false,
        var isPasswordError: Boolean = false,
        var isSuccessLogin: Boolean = false
    )
}