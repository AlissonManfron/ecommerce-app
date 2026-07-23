package br.com.amanfron.ecommerce_app.features.login

import androidx.lifecycle.viewModelScope
import br.com.amanfron.ecommerce_app.core.architecture.BaseViewModel
import br.com.amanfron.ecommerce_app.core.domain.usecase.LoginUseCase
import br.com.amanfron.ecommerce_app.features.login.LoginViewModel.LoginEffect
import br.com.amanfron.ecommerce_app.features.login.LoginViewModel.LoginIntent
import br.com.amanfron.ecommerce_app.features.login.LoginViewModel.LoginIntent.OnLoginClick
import br.com.amanfron.ecommerce_app.features.login.LoginViewModel.LoginIntent.SetEmail
import br.com.amanfron.ecommerce_app.features.login.LoginViewModel.LoginIntent.SetPassword
import br.com.amanfron.ecommerce_app.features.login.LoginViewModel.LoginViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : BaseViewModel<LoginViewState, LoginIntent, LoginEffect>(LoginViewState()) {

    override fun onIntent(intent: LoginIntent) {
        when (intent) {
            is SetEmail -> setEmail(intent.email)
            is SetPassword -> setPassword(intent.password)
            is OnLoginClick -> onLoginButtonClick()
        }
    }

    private fun setEmail(newEmail: String) {
        updateState {
            it.copy(email = newEmail)
        }
        checkFieldErrors()
    }

    private fun setPassword(newPassword: String) {
        updateState {
            it.copy(password = newPassword)
        }
        checkFieldErrors()
    }

    private fun checkFieldErrors() {
        updateState {
            it.copy(
                isEmailError = currentState.email.isEmpty(),
                isPasswordError = currentState.password.isEmpty()
            )
        }
    }

    private fun onLoginButtonClick() {
        val email = currentState.email
        val password = currentState.password

        if (email.isNotEmpty() && password.isNotEmpty()) {
            viewModelScope.launch {
                loginUseCase(email, password)
                    .onStart { shouldShowLoading(true) }
                    .onCompletion { shouldShowLoading(false) }
                    .catch { emitEffect(LoginEffect.ShowErrorToast) }
                    .collect { emitEffect(LoginEffect.NavigateToHome) }
            }
        }
    }

    private fun shouldShowLoading(should: Boolean) {
        updateState {
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
