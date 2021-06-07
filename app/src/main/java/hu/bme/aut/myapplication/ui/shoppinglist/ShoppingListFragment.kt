package hu.bme.aut.myapplication.ui.shoppinglist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import hu.bme.aut.myapplication.MainActivity
import hu.bme.aut.myapplication.R
import hu.bme.aut.myapplication.shoppingdata.ShoppingAdapter
import hu.bme.aut.myapplication.shoppingdata.ShoppingItem
import hu.bme.aut.myapplication.shoppingdata.ShoppingListDatabase
import kotlinx.android.synthetic.main.shopping_recyclerview.*
import kotlin.concurrent.thread

class ShoppingListFragment : Fragment(), ShoppingAdapter.ShoppingItemClickListener {

    private lateinit var shoppingListViewModel: ShoppingListViewModel
    private lateinit var mainActivity: MainActivity
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        shoppingListViewModel =
                ViewModelProvider(this).get(ShoppingListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_shoppinglist, container, false)

        mainActivity = activity as MainActivity

        return root
    }

    override fun onItemChanged(item: ShoppingItem) {
        thread {
            mainActivity.shoppingdatabase.shoppingItemDao().update(item)
            Log.d("ShoppingListFragment", "ShoppingItem update was successful")
        }
    }

    override fun onBoughtClicked(item: ShoppingItem) {
        thread {
            mainActivity.shoppingdatabase.shoppingItemDao().update(item)
            Log.d("ShoppingListFragment", "ShoppingItem update was successful")
        }    }

    override fun onItemRemoved(item: ShoppingItem, position: Int) {
        thread {
            mainActivity.shoppingdatabase.shoppingItemDao().deleteItem(item)
            mainActivity.runOnUiThread {
                mainActivity.shoppingadapter.removeItem(item, position)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        var values = arguments?.getString("values")
        var temp = values?.split("\n")?.toTypedArray()?.toMutableList()
        Log.d("values", "$values")
        if (temp != null) {
            for(actual in temp){
                mainActivity.insertNewShoppingItem(ShoppingItem(null, actual, false))
            }
        }
        loadItemsInBackground()
    }

    private fun initRecyclerView() {
        recyclerView = MainRecyclerView
        mainActivity.shoppingadapter = ShoppingAdapter(this)
        loadItemsInBackground()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = mainActivity.shoppingadapter
    }

    private fun loadItemsInBackground() {
        mainActivity.loadShoppingItems()
    }

}