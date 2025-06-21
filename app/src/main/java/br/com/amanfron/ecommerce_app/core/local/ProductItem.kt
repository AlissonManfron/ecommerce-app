package br.com.amanfron.ecommerce_app.core.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.amanfron.ecommerce_app.core.model.response.product.Product

@Entity(tableName = "products")
data class ProductItem(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "price") val price: String,
    @ColumnInfo(name = "category_id") val categoryId: Int,
    @ColumnInfo(name = "category_name") val categoryName: String
)

fun ProductItem.toProduct() = Product(
    id = id,
    title = title,
    description = description,
    imageUrl = imageUrl,
    price = price,
    categoryId = categoryId,
    categoryName = categoryName
)

fun Product.toProductItem() = ProductItem(
    id = id,
    title = title,
    description = description,
    imageUrl = imageUrl,
    price = price,
    categoryId = categoryId,
    categoryName = categoryName
)