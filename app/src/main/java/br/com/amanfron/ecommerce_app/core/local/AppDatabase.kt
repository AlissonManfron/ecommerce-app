package br.com.amanfron.ecommerce_app.core.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ProductItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shoppingCartDao(): ShoppingCartDao
}