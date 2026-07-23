package br.com.amanfron.ecommerce_app.features.cart

import androidx.lifecycle.viewModelScope
import br.com.amanfron.ecommerce_app.core.architecture.BaseViewModel
import br.com.amanfron.ecommerce_app.core.domain.model.Product
import br.com.amanfron.ecommerce_app.core.domain.usecase.DeleteCartItemUseCase
import br.com.amanfron.ecommerce_app.core.domain.usecase.GetCartItemsUseCase
import br.com.amanfron.ecommerce_app.core.domain.usecase.GetProductsCountUseCase
import br.com.amanfron.ecommerce_app.core.local.ProductItem
import br.com.amanfron.ecommerce_app.core.local.toProduct
import br.com.amanfron.ecommerce_app.core.utils.calculateTotal
import br.com.amanfron.ecommerce_app.core.utils.decrementQuantity
import br.com.amanfron.ecommerce_app.core.utils.incrementQuantity
import br.com.amanfron.ecommerce_app.core.utils.shouldRemove
import br.com.amanfron.ecommerce_app.core.utils.toCurrency
import br.com.amanfron.ecommerce_app.features.cart.ShoppingCartViewModel.ShoppingCartEffect
import br.com.amanfron.ecommerce_app.features.cart.ShoppingCartViewModel.ShoppingCartEffect.ShowErrorToast
import br.com.amanfron.ecommerce_app.features.cart.ShoppingCartViewModel.ShoppingCartIntent
import br.com.amanfron.ecommerce_app.features.cart.ShoppingCartViewModel.ShoppingCartIntent.OnDecreaseQuantityClick
import br.com.amanfron.ecommerce_app.features.cart.ShoppingCartViewModel.ShoppingCartIntent.OnIncreaseQuantityClick
import br.com.amanfron.ecommerce_app.features.cart.ShoppingCartViewModel.ShoppingCartViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingCartViewModel @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val getProductsCountUseCase: GetProductsCountUseCase,
    private val deleteCartItemUseCase: DeleteCartItemUseCase
) : BaseViewModel<ShoppingCartViewState, ShoppingCartIntent, ShoppingCartEffect>(ShoppingCartViewState()) {

    init {
        viewModelScope.launch(ioDispatcher) {
            getCartItemsUseCase()
                .take(1)
                .onStart { shouldShowLoading(true) }
                .onCompletion { shouldShowLoading(false) }
                .collect(::onGetProductItemsSuccess)
        }
    }

    override fun onIntent(intent: ShoppingCartIntent) {
        when (intent) {
            is OnIncreaseQuantityClick -> onIncreaseQuantity(intent.product)
            is OnDecreaseQuantityClick -> onDecreaseQuantity(intent.product)
        }
    }

    private fun onGetProductItemsSuccess(productsItems: List<ProductItem>) {
        val products = productsItems.map { it.toProduct() }
        updateState { state ->
            state.copy(
                products = products,
                cartItemCount = products.size,
                totalPrice = products.calculateTotal().toCurrency()
            )
        }
    }

    private fun onIncreaseQuantity(selectedProduct: Product) {
        val updatedProducts = currentState.products.map { product ->
            if (product.id == selectedProduct.id) {
                product.incrementQuantity()
            } else {
                product
            }
        }

        updateState { currentState ->
            currentState.copy(
                products = updatedProducts,
                totalPrice = updatedProducts.calculateTotal().toCurrency()
            )
        }
    }

    private fun onDecreaseQuantity(selectedProduct: Product) {
        val updatedProducts = currentState.products.mapNotNull { product ->
            if (product.id == selectedProduct.id) {
                if (product.shouldRemove) {
                    deleteProductFromCart(product)
                    null
                } else {
                    product.decrementQuantity()
                }
            } else {
                product
            }
        }

        updateState { currentState ->
            currentState.copy(
                products = updatedProducts,
                totalPrice = updatedProducts.calculateTotal().toCurrency(),
                cartItemCount = updatedProducts.size
            )
        }
    }

    private fun deleteProductFromCart(product: Product) {
        viewModelScope.launch(ioDispatcher) {
            deleteCartItemUseCase(product)
        }
    }

    fun getProductsCount() {
        viewModelScope.launch(ioDispatcher) {
            getProductsCountUseCase()
                .catch {
                    emitEffect(ShowErrorToast)
                }
                .collect(::onGetProductsCountSuccess)
        }
    }

    private fun onGetProductsCountSuccess(count: Int) {
        updateState { state ->
            state.copy(
                cartItemCount = count
            )
        }
    }

    private fun shouldShowLoading(should: Boolean) {
        updateState {
            it.copy(shouldShowLoading = should)
        }
    }

    data class ShoppingCartViewState(
        val products: List<Product> = emptyList(),
        val cartItemCount: Int = 0,
        val totalPrice: String = "",
        val shouldShowLoading: Boolean = false
    )

    sealed interface ShoppingCartIntent {
        data class OnIncreaseQuantityClick(val product: Product) : ShoppingCartIntent
        data class OnDecreaseQuantityClick(val product: Product) : ShoppingCartIntent
    }

    sealed interface ShoppingCartEffect {
        data object ShowErrorToast : ShoppingCartEffect
    }
}
