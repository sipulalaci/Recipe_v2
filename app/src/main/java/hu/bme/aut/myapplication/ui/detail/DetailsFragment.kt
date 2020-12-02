package hu.bme.aut.myapplication.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import hu.bme.aut.myapplication.MainActivity
import hu.bme.aut.myapplication.R

import androidx.core.os.bundleOf
import hu.bme.aut.myapplication.data.RecipeItem
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment() {

    private lateinit var detailsViewModel: DetailsViewModel
    private lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Toast.makeText(context, arguments?.getString("name"), Toast.LENGTH_SHORT).show()
        //RecipeItemNameTextView.text = arguments?.getString("name")
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


}