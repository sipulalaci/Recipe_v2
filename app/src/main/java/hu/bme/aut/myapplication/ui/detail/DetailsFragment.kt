package hu.bme.aut.myapplication.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import hu.bme.aut.myapplication.MainActivity
import hu.bme.aut.myapplication.R
import kotlinx.android.synthetic.main.fragment_details.*
import java.lang.StringBuilder

import hu.bme.aut.myapplication.data.RecipeItem as RecipeItem

class DetailsFragment : Fragment() {
    private lateinit var detailsViewModel: DetailsViewModel
    private lateinit var mainActivity: MainActivity
    private lateinit var ingredients: MutableList<String>
    private lateinit var directions: MutableList<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        detailsViewModel =
            ViewModelProvider(this).get(DetailsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_details, container, false)
        mainActivity = activity as MainActivity
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var item = arguments?.getParcelable<RecipeItem>("item")!!

        if(item.pictureURI != ""){
            recipe_image.setImageURI(item.pictureURI.toUri())
        }else
            when (item.category){
                RecipeItem.Category.APPETIZER -> recipe_image.setImageResource(R.drawable.appetizer)
                RecipeItem.Category.SOUP -> recipe_image.setImageResource(R.drawable.soup)
                RecipeItem.Category.MAINCOURSE -> recipe_image.setImageResource(R.drawable.maincourse)
                RecipeItem.Category.DESSERT -> recipe_image.setImageResource(R.drawable.dessert)
            }

        //Setting the title of the food
        RecipeItemNameTextView.text = item.name

        //Setting the category and chaging the enum values to better looking strings
        when(item.category){
            RecipeItem.Category.APPETIZER -> RecipeItemCategoryTextView.text = "Appetizer"
            RecipeItem.Category.SOUP -> RecipeItemCategoryTextView.text = "Soup"
            RecipeItem.Category.MAINCOURSE -> RecipeItemCategoryTextView.text = "Main course"
            RecipeItem.Category.DESSERT -> RecipeItemCategoryTextView.text = "Dessert"
        }

        //Setting the value of the price
        RecipeItemPriceTextView.text = item.price

        //
        RecipeItemCookingTimeTextView.text = item.cookingTime.toString()

        //Splitting the ingredients String into list to display it better
        ingredients = item.ingredients.split("\n").toTypedArray().toMutableList()
        var temp = mutableListOf<String>()
        for (actual in ingredients){
            if(actual != "") temp.add(actual)
        }
        ingredients.clear()
        ingredients = temp
        var ingredientsBuilder = StringBuilder()
        for(actual in ingredients){
            ingredientsBuilder.append("- ")
            ingredientsBuilder.append(actual)
            if(actual != ingredients.last()) ingredientsBuilder.append("\n\n")
        }
        ingredientTextView.text = ingredientsBuilder.toString()

        //Splitting the directions String into list to display it better
        directions = item.directions.split("\n").toTypedArray().toMutableList()
        temp.clear()
        for (actual in directions){
            if(actual != "") temp.add(actual)
        }
        directions.clear()
        directions = temp
        var directionsBuilder = StringBuilder()
        for(actual in directions){
            directionsBuilder.append("${directions.indexOf(actual) + 1}.\n")
            directionsBuilder.append(actual)
            if(actual != ingredients.last()) directionsBuilder.append("\n\n")
        }
        directionsTextView.text = directionsBuilder.toString()
    }


}