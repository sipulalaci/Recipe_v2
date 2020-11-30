package hu.bme.aut.myapplication.ui.list

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
import hu.bme.aut.myapplication.adapter.RecipeAdapter
import hu.bme.aut.myapplication.data.RecipeItem
import hu.bme.aut.myapplication.data.RecipeListDatabase
import hu.bme.aut.myapplication.fragments.NewRecipeItemDialogFragment
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.recipe_recyclerview.*
import kotlin.concurrent.thread



class ListFragment : Fragment(), RecipeAdapter.RecipeItemClickListener, NewRecipeItemDialogFragment.NewRecipeItemDialogListener {

    private lateinit var listViewModel: ListViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecipeAdapter
    private lateinit var database: RecipeListDatabase
    private lateinit var mainActivity: MainActivity


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listViewModel =
                ViewModelProvider(this).get(ListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_list, container, false)

        mainActivity = activity as MainActivity

        //TODO: mainactivitybe való
        database = Room.databaseBuilder(
            requireContext(),
            RecipeListDatabase::class.java,
            "recipe-list"
        ).build()

        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

//        insertRecipeItem(RecipeItem(null, "leves","valami leírás1", RecipeItem.Category.SOUP,  3, 100,false))
//        insertRecipeItem(RecipeItem(null, "süti","valami leírás2", RecipeItem.Category.DESSERT,  2, 70,false))
//        insertRecipeItem(RecipeItem(null, "habos almás","valami leírás3", RecipeItem.Category.DESSERT,  3, 100,false))
//        insertRecipeItem(RecipeItem(null, "főzelék","valami leírás4", RecipeItem.Category.MAINCOURSE,  3, 45,false))
//        insertRecipeItem(RecipeItem(null, "palacsinta","valami leírás5", RecipeItem.Category.DESSERT,  1, 30,false))
//        addRecipe.setOnClickListener {
//            insertRecipeItem(RecipeItem(1, "leves","valami leírás1", RecipeItem.Category.SOUP,  3))
//        }
//        addRecipe.setOnClickListener {
//            insertRecipeItem(RecipeItem(2, "süti","valami leírás2", RecipeItem.Category.DESSERT,  2))
//        }
//        addRecipe.setOnClickListener {
//            insertRecipeItem(RecipeItem(3, "habos almás","valami leírás3", RecipeItem.Category.DESSERT,  3))
//        }
//        addRecipe.setOnClickListener {
//            insertRecipeItem(RecipeItem(4, "főzelék","valami leírás4", RecipeItem.Category.MAINCOURSE,  3))
//        }
//        addRecipe.setOnClickListener {
//            insertRecipeItem(RecipeItem(1, "palacsinta","valami leírás5", RecipeItem.Category.DESSERT,  1))
//        }

        fab.setOnClickListener{
            NewRecipeItemDialogFragment().show(
                childFragmentManager,
                NewRecipeItemDialogFragment.TAG
            )
//            database = Room.databaseBuilder(
//                requireContext(),
//                RecipeListDatabase::class.java,
//                "recipe-list"
//            ).build()

            //val dialog: NewRecipeItemDialogFragment = NewRecipeItemDialogFragment()..instantiate(requireActivity(), "NewRecipeItemDialogFragment") as NewRecipeItemDialogFragment


        }

    }

    private fun initRecyclerView() {
        recyclerView = MainRecyclerView
        adapter = RecipeAdapter(this)
        loadItemsInBackground()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }


    private fun loadItemsInBackground() {
        //TODO: mainActivity.loadAll() getAllItems() akármi
        thread {
            val items = database.recipeItemDao().getAll()
//            mainActivity.getAllItems()
            activity?.runOnUiThread {
                adapter.update(items)
            }
        }
    }

    //TODO: mainActivity.update(item)
    override fun onItemChanged(item: RecipeItem) {
        thread {
            database.recipeItemDao().update(item)
            Log.d("Recyclerview fragment", "RecipeItem update was successful")
        }
    }

    fun addItemToList(item: RecipeItem){

    }
    //TODO: mainActivity.newItem(item)
     fun insertRecipeItem(newItem: RecipeItem) {
        thread {
            val newId = database.recipeItemDao().insert(newItem)
            val newRecipeItem = newItem.copy(
                id = newId
            )
            activity?.runOnUiThread {//nem is kell
                adapter.addItem(newRecipeItem)
            }
        }
    }

    override fun onRecipeItemCreated(newItem: RecipeItem) {
        thread {
            val newId = database.recipeItemDao().insert(newItem)
            val newRecipeItem = newItem.copy(
                id = newId
            )
            activity?.runOnUiThread {
                adapter.addItem(newRecipeItem)
            }
        }
    }

}