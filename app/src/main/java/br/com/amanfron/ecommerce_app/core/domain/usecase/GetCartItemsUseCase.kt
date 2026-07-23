package br.com.amanfron.ecommerce_app.core.domain.usecase

import br.com.amanfron.ecommerce_app.core.repository.ShoppingCartRepository
import javax.inject.Inject

class GetCartItemsUseCase @Inject constructor(
    private val repository: ShoppingCartRepository
) {
    operator fun invoke() = repository.getProductItems()
}
