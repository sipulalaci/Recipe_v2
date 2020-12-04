package hu.bme.aut.myapplication.data

import androidx.room.*

@Dao
interface RecipeItemDao {
    @Query("SELECT * FROM recipeitem")
    fun getAll(): List<RecipeItem>

    @Insert
    fun insert(recipeItems: RecipeItem): Long

    @Update
    fun update(recipeItem: RecipeItem)

    @Delete
    fun deleteItem(recipeItem: RecipeItem)

    @Query("SELECT * FROM recipeitem WHERE category = :type")
    fun getCategory(type: String): List<RecipeItem>

    @Query("SELECT * FROM recipeitem WHERE category = :type")
    fun getFavourites(type: String): List<RecipeItem>
}
