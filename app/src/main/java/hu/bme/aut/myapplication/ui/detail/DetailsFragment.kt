package hu.bme.aut.myapplication.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import hu.bme.aut.myapplication.MainActivity
import hu.bme.aut.myapplication.R
import hu.bme.aut.myapplication.adapter.RecipeAdapter
import hu.bme.aut.myapplication.data.RecipeItem
import java.security.KeyStore

class DetailsFragment : Fragment(), RecipeAdapter.RecipeItemClickListener {

    private lateinit var detailsViewModel: DetailsViewModel
    private lateinit var mainActivity: MainActivity
    private lateinit var item: RecipeItem


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        detailsViewModel =
            ViewModelProvider(this).get(DetailsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_list, container, false)

        mainActivity = activity as MainActivity


        return root
    }

    override fun onItemChanged(item: RecipeItem) {
        TODO("Not yet implemented")
    }

    override fun onRecipeClicked(item: RecipeItem) {
        this.item = item
    }
}