package hu.bme.aut.myapplication.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import hu.bme.aut.myapplication.MainActivity
import hu.bme.aut.myapplication.R
import kotlinx.android.synthetic.main.fragment_details.*

import hu.bme.aut.myapplication.data.RecipeItem as RecipeItem

class DetailsFragment : Fragment() {
    private lateinit var detailsViewModel: DetailsViewModel
    private lateinit var mainActivity: MainActivity

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

        ingredientTextView.text = item.ingredients
        directionsTextView.text = item.directions


    }


}