package br.com.amanfron.ecommerce_app.features.createaccount

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CreateAccountViewModel : ViewModel() {
    private val _name = MutableStateFlow("")
    private val _email = MutableStateFlow("")
    private val _password = MutableStateFlow("")

    val name: StateFlow<String> = _name
    val email: StateFlow<String> = _email
    val password: StateFlow<String> = _password

    fun setName(newName: String) {
        _name.value = newName
    }

    fun setEmail(newEmail: String) {
        _email.value = newEmail
    }

    fun setPassword(newPassword: String) {
        _password.value = newPassword
    }

    fun onButtonCreateAccountClick() {
        val nameValue = name.value
        val emailValue = email.value
        val passwordValue = password.value

    }
}