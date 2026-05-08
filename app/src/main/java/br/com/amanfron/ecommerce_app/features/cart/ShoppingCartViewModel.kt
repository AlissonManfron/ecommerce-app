package br.com.amanfron.ecommerce_app.features.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.amanfron.ecommerce_app.core.domain.model.Product
import br.com.amanfron.ecommerce_app.core.local.ProductItem
import br.com.amanfron.ecommerce_app.core.local.toProduct
import br.com.amanfron.ecommerce_app.core.local.toProductItem
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
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale
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
        val products = productsItems.map { it.toProduct() }
        _state.update { state ->
            state.copy(
                products = products,
                cartItemCount = products.size,
                totalPrice = calculateTotalPrice(products = products)
            )
        }
    }

    fun calculateTotalPrice(products: List<Product>): String {
        var totalPrice = BigDecimal.ZERO

        for (product in products) {
            try {
                val priceDecimal = BigDecimal(product.price)
                val totalItemPrice = priceDecimal.multiply(BigDecimal(product.quantity))
                totalPrice = totalPrice.add(totalItemPrice)
            } catch (e: NumberFormatException) {
            }
        }

        val locale = Locale("pt", "BR")
        val currencyFormatter =
            NumberFormat.getCurrencyInstance(locale)
        return currencyFormatter.format(totalPrice)
    }

    fun onIncreaseQuantityClick(selectedProduct: Product) {
        val updatedProducts = _state.value.products.map { product ->
            if (product.id == selectedProduct.id) {
                product.copy(quantity = product.quantity + 1)
            } else {
                product
            }
        }

        _state.update { currentState ->
            currentState.copy(
                products = updatedProducts,
                totalPrice = calculateTotalPrice(products = updatedProducts)
            )
        }
    }

    fun onDecreaseQuantityClick(selectedProduct: Product) {
        val updatedProducts = _state.value.products.mapNotNull { product ->
            if (product.id == selectedProduct.id) {
                val newQuantity = product.quantity - 1
                if (newQuantity <= 0) {
                    deleteProductToCart(product)
                    null
                } else {
                    product.copy(quantity = newQuantity)
                }
            } else {
                product
            }
        }

        _state.update { currentState ->
            currentState.copy(
                products = updatedProducts,
                totalPrice = calculateTotalPrice(products = updatedProducts),
                cartItemCount = updatedProducts.size
            )
        }
    }

    private fun deleteProductToCart(product: Product) {
        viewModelScope.launch(ioDispatcher) {
            shoppingCartRepository.deleteProductItem(product.toProductItem())
        }
    }

    fun addProductToCart(product: Product) {
        viewModelScope.launch(ioDispatcher) {
            shoppingCartRepository.insertProductItem(product.toProductItem())
        }
    }

    fun clearDefaultError() {
        _state.update {
            it.copy(shouldShowDefaultError = false)
        }
    }

    fun getProductsCount() {
        viewModelScope.launch(ioDispatcher) {
            shoppingCartRepository.getProductsCount()
                .catch {
                    _state.update { currentState ->
                        currentState.copy(shouldShowDefaultError = true)
                    }
                }
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
        val totalPrice: String = "",
        val shouldShowLoading: Boolean = false,
        val shouldShowDefaultError: Boolean = false
    )
}