package br.com.amanfron.ecommerce_app.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.amanfron.ecommerce_app.core.domain.usecase.GetRankedProductsUseCase
import br.com.amanfron.ecommerce_app.core.model.response.product.ProductCategoryResponse
import br.com.amanfron.ecommerce_app.core.model.response.product.ProductResponse
import br.com.amanfron.ecommerce_app.core.model.response.product.ProductsResponse
import br.com.amanfron.ecommerce_app.features.home.HomeViewModel.HomeEffect.NavigateToProductDetail
import br.com.amanfron.ecommerce_app.features.home.HomeViewModel.HomeEffect.NavigateToSeeMore
import br.com.amanfron.ecommerce_app.features.home.HomeViewModel.HomeEffect.ShowErrorToast
import br.com.amanfron.ecommerce_app.features.home.HomeViewModel.HomeIntent.LoadProducts
import br.com.amanfron.ecommerce_app.features.home.HomeViewModel.HomeIntent.OnProductClick
import br.com.amanfron.ecommerce_app.features.home.HomeViewModel.HomeIntent.OnSeeMoreClick
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRankedProductsUseCase: GetRankedProductsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeViewState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<HomeEffect>()
    val effect = _effect.asSharedFlow()

    init {
        onIntent(LoadProducts)
    }

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            is LoadProducts -> fetchProducts()
            is OnProductClick -> emitEffect(NavigateToProductDetail(intent.productId))
            is OnSeeMoreClick -> emitEffect(NavigateToSeeMore(intent.categoryName))
        }
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            getRankedProductsUseCase()
                .onStart { shouldShowLoading(true) }
                .onCompletion { shouldShowLoading(false) }
                .catch { emitEffect(ShowErrorToast) }
                .collect(::onGetRankedProductsSuccess)
        }
    }

    private fun emitEffect(effect: HomeEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }

    private fun onGetRankedProductsSuccess(response: ProductsResponse) {
        _state.update {
            it.copy(
                bannerProductList = response.bannerProductList,
                rankedProductList = response.rankedProductList
            )
        }
    }

    private fun shouldShowLoading(should: Boolean) {
        _state.update {
            it.copy(shouldShowLoading = should)
        }
    }

    data class HomeViewState(
        val shouldShowLoading: Boolean = false,
        val bannerProductList: List<ProductResponse> = emptyList(),
        val rankedProductList: List<ProductCategoryResponse> = emptyList()
    )

    sealed interface HomeIntent {
        object LoadProducts : HomeIntent
        data class OnProductClick(val productId: Int) : HomeIntent
        data class OnSeeMoreClick(val categoryName: String) : HomeIntent
    }

    sealed interface HomeEffect {
        object ShowErrorToast : HomeEffect
        data class NavigateToProductDetail(val productId: Int) : HomeEffect
        data class NavigateToSeeMore(val categoryName: String) : HomeEffect
    }
}
