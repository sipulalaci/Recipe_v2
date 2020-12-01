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

//    @Query("SELECT * FROM recipeitem WHERE category = '0'")
//    fun getAppetizers(): List<RecipeItem>
//
//    @Query("SELECT * FROM recipeitem WHERE category = '1'")
//    fun getSoups(): List<RecipeItem>
//
//    @Query("SELECT * FROM recipeitem WHERE category = '2'")
//    fun getMainCourses(): List<RecipeItem>
//
//    @Query("SELECT * FROM recipeitem WHERE category = '3'")
//        fun getDesserts(): List<RecipeItem>

    @Query("SELECT * FROM recipeitem WHERE category = '0'")
    fun getCategory(): List<RecipeItem>
}
