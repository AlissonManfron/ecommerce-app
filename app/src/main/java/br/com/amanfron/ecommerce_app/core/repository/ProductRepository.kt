package br.com.amanfron.ecommerce_app.core.repository

import br.com.amanfron.ecommerce_app.core.domain.model.Product
import br.com.amanfron.ecommerce_app.core.domain.model.RankedProducts
import br.com.amanfron.ecommerce_app.core.mapper.ProductMapper
import br.com.amanfron.ecommerce_app.core.model.AppService
import br.com.amanfron.ecommerce_app.core.network.ResponseHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val service: AppService,
    private val responseHandler: ResponseHandler
) {
    fun getRankedProducts(): Flow<RankedProducts> {
        return responseHandler.handleResponseFlow {
            service.getRankedProducts()
        }.map { productsResponse ->
            ProductMapper.toRankedProducts(productsResponse)
        }
    }

    fun getProductDetail(productId: Int): Flow<Product> {
        return responseHandler.handleResponseFlow {
            service.getProductDetail(productId)
        }.map { productResponse ->
            ProductMapper.toProduct(product = productResponse)
        }
    }

    fun getProductsByCategory(categoryId: Int): Flow<List<Product>> {
        return responseHandler.handleResponseFlow {
            service.getProductsByCategory(categoryId)
        }.map { productResponseList ->
            productResponseList.map { productResponse ->
                ProductMapper.toProduct(productResponse)
            }
        }
    }
}
