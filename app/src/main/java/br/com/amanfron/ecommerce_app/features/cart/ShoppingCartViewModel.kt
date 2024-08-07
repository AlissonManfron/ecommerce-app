package br.com.amanfron.ecommerce_app.features.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingCartViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(ShoppingCartViewState())
    val state: StateFlow<ShoppingCartViewState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            delay(2000)
            shouldShowLoading(false)
        }
    }

    private fun shouldShowLoading(should: Boolean) {
        _state.update {
            it.copy(shouldShowLoading = should)
        }
    }

    data class ShoppingCartViewState(
        var shouldShowLoading: Boolean = true,
        var shouldShowDefaultError: Boolean = false
    )
}