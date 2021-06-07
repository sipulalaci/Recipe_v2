package hu.bme.aut.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import hu.bme.aut.myapplication.adapter.RecipeAdapter
import hu.bme.aut.myapplication.data.RecipeItem
import hu.bme.aut.myapplication.data.RecipeListDatabase
import hu.bme.aut.myapplication.fragments.NewRecipeItemDialogFragment
import hu.bme.aut.myapplication.shoppingdata.ShoppingAdapter
import hu.bme.aut.myapplication.shoppingdata.ShoppingItem
import hu.bme.aut.myapplication.shoppingdata.ShoppingListDatabase
import hu.bme.aut.myapplication.ui.list.ListFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), NewRecipeItemDialogFragment.NewRecipeItemDialogListener {

    lateinit var shoppingdatabase: ShoppingListDatabase
    lateinit var shoppingadapter: ShoppingAdapter
    lateinit var database: RecipeListDatabase
    lateinit var adapter: RecipeAdapter
    val TAG = "Main"

    var selectedItems = mutableListOf<RecipeItem>()
    var selectedType = "ALL"
    override fun onCreate(savedInstanceState: Bundle?) {
        Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show()
        Thread.sleep(2000)
        setTheme(R.style.Theme_MyApplication)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)

        //creating the database of the recipes
        database = Room.databaseBuilder(
            this,
            RecipeListDatabase::class.java,
            "recipe-list"
        ).build()

        //Creating the database of the shoppinglist items
        shoppingdatabase = Room.databaseBuilder(
            this,
            ShoppingListDatabase::class.java,
            "shopping-list"
        ).build()

    }

    override fun onRecipeItemCreated(newItem: RecipeItem) {
        thread {
            val newId = database.recipeItemDao().insert(newItem)
            val newRecipeItem = newItem.copy(
                id = newId
            )
            runOnUiThread {
                adapter.addItem(newRecipeItem)
            }
        }
    }

    fun loadShoppingItems(){
        thread {
            val items = shoppingdatabase.shoppingItemDao().getAll()
            runOnUiThread {
                shoppingadapter.update(items)
            }
        }
    }

    fun loadSelected() {
        if (selectedType == "ALL") {
            thread {
                val items = database.recipeItemDao().getAll()
                runOnUiThread {
                    adapter.update(items)
                }
            }
        } else if(selectedType == "FAVOURITES"){
            thread {
                selectedItems = database.recipeItemDao().getFavourites().toMutableList()
                runOnUiThread {
                    adapter.update(selectedItems)
                }
            }
        }
        else{
            thread {
                selectedItems = database.recipeItemDao().getCategory(selectedType).toMutableList()
                runOnUiThread {
                    adapter.update(selectedItems)
                }
            }
        }
    }
    fun loadFavourites(itemView: View){
        selectedType = "FAVOURITES"
        Navigation.findNavController(itemView).navigate(R.id.navigation_recipeList)
    }

    fun update(item: RecipeItem) {
        thread {
            database.recipeItemDao().update(item)
            Log.d("Recyclerview fragment", "RecipeItem update was successful")
        }
    }

    fun insertNewItem(newItem: RecipeItem) {
        thread {
            val newId = database.recipeItemDao().insert(newItem)
            val newRecipeItem = newItem.copy(
                id = newId
            )
            runOnUiThread {
                adapter.addItem(newRecipeItem)
            }
        }
        adapter.notifyDataSetChanged()
    }
    fun insertNewShoppingItem(newItem: ShoppingItem){
        thread {
            val newId = shoppingdatabase.shoppingItemDao().insert(newItem)
            val newShoppingItem = newItem.copy(
                id = newId
            )
            runOnUiThread {
                shoppingadapter.addItem(newShoppingItem)
            }
        }
        shoppingadapter.notifyDataSetChanged()
    }
}