package br.com.amanfron.ecommerce_app.core.repository

import br.com.amanfron.ecommerce_app.core.local.ProductItem
import br.com.amanfron.ecommerce_app.core.local.ShoppingCartDao
import javax.inject.Inject

class ShoppingCartRepository @Inject constructor(
    private val shoppingCartDao: ShoppingCartDao
) {
    fun getProductItems() = shoppingCartDao.getAll()

    suspend fun insertProductItem(productItem: ProductItem) = shoppingCartDao.insert(productItem)

    suspend fun deleteProductItem(productItem: ProductItem) = shoppingCartDao.delete(productItem)
}