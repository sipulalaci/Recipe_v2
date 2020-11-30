package hu.bme.aut.myapplication.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import hu.bme.aut.myapplication.R
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        card_appetizer.setOnClickListener {
            Toast.makeText(context, "Appetizer clicked", Toast.LENGTH_SHORT).show()
        }
        card_soup.setOnClickListener {
            Toast.makeText(context, "Soup clicked", Toast.LENGTH_SHORT).show()
        }
        card_mainCourse.setOnClickListener {
            Toast.makeText(context, "Main course clicked", Toast.LENGTH_SHORT).show()
        }
        card_dessert.setOnClickListener {
            Toast.makeText(context, "Dessert clicked", Toast.LENGTH_SHORT).show()
        }

    }
}