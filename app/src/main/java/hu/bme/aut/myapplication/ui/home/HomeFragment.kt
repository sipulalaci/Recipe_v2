package hu.bme.aut.myapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import hu.bme.aut.myapplication.MainActivity
import hu.bme.aut.myapplication.R
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
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        card_appetizer.setOnClickListener {
            Toast.makeText(context, "Appetizer selected", Toast.LENGTH_SHORT).show()
            mainActivity.selectedType = "0"
            findNavController().navigate(R.id.navigation_recipeList)

        }
        card_soup.setOnClickListener {
            Toast.makeText(context, "Soup selected", Toast.LENGTH_SHORT).show()
            mainActivity.selectedType = "1"
            findNavController().navigate(R.id.navigation_recipeList)
        }
        card_mainCourse.setOnClickListener {
            Toast.makeText(context, "Main course selected", Toast.LENGTH_SHORT).show()
            mainActivity.selectedType = "2"
            findNavController().navigate(R.id.navigation_recipeList)
        }
        card_dessert.setOnClickListener {
            Toast.makeText(context, "Dessert selected", Toast.LENGTH_SHORT).show()
            mainActivity.selectedType = "3"
            findNavController().navigate(R.id.navigation_recipeList)
        }
    }
}