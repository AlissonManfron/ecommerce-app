package br.com.amanfron.ecommerce_app.features.home

import androidx.lifecycle.viewModelScope
import br.com.amanfron.ecommerce_app.core.architecture.BaseViewModel
import br.com.amanfron.ecommerce_app.core.architecture.UiState
import br.com.amanfron.ecommerce_app.core.domain.model.Product
import br.com.amanfron.ecommerce_app.core.domain.model.ProductCategory
import br.com.amanfron.ecommerce_app.core.domain.model.RankedProducts
import br.com.amanfron.ecommerce_app.core.domain.usecase.GetRankedProductsUseCase
import br.com.amanfron.ecommerce_app.features.home.HomeViewModel.HomeEffect
import br.com.amanfron.ecommerce_app.features.home.HomeViewModel.HomeEffect.NavigateToProductDetail
import br.com.amanfron.ecommerce_app.features.home.HomeViewModel.HomeEffect.NavigateToSeeMore
import br.com.amanfron.ecommerce_app.features.home.HomeViewModel.HomeIntent
import br.com.amanfron.ecommerce_app.features.home.HomeViewModel.HomeIntent.LoadProducts
import br.com.amanfron.ecommerce_app.features.home.HomeViewModel.HomeIntent.OnProductClick
import br.com.amanfron.ecommerce_app.features.home.HomeViewModel.HomeIntent.OnSeeMoreClick
import br.com.amanfron.ecommerce_app.features.home.HomeViewModel.HomeViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRankedProductsUseCase: GetRankedProductsUseCase
) : BaseViewModel<UiState<HomeViewState>, HomeIntent, HomeEffect>(UiState.Loading) {

    init {
        onIntent(LoadProducts)
    }

    override fun onIntent(intent: HomeIntent) {
        when (intent) {
            is LoadProducts -> fetchProducts()
            is OnProductClick -> emitEffect(NavigateToProductDetail(intent.productId))
            is OnSeeMoreClick -> emitEffect(NavigateToSeeMore(intent.categoryId))
        }
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            getRankedProductsUseCase()
                .onStart { updateState { UiState.Loading } }
                .catch { updateState { UiState.Error() } }
                .collect { rankedProducts ->
                    onGetRankedProductsSuccess(rankedProducts)
                }
        }
    }

    private fun onGetRankedProductsSuccess(rankedProducts: RankedProducts) {
        updateState {
            UiState.Success(
                HomeViewState(
                    bannerProductList = rankedProducts.bannerProducts,
                    rankedProductList = rankedProducts.rankedProducts
                )
            )
        }
    }

    data class HomeViewState(
        val bannerProductList: List<Product> = emptyList(),
        val rankedProductList: List<ProductCategory> = emptyList()
    )

    sealed interface HomeIntent {
        data object LoadProducts : HomeIntent
        data class OnProductClick(val productId: Int) : HomeIntent
        data class OnSeeMoreClick(val categoryId: Int) : HomeIntent
    }

    sealed interface HomeEffect {
        data class NavigateToProductDetail(val productId: Int) : HomeEffect
        data class NavigateToSeeMore(val categoryId: Int) : HomeEffect
    }
}
