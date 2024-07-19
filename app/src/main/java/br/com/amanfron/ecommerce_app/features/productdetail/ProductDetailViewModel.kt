package br.com.amanfron.ecommerce_app.features.productdetail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.amanfron.ecommerce_app.core.model.response.product.Product
import br.com.amanfron.ecommerce_app.core.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: ProductRepository
) : ViewModel() {

    private val productId: Int? = savedStateHandle["productId"]

    private val _state = mutableStateOf(ProductDetailViewState())
    val state: State<ProductDetailViewState> = _state

    init {
        viewModelScope.launch {
            productId?.let {
                repository.getProductDetail(it)
                    .catch { onGetProductDetailError() }
                    .onStart { shouldShowLoading(true) }
                    .onCompletion { shouldShowLoading(false) }
                    .collect(::onGetProductDetailSuccess)
            }
        }
    }

    private fun onGetProductDetailError() {
        _state.value = state.value.copy(
            shouldShowDefaultError = true
        )
    }

    private fun onGetProductDetailSuccess(product: Product) {
        _state.value = state.value.copy(
            product = product
        )
    }

    private fun shouldShowLoading(should: Boolean) {
        _state.value = state.value.copy(
            shouldShowLoading = should
        )
    }

    data class ProductDetailViewState(
        var shouldShowLoading: Boolean = false,
        var shouldShowDefaultError: Boolean = false,
        val product: Product? = null
    )
}