package hu.bme.aut.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import hu.bme.aut.myapplication.adapter.RecipeAdapter
import hu.bme.aut.myapplication.data.RecipeItem
import hu.bme.aut.myapplication.data.RecipeListDatabase
import hu.bme.aut.myapplication.fragments.NewRecipeItemDialogFragment
import hu.bme.aut.myapplication.ui.list.ListFragment
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), NewRecipeItemDialogFragment.NewRecipeItemDialogListener{

    val TAG = "Main"
    override fun onCreate(savedInstanceState: Bundle?) {
        Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show()
        Thread.sleep(2000)
        setTheme(R.style.Theme_MyApplication)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_recipeList, R.id.navigation_newRecipe, R.id.navigation_shoppingList))
       // setupActionBarWithNavController(navController, appBarConfiguration)


        navView.setupWithNavController(navController)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.navigation_shoppingList ->{
                val settingsActivity = Intent(this, ListActivity::class.java)
                startActivity(settingsActivity)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onRecipeItemCreated(newItem: RecipeItem) {

    }


}