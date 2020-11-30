package hu.bme.aut.myapplication.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import hu.bme.aut.myapplication.R

class ShoppingListFragment : Fragment() {

    private lateinit var shoppingListViewModel: ShoppingListViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        shoppingListViewModel =
                ViewModelProvider(this).get(ShoppingListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_shoppinglist, container, false)

        return root
    }
}