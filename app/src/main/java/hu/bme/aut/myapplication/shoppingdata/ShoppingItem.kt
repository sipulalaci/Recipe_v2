package hu.bme.aut.myapplication.shoppingdata

import androidx.room.*


@Entity(tableName = "shoppingitem")
data class ShoppingItem(
        @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Long?,
        @ColumnInfo(name = "value") val value: String,
        @ColumnInfo(name = "is_bought") val isBought: Boolean
)