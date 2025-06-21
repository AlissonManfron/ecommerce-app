package br.com.amanfron.ecommerce_app.features.productdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.amanfron.ecommerce_app.core.model.response.product.Product
import br.com.amanfron.ecommerce_app.core.repository.ProductRepository
import br.com.amanfron.ecommerce_app.navigation.NavRoutes.PRODUCT_ID_PARAM
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
class ProductDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: ProductRepository
) : ViewModel() {

    private val productId: Int? = savedStateHandle[PRODUCT_ID_PARAM]

    private val _state = MutableStateFlow(ProductDetailViewState())
    val state: StateFlow<ProductDetailViewState> = _state.asStateFlow()

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
        _state.update {
            it.copy(shouldShowDefaultError = true)
        }
    }

    private fun onGetProductDetailSuccess(product: Product) {
        _state.update {
            it.copy(product = product)
        }
    }

    private fun shouldShowLoading(should: Boolean) {
        _state.update {
            it.copy(shouldShowLoading = should)
        }
    }

    data class ProductDetailViewState(
        var shouldShowLoading: Boolean = false,
        var shouldShowDefaultError: Boolean = false,
        val product: Product? = null
    )
}