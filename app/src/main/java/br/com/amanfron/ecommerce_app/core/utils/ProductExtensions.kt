package br.com.amanfron.ecommerce_app.core.utils

import br.com.amanfron.ecommerce_app.core.domain.model.Product
import java.math.BigDecimal

fun List<Product>.calculateTotal(): BigDecimal {
    return this.fold(BigDecimal.ZERO) { acc, product ->
        acc.add(product.totalPrice)
    }
}

val Product.totalPrice: BigDecimal
    get() = priceToBigDecimal().multiply(BigDecimal(quantity))

fun Product.priceToBigDecimal(): BigDecimal {
    return try {
        // Handle cases where the price might use comma as decimal separator
        BigDecimal(price.replace(",", "."))
    } catch (e: Exception) {
        BigDecimal.ZERO
    }
}

fun Product.incrementQuantity(): Product = copy(quantity = quantity + 1)

fun Product.decrementQuantity(): Product = copy(quantity = quantity - 1)

val Product.shouldRemove: Boolean get() = quantity <= 1
