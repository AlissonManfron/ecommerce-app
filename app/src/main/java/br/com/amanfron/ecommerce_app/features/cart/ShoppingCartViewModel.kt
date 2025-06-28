package br.com.amanfron.ecommerce_app.features.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.amanfron.ecommerce_app.core.local.ProductItem
import br.com.amanfron.ecommerce_app.core.local.toProduct
import br.com.amanfron.ecommerce_app.core.local.toProductItem
import br.com.amanfron.ecommerce_app.core.model.response.product.Product
import br.com.amanfron.ecommerce_app.core.repository.ShoppingCartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingCartViewModel @Inject constructor(
    private val ioDispatcher: CoroutineDispatcher,
    private val shoppingCartRepository: ShoppingCartRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ShoppingCartViewState())
    val state: StateFlow<ShoppingCartViewState> = _state.asStateFlow()

    init {
        viewModelScope.launch(ioDispatcher) {
            shoppingCartRepository.getProductItems()
                .take(1)
                .onStart { shouldShowLoading(true) }
                .onCompletion { shouldShowLoading(false) }
                .collect(::onGetProductItemsSuccess)
        }
    }

    private fun onGetProductItemsSuccess(productsItems: List<ProductItem>) {
        _state.update { state ->
            state.copy(
                products = productsItems.map { it.toProduct() },
                cartItemCount = productsItems.size
            )
        }
    }

    fun addProductToCart(product: Product) {
        viewModelScope.launch(ioDispatcher) {
            shoppingCartRepository.insertProductItem(product.toProductItem())
        }
        _state.update {
            it.copy(shouldShowCheckoutDialog = true)
        }
    }

    fun getProductsCount() {
        viewModelScope.launch(ioDispatcher) {
            shoppingCartRepository.getProductsCount()
                .catch { }
                .collect(::onGetProductsCountSuccess)
        }
    }

    private fun onGetProductsCountSuccess(count: Int) {
        _state.update { state ->
            state.copy(
                cartItemCount = count
            )
        }
    }

    private fun shouldShowLoading(should: Boolean) {
        _state.update {
            it.copy(shouldShowLoading = should)
        }
    }

    data class ShoppingCartViewState(
        val products: List<Product> = emptyList(),
        val cartItemCount: Int = 0,
        var shouldShowLoading: Boolean = false,
        var shouldShowDefaultError: Boolean = false,
        var shouldShowCheckoutDialog: Boolean = false
    )
}