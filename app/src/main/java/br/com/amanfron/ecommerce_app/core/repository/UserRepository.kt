package br.com.amanfron.ecommerce_app.core.repository

import javax.inject.Inject

class UserRepository @Inject constructor() {
    var name: String = ""
    var email: String = ""
    var token: String = ""

    fun setUser(name: String, email: String, token: String) {
        this.name = name
        this.email = email
        this.token = token
    }
}