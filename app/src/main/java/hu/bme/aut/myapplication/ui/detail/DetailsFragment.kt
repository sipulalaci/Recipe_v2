package hu.bme.aut.myapplication.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import hu.bme.aut.myapplication.MainActivity
import hu.bme.aut.myapplication.R
import kotlinx.android.synthetic.main.dialog_new_item.*
import kotlinx.android.synthetic.main.fragment_details.*

import hu.bme.aut.myapplication.data.RecipeItem as RecipeItem

class DetailsFragment : Fragment() {

    private lateinit var detailsViewModel: DetailsViewModel
    private lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Toast.makeText(context, arguments?.getString("name"), Toast.LENGTH_SHORT).show()
        System.out.println("name: " + arguments?.getString("name"))
        System.out.println("category: " + arguments?.getString("category"))
        System.out.println("price: " + arguments?.getInt("price"))
        System.out.println("cookingTime: " + arguments?.getString("cookingTime"))
        System.out.println("isFavourite: " + arguments?.getBoolean("isFavourite"))


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        detailsViewModel =
            ViewModelProvider(this).get(DetailsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_details, container, false)

        mainActivity = activity as MainActivity
        //Toast.makeText(context, arguments?.getString("name"), Toast.LENGTH_SHORT).show()
//        RecipeItemNameTextView.text = ""





        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var item = arguments?.getParcelable<RecipeItem>("item")!!

        RecipeItemNameTextView.text = item.name
        when(item.category){
            RecipeItem.Category.APPETIZER -> RecipeItemCategoryTextView.text = "Appetizer"
            RecipeItem.Category.SOUP -> RecipeItemCategoryTextView.text = "Soup"
            RecipeItem.Category.MAINCOURSE -> RecipeItemCategoryTextView.text = "Main course"
            RecipeItem.Category.DESSERT -> RecipeItemCategoryTextView.text = "Dessert"
        }

        if(item.price == null){
            RecipeItemPriceTextView.text = "$$$"
        }else{
            RecipeItemPriceTextView.text = item.price.toString()
        }

        if(item.cookingTime == null){
            RecipeItemCookingTimeTextView.text = "120"
        }else{
            RecipeItemCookingTimeTextView.text = item.cookingTime.toString()
        }
        ingredientTextView.text = item.ingredients
        directionsTextView.text = item.directions


    }


}