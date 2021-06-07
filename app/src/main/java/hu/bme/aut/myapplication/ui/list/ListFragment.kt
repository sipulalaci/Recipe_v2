package hu.bme.aut.myapplication.ui.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewpager.widget.ViewPager
import hu.bme.aut.myapplication.MainActivity
import hu.bme.aut.myapplication.R
import hu.bme.aut.myapplication.adapter.RecipeAdapter
import hu.bme.aut.myapplication.data.RecipeItem
import hu.bme.aut.myapplication.fragments.NewRecipeItemDialogFragment
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.recipe_recyclerview.*
import kotlin.concurrent.thread


class ListFragment : Fragment(), RecipeAdapter.OnRecipeSelectedListener {

    private lateinit var listViewModel: ListViewModel
    private lateinit var recyclerView: RecyclerView
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

        System.out.println(arguments)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        //Alap leves hozzáadása
//        mainActivity.insertNewItem(RecipeItem(null,
//            "Leves",
//            "Mossuk meg a húst és rakjuk fel főni annyi vízben, hogy ellepje.\nAdjunk hozzá sót és szemes borsot ízlés szerint.\nTisztítsuk meg és daraboljuk fel a zöldségeket.\nHa már kellően puha a hús, adjuk hozzá a zöldségeket és lassú fokozaton főzzük.\nHa minden megfőtt, frissen tálaljuk.",
//            "hús\n6db sárgarépa\n4db fehérrépa\n1db zeller\n1db karalábé\n2db krumpli\n1 fej hagyma\nsó és bors ízlés szerint",
//            RecipeItem.Category.SOUP, "$$", 120, false, ""))

        fab.setOnClickListener {
            NewRecipeItemDialogFragment().show(
                childFragmentManager,
                NewRecipeItemDialogFragment.TAG
            )
        }
        itemSwipeToRefresh.setOnRefreshListener {
            mainActivity.selectedType = "ALL"
            loadItemsInBackground()
            mainActivity.adapter.notifyDataSetChanged()
            itemSwipeToRefresh.isRefreshing = false
        }
    }

    private fun initRecyclerView() {
        recyclerView = MainRecyclerView
        mainActivity.adapter = RecipeAdapter(this)
        loadItemsInBackground()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = mainActivity.adapter
    }

    override fun onResume() {
        super.onResume()
        mainActivity.adapter.notifyDataSetChanged()
        loadItemsInBackground()
    }

    private fun loadItemsInBackground() {
        if (mainActivity.selectedType == "ALL")
            mainActivity.loadSelected()
        else {
            mainActivity.loadSelected()
        }
    }

    override fun onRecipeSelected(item: RecipeItem, itemView: View) {
        val bundle = bundleOf(
            "item" to item
        )
        Navigation.findNavController(itemView).navigate(R.id.navigation_detail, bundle)
    }

    override fun onIsFavouriteChecked(item: RecipeItem) {
        thread {
            mainActivity.database.recipeItemDao().update(item)
            Log.d("ListFragment", "RecipeItem update was successful")
        }
    }

    override fun onIsFavouriteLongClicked(itemView: View) {
        mainActivity.loadFavourites(itemView)
    }
}