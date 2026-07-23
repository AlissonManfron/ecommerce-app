package br.com.amanfron.ecommerce_app.features.productdetail

import androidx.lifecycle.viewModelScope
import br.com.amanfron.ecommerce_app.core.architecture.BaseViewModel
import br.com.amanfron.ecommerce_app.core.architecture.UiState
import br.com.amanfron.ecommerce_app.core.domain.model.Product
import br.com.amanfron.ecommerce_app.core.domain.usecase.AddProductToCartUseCase
import br.com.amanfron.ecommerce_app.core.domain.usecase.GetProductDetailUseCase
import br.com.amanfron.ecommerce_app.features.productdetail.ProductDetailViewModel.ProductDetailEffect
import br.com.amanfron.ecommerce_app.features.productdetail.ProductDetailViewModel.ProductDetailIntent
import br.com.amanfron.ecommerce_app.features.productdetail.ProductDetailViewModel.ProductDetailIntent.LoadProduct
import br.com.amanfron.ecommerce_app.features.productdetail.ProductDetailViewModel.ProductDetailIntent.OnAddToCartClick
import br.com.amanfron.ecommerce_app.features.productdetail.ProductDetailViewModel.ProductDetailIntent.OnBuyClick
import br.com.amanfron.ecommerce_app.features.productdetail.ProductDetailViewModel.ProductDetailViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductDetailUseCase: GetProductDetailUseCase,
    private val addProductToCartUseCase: AddProductToCartUseCase
) : BaseViewModel<UiState<ProductDetailViewState>, ProductDetailIntent, ProductDetailEffect>(UiState.Loading) {

    override fun onIntent(intent: ProductDetailIntent) {
        when (intent) {
            is LoadProduct -> getProduct(intent.productId)
            is OnBuyClick -> onBuyClick()
            is OnAddToCartClick -> onAddToCartClick()
        }
    }

    private fun getProduct(productId: Int) {
        viewModelScope.launch {
            getProductDetailUseCase(productId)
                .onStart { updateState { UiState.Loading } }
                .catch { onGetProductDetailError() }
                .collect(::onGetProductDetailSuccess)
        }
    }

    private fun onGetProductDetailError() {
        updateState { UiState.Error() }
        emitEffect(ProductDetailEffect.ShowErrorToast)
    }

    private fun onGetProductDetailSuccess(product: Product) {
        updateState {
            UiState.Success(ProductDetailViewState(product = product))
        }
    }

    private fun onBuyClick() {
        (currentState as? UiState.Success)?.data?.product?.let { product ->
            viewModelScope.launch {
                addProductToCartUseCase(product)
                emitEffect(ProductDetailEffect.NavigateToCart)
            }
        }
    }

    private fun onAddToCartClick() {
        (currentState as? UiState.Success)?.data?.product?.let { product ->
            viewModelScope.launch {
                addProductToCartUseCase(product)
                emitEffect(ProductDetailEffect.ShowAddToCartToast)
                emitEffect(ProductDetailEffect.NavigateBack)
            }
        }
    }

    data class ProductDetailViewState(
        val product: Product
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
