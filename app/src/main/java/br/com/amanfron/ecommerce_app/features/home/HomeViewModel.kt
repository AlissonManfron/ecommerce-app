package br.com.amanfron.ecommerce_app.features.home

import androidx.lifecycle.viewModelScope
import br.com.amanfron.ecommerce_app.core.architecture.BaseViewModel
import br.com.amanfron.ecommerce_app.core.architecture.UiState
import br.com.amanfron.ecommerce_app.core.domain.usecase.GetRankedProductsUseCase
import br.com.amanfron.ecommerce_app.core.model.response.product.ProductCategoryResponse
import br.com.amanfron.ecommerce_app.core.model.response.product.ProductResponse
import br.com.amanfron.ecommerce_app.core.model.response.product.ProductsResponse
import br.com.amanfron.ecommerce_app.features.home.HomeViewModel.HomeEffect
import br.com.amanfron.ecommerce_app.features.home.HomeViewModel.HomeEffect.NavigateToProductDetail
import br.com.amanfron.ecommerce_app.features.home.HomeViewModel.HomeEffect.NavigateToSeeMore
import br.com.amanfron.ecommerce_app.features.home.HomeViewModel.HomeEffect.ShowErrorToast
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
import kotlin.Int

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
                .catch {
                    updateState { UiState.Error() }
                    emitEffect(ShowErrorToast)
                }
                .collect(::onGetRankedProductsSuccess)
        }
    }

    private fun onGetRankedProductsSuccess(response: ProductsResponse) {
        updateState {
            UiState.Success(
                HomeViewState(
                    bannerProductList = response.bannerProductList,
                    rankedProductList = response.rankedProductList
                )
            )
        }
    }

    data class HomeViewState(
        val bannerProductList: List<ProductResponse> = emptyList(),
        val rankedProductList: List<ProductCategoryResponse> = emptyList()
    )

    sealed interface HomeIntent {
        data object LoadProducts : HomeIntent
        data class OnProductClick(val productId: Int) : HomeIntent
        data class OnSeeMoreClick(val categoryId: Int) : HomeIntent
    }

    sealed interface HomeEffect {
        data object ShowErrorToast : HomeEffect
        data class NavigateToProductDetail(val productId: Int) : HomeEffect
        data class NavigateToSeeMore(val categoryId: Int) : HomeEffect
    }
}
