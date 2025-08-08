package br.com.amanfron.ecommerce_app.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.amanfron.ecommerce_app.core.model.response.product.ProductResponse
import br.com.amanfron.ecommerce_app.core.model.response.product.ProductCategoryResponse
import br.com.amanfron.ecommerce_app.core.model.response.product.ProductsResponse
import br.com.amanfron.ecommerce_app.core.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeViewState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getRankedProducts()
                .catch { onGetProductsError() }
                .onStart { shouldShowLoading(true) }
                .onCompletion { shouldShowLoading(false) }
                .collect(::onGetProductsSuccess)
        }
    }

    private fun onGetProductsSuccess(response: ProductsResponse) {
        _state.update {
            it.copy(
                bannerProductList = response.bannerProductList,
                rankedProductList = response.rankedProductList
            )
        }
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
        val bannerProductList: List<ProductResponse> = emptyList(),
        val rankedProductList: List<ProductCategoryResponse> = emptyList()
    )
}