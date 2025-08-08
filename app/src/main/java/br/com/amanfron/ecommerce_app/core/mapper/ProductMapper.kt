package br.com.amanfron.ecommerce_app.core.mapper

import br.com.amanfron.ecommerce_app.core.domain.model.Product
import br.com.amanfron.ecommerce_app.core.model.response.product.ProductResponse

object ProductMapper {
    fun toProduct(product: ProductResponse): Product {
        return Product(
            id = product.id,
            title = product.title,
            description = product.description,
            imageUrl = product.imageUrl,
            price = product.price,
            categoryId = product.categoryId,
            categoryName = product.categoryName
        )
    }
}