package br.com.amanfron.ecommerce_app.core.architecture

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<out T>(val data: T) : UiState<T>
    data class Error(val message: String? = null) : UiState<Nothing>
}
