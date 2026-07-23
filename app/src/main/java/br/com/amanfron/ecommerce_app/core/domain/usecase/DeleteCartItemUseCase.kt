package br.com.amanfron.ecommerce_app.core.domain.usecase

import br.com.amanfron.ecommerce_app.core.domain.model.Product
import br.com.amanfron.ecommerce_app.core.local.toProductItem
import br.com.amanfron.ecommerce_app.core.repository.ShoppingCartRepository
import javax.inject.Inject

class DeleteCartItemUseCase @Inject constructor(
    private val repository: ShoppingCartRepository
) {
    suspend operator fun invoke(product: Product) {
        repository.deleteProductItem(product.toProductItem())
    }
}
