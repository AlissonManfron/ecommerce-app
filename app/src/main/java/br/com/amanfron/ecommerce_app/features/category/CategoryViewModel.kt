package br.com.amanfron.ecommerce_app.features.category

import androidx.lifecycle.viewModelScope
import br.com.amanfron.ecommerce_app.core.architecture.BaseViewModel
import br.com.amanfron.ecommerce_app.core.architecture.UiState
import br.com.amanfron.ecommerce_app.core.domain.model.Product
import br.com.amanfron.ecommerce_app.core.domain.usecase.GetProductsByCategoryUseCase
import br.com.amanfron.ecommerce_app.features.category.CategoryIntent.LoadProducts
import br.com.amanfron.ecommerce_app.features.category.CategoryIntent.OnProductClick
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getProductsByCategoryUseCase: GetProductsByCategoryUseCase
) : BaseViewModel<UiState<CategoryViewState>, CategoryIntent, CategoryEffect>(UiState.Loading) {

    override fun onIntent(intent: CategoryIntent) {
        when (intent) {
            is LoadProducts -> fetchProducts(intent.categoryId)
            is OnProductClick -> emitEffect(CategoryEffect.NavigateToProductDetail(intent.productId))
        }
    }

    private fun fetchProducts(categoryId: Int) {
        viewModelScope.launch {
            getProductsByCategoryUseCase(categoryId)
                .onStart { updateState { UiState.Loading } }
                .catch { updateState { UiState.Error() } }
                .collect { products ->
                    updateState {
                        UiState.Success(CategoryViewState(products = products))
                    }
                }
        }
    }
}

data class CategoryViewState(
    val products: List<Product> = emptyList()
)

sealed interface CategoryIntent {
    data class LoadProducts(val categoryId: Int) : CategoryIntent
    data class OnProductClick(val productId: Int) : CategoryIntent
}

sealed interface CategoryEffect {
    data class NavigateToProductDetail(val productId: Int) : CategoryEffect
}
