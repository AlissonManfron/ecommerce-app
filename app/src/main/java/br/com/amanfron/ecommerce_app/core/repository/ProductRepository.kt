package br.com.amanfron.ecommerce_app.core.repository

import br.com.amanfron.ecommerce_app.core.model.AppService
import br.com.amanfron.ecommerce_app.core.model.response.product.ProductResponse
import br.com.amanfron.ecommerce_app.core.network.ResponseHandler
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val service: AppService,
    private val responseHandler: ResponseHandler
) {
    fun getRankedProducts(): Flow<ProductResponse> {
        return responseHandler.handleResponseFlow {
            service.getRankedProducts()
        }
    }
}