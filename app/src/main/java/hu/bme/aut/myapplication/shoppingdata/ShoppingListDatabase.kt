package hu.bme.aut.myapplication.shoppingdata

import androidx.room.*

@Database(entities = [ShoppingItem::class], version = 1)
abstract class ShoppingListDatabase : RoomDatabase() {
    abstract fun shoppingItemDao(): ShoppingItemDao
}