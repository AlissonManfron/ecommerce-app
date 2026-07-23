package br.com.amanfron.ecommerce_app.core.domain.usecase

import br.com.amanfron.ecommerce_app.core.repository.ProductRepository
import javax.inject.Inject

class GetRankedProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke() = repository.getRankedProducts()
}
