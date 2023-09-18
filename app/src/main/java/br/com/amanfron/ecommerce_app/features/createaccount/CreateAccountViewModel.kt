package br.com.amanfron.ecommerce_app.features.createaccount

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.amanfron.ecommerce_app.core.model.RegisterResponse
import br.com.amanfron.ecommerce_app.core.repository.AuthRepository
import br.com.amanfron.ecommerce_app.core.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateAccountViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = mutableStateOf(CreateAccountViewState())
    val state: State<CreateAccountViewState> = _state

    fun setName(newName: String) {
        _state.value = state.value.copy(
            name = newName
        )
        verifyErrorFields()
    }

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
            isNameError = _state.value.name.isEmpty(),
            isEmailError = _state.value.email.isEmpty(),
            isPasswordError = _state.value.password.isEmpty()
        )
    }

    fun onButtonCreateAccountClick() {
        val name = _state.value.name
        val email = _state.value.email
        val password = _state.value.password

        if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
            viewModelScope.launch {
                authRepository.register(name, email, password)
                    .catch { onRegisterError() }
                    .onStart { shouldShowLoading(true) }
                    .onCompletion { shouldShowLoading(false) }
                    .collect(::onRegisterSuccess)
            }
        }
    }

    private fun onRegisterError() {
        _state.value = state.value.copy(
            shouldShowDefaultError = true
        )
    }

    private fun onRegisterSuccess(response: RegisterResponse) {
        userRepository.setUser(response.name, response.email, response.token)
        _state.value = state.value.copy(
            isRegisterIsSuccess = true
        )
    }

    private fun shouldShowLoading(should: Boolean) {
        _state.value = state.value.copy(
            shouldShowLoading = should
        )
    }

    data class CreateAccountViewState(
        var name: String = "",
        var email: String = "",
        var password: String = "",
        var shouldShowLoading: Boolean = false,
        var shouldShowDefaultError: Boolean = false,
        var isNameError: Boolean = false,
        var isEmailError: Boolean = false,
        var isPasswordError: Boolean = false,
        var isRegisterIsSuccess: Boolean = false
    )
}