package br.com.amanfron.ecommerce_app.features.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.amanfron.ecommerce_app.core.model.response.user.LoginResponse
import br.com.amanfron.ecommerce_app.core.repository.AuthRepository
import br.com.amanfron.ecommerce_app.core.repository.UserRepository
import br.com.amanfron.ecommerce_app.features.login.LoginViewModel.LoginIntent.OnLoginClick
import br.com.amanfron.ecommerce_app.features.login.LoginViewModel.LoginIntent.SetEmail
import br.com.amanfron.ecommerce_app.features.login.LoginViewModel.LoginIntent.SetPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    private val _effect = MutableSharedFlow<LoginEffect>()
    val effect = _effect.asSharedFlow()

    fun onIntent(intent: LoginIntent) {
        when (intent) {
            is SetEmail -> setEmail(intent.email)
            is SetPassword -> setPassword(intent.password)
            is OnLoginClick -> onLoginButtonClick()
        }
    }

    private fun setEmail(newEmail: String) {
        _state.update {
            it.copy(email = newEmail)
        }
        checkFieldErrors()
    }

    private fun setPassword(newPassword: String) {
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

    private fun onLoginButtonClick() {
        val email = _state.value.email
        val password = _state.value.password

        if (email.isNotEmpty() && password.isNotEmpty()) {
            viewModelScope.launch {
                authRepository.login(email, password)
                    .onStart { shouldShowLoading(true) }
                    .onCompletion { shouldShowLoading(false) }
                    .catch { emitEffect(LoginEffect.ShowErrorToast) }
                    .collect(::onLoginSuccess)
            }
        }
    }

    private fun onLoginSuccess(response: LoginResponse) {
        userRepository.setUser(response.name, response.email, response.token)
        emitEffect(LoginEffect.NavigateToHome)
    }

    private fun emitEffect(effect: LoginEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }

    private fun shouldShowLoading(should: Boolean) {
        _state.update {
            it.copy(shouldShowLoading = should)
        }
    }

    data class LoginViewState(
        val email: String = "",
        val password: String = "",
        val shouldShowLoading: Boolean = false,
        val isEmailError: Boolean = false,
        val isPasswordError: Boolean = false
    )

    sealed interface LoginIntent {
        data class SetEmail(val email: String) : LoginIntent
        data class SetPassword(val password: String) : LoginIntent
        data object OnLoginClick : LoginIntent
    }

    sealed interface LoginEffect {
        data object NavigateToHome : LoginEffect
        data object ShowErrorToast : LoginEffect
    }
}
