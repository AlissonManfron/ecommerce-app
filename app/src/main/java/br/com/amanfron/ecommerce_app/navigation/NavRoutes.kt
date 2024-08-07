package br.com.amanfron.ecommerce_app.navigation

object NavRoutes {
    const val PRODUCT_ID_PARAM = "productId"

    const val LOGIN = "login"
    const val HOME = "home"
    const val CREATE_ACCOUNT = "create_account"
    const val PRODUCT_DETAIL = "product_detail/{$PRODUCT_ID_PARAM}"
    const val SHOPPING_CART = "shopping_cart"
}