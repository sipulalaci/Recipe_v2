package hu.bme.aut.myapplication.ui.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import hu.bme.aut.myapplication.MainActivity
import hu.bme.aut.myapplication.R
import hu.bme.aut.myapplication.adapter.RecipeAdapter
import hu.bme.aut.myapplication.data.RecipeItem
import hu.bme.aut.myapplication.data.RecipeListDatabase
import hu.bme.aut.myapplication.fragments.NewRecipeItemDialogFragment
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.item_list.*
import kotlinx.android.synthetic.main.recipe_recyclerview.*
import kotlin.concurrent.thread



class ListFragment : Fragment(), RecipeAdapter.RecipeItemClickListener{

    private lateinit var listViewModel: ListViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var mainActivity: MainActivity

    //private val args by navArgs<ListFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listViewModel =
                ViewModelProvider(this).get(ListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_list, container, false)

        mainActivity = activity as MainActivity

        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()


//        insertRecipeItem(RecipeItem(null, "leves","valami leírás1", RecipeItem.Category.SOUP,  3, 100,false))
//        insertRecipeItem(RecipeItem(null, "süti","valami leírás2", RecipeItem.Category.DESSERT,  2, 70,false))
//        //insertRecipeItem(RecipeItem(null, "habos almás","valami leírás3", RecipeItem.Category.DESSERT,  3, 100,false))
//        insertRecipeItem(RecipeItem(null, "főzelék","valami leírás4", RecipeItem.Category.MAINCOURSE,  3, 45,false))
//        insertRecipeItem(RecipeItem(null, "nyami","valami leírás5", RecipeItem.Category.APPETIZER,  1, 30,false))

        fab.setOnClickListener{
            NewRecipeItemDialogFragment().show(
                childFragmentManager,
                NewRecipeItemDialogFragment.TAG
            )
            loadItemsInBackground()
        }

    }

    private fun initRecyclerView() {
        recyclerView = MainRecyclerView
        mainActivity.adapter = RecipeAdapter(this)
        loadItemsInBackground()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = mainActivity.adapter

    }

    override fun onResume(){
        super.onResume()
        mainActivity.adapter.notifyDataSetChanged()
        loadItemsInBackground()
    }

   private fun loadItemsInBackground() {
        mainActivity.loadSelected(mainActivity.selectedType)

    }

    override fun onItemChanged(item: RecipeItem) {
        mainActivity.update(item)
        loadItemsInBackground()
    }

     fun insertRecipeItem(newItem: RecipeItem) {
        mainActivity.insertNewItem(newItem)
    }

//    override fun onRecipeItemCreated(newItem: RecipeItem) {
//        thread {
//            val newId = database.recipeItemDao().insert(newItem)
//            val newRecipeItem = newItem.copy(
//                id = newId
//            )
//            activity?.runOnUiThread {
//                adapter.addItem(newRecipeItem)
//            }
//        }
//    }

}