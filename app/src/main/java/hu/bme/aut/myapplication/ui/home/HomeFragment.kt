package hu.bme.aut.myapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import hu.bme.aut.myapplication.MainActivity
import hu.bme.aut.myapplication.R
import hu.bme.aut.myapplication.adapter.RecipeAdapter
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        mainActivity = activity as MainActivity
//        mainActivity.adapter = RecipeAdapter(this)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        card_appetizer.setOnClickListener {
            Toast.makeText(context, "Appetizer clicked", Toast.LENGTH_SHORT).show()
            //mainActivity.loadSelected("APPETIZER")
            mainActivity.selectedType = "0"
            //mainActivity.findNavController().navigate(R.id.navigation_recipeList)
//            var nextFragment = ListFragment()
//            mainActivity.supportFragmentManager.beginTransaction()
//                    .replace(R.id.navigation_recipeList, nextFragment, "findThisFragment")
//                    .addToBackStack(null)
//                    .commit()
            Navigation.createNavigateOnClickListener(R.id.navigation_recipeList)

        }
        card_soup.setOnClickListener {
            Toast.makeText(context, "Soup clicked", Toast.LENGTH_SHORT).show()
            //mainActivity.loadSelected("SOUP")
            mainActivity.selectedType = "1"
        }
        card_mainCourse.setOnClickListener {
            Toast.makeText(context, "Main course clicked", Toast.LENGTH_SHORT).show()
            //mainActivity.loadSelected("MAINCOURSE")
            mainActivity.selectedType = "2"
        }
        card_dessert.setOnClickListener {
            Toast.makeText(context, "Dessert clicked", Toast.LENGTH_SHORT).show()
            //mainActivity.loadSelected("DESSERT")
            mainActivity.selectedType = "3"
        }

    }
}