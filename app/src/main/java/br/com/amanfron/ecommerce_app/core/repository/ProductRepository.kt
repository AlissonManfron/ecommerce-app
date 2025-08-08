package br.com.amanfron.ecommerce_app.core.repository

import br.com.amanfron.ecommerce_app.core.domain.model.Product
import br.com.amanfron.ecommerce_app.core.mapper.ProductMapper
import br.com.amanfron.ecommerce_app.core.model.AppService
import br.com.amanfron.ecommerce_app.core.model.response.product.ProductsResponse
import br.com.amanfron.ecommerce_app.core.network.ResponseHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val service: AppService,
    private val responseHandler: ResponseHandler
) {
    fun getRankedProducts(): Flow<ProductsResponse> {
        return responseHandler.handleResponseFlow {
            service.getRankedProducts()
        }
    }

    fun getProductDetail(productId: Int): Flow<Product> {
        return responseHandler.handleResponseFlow {
            service.getProductDetail(productId)
        }.map { productResponse ->
            ProductMapper.toProduct(product = productResponse)
        }
    }
}