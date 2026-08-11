package br.com.amanfron.ecommerce_app.core.mapper

import br.com.amanfron.ecommerce_app.core.domain.model.Product
import br.com.amanfron.ecommerce_app.core.domain.model.ProductCategory
import br.com.amanfron.ecommerce_app.core.domain.model.RankedProducts
import br.com.amanfron.ecommerce_app.core.model.response.product.ProductCategoryResponse
import br.com.amanfron.ecommerce_app.core.model.response.product.ProductResponse
import br.com.amanfron.ecommerce_app.core.model.response.product.ProductsResponse

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

    fun toProductCategory(categoryResponse: ProductCategoryResponse): ProductCategory {
        return ProductCategory(
            categoryId = categoryResponse.categoryId ?: categoryResponse.products.firstOrNull()?.categoryId ?: 0,
            categoryName = categoryResponse.categoryName,
            products = categoryResponse.products.map { toProduct(it) }
        )
    }

    fun toRankedProducts(productsResponse: ProductsResponse): RankedProducts {
        return RankedProducts(
            bannerProducts = productsResponse.bannerProductList.map { toProduct(it) },
            rankedProducts = productsResponse.rankedProductList.map { toProductCategory(it) }
        )
    }
}
