package br.com.amanfron.ecommerce_app.features.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.amanfron.ecommerce_app.core.model.response.product.ProductResponse
import br.com.amanfron.ecommerce_app.core.model.response.product.RankedProductResponse
import br.com.amanfron.ecommerce_app.core.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _state = mutableStateOf(HomeViewState())
    val state: State<HomeViewState> = _state

    init {
        viewModelScope.launch {
            repository.getRankedProducts()
                .catch { onGetProductsError() }
                .onStart { shouldShowLoading(true) }
                .onCompletion { shouldShowLoading(false) }
                .collect(::onGetProductsSuccess)
        }
    }

    private fun onGetProductsSuccess(response: ProductResponse) {
        _state.value = state.value.copy(
            rankedProductList = response.rankedProductList
        )
    }

    private fun onGetProductsError() {
        _state.value = state.value.copy(
            shouldShowDefaultError = true
        )
    }

    private fun shouldShowLoading(should: Boolean) {
        _state.value = state.value.copy(
            shouldShowLoading = should
        )
    }

    data class HomeViewState(
        var shouldShowLoading: Boolean = false,
        var shouldShowDefaultError: Boolean = false,
        val rankedProductList: List<RankedProductResponse> = emptyList()
    )
}