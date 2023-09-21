package br.com.amanfron.ecommerce_app.features.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.amanfron.ecommerce_app.core.model.response.product.ProductResponse
import br.com.amanfron.ecommerce_app.core.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            repository.getProductsRankeds()
                .catch { onGetProductsError(it) }
                //.onStart { shouldShowLoading(true) }
                //.onCompletion { shouldShowLoading(false) }
                .collect(::onGetProductsSuccess)
        }
    }

    private fun onGetProductsSuccess(response: ProductResponse) {
        Log.w("Products::", response.products.toString())
    }

    private fun onGetProductsError(throwable: Throwable) {

    }
}