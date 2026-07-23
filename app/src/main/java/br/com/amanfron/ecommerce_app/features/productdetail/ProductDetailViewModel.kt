package br.com.amanfron.ecommerce_app.features.productdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.amanfron.ecommerce_app.core.domain.model.Product
import br.com.amanfron.ecommerce_app.core.domain.usecase.AddProductToCartUseCase
import br.com.amanfron.ecommerce_app.core.domain.usecase.GetProductDetailUseCase
import br.com.amanfron.ecommerce_app.features.productdetail.ProductDetailViewModel.ProductDetailIntent.LoadProduct
import br.com.amanfron.ecommerce_app.features.productdetail.ProductDetailViewModel.ProductDetailIntent.OnAddToCartClick
import br.com.amanfron.ecommerce_app.features.productdetail.ProductDetailViewModel.ProductDetailIntent.OnBuyClick
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
class ProductDetailViewModel @Inject constructor(
    private val getProductDetailUseCase: GetProductDetailUseCase,
    private val addProductToCartUseCase: AddProductToCartUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProductDetailViewState())
    val state: StateFlow<ProductDetailViewState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ProductDetailEffect>()
    val effect = _effect.asSharedFlow()

    fun onIntent(intent: ProductDetailIntent) {
        when (intent) {
            is LoadProduct -> getProduct(intent.productId)
            is OnBuyClick -> onBuyClick()
            is OnAddToCartClick -> onAddToCartClick()
        }
    }

    private fun getProduct(productId: Int) {
        viewModelScope.launch {
            getProductDetailUseCase(productId)
                .catch { onGetProductDetailError() }
                .onStart { shouldShowLoading(true) }
                .onCompletion { shouldShowLoading(false) }
                .collect(::onGetProductDetailSuccess)
        }
    }

    private fun onGetProductDetailError() {
        emitEffect(ProductDetailEffect.ShowErrorToast)
    }

    private fun onGetProductDetailSuccess(product: Product) {
        _state.update {
            it.copy(product = product)
        }
    }

    private fun onBuyClick() {
        _state.value.product?.let { product ->
            viewModelScope.launch {
                addProductToCartUseCase(product)
                emitEffect(ProductDetailEffect.NavigateToCart)
            }
        }
    }

    private fun onAddToCartClick() {
        _state.value.product?.let { product ->
            viewModelScope.launch {
                addProductToCartUseCase(product)
                emitEffect(ProductDetailEffect.ShowAddToCartToast)
                emitEffect(ProductDetailEffect.NavigateBack)
            }
        }
    }

    private fun emitEffect(effect: ProductDetailEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }

    private fun shouldShowLoading(should: Boolean) {
        _state.update {
            it.copy(shouldShowLoading = should)
        }
    }

    data class ProductDetailViewState(
        val shouldShowLoading: Boolean = false,
        val product: Product? = null
    )

    sealed interface ProductDetailIntent {
        data class LoadProduct(val productId: Int) : ProductDetailIntent
        data object OnBuyClick : ProductDetailIntent
        data object OnAddToCartClick : ProductDetailIntent
    }

    sealed interface ProductDetailEffect {
        data object NavigateToCart : ProductDetailEffect
        data object NavigateBack : ProductDetailEffect
        data object ShowErrorToast : ProductDetailEffect
        data object ShowAddToCartToast : ProductDetailEffect
    }
}
