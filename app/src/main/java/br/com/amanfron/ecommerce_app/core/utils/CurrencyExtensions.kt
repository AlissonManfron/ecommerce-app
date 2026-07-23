package br.com.amanfron.ecommerce_app.core.utils

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

fun BigDecimal.toCurrency(locale: Locale = Locale("pt", "BR")): String {
    val currencyFormatter = NumberFormat.getCurrencyInstance(locale)
    return currencyFormatter.format(this)
}
