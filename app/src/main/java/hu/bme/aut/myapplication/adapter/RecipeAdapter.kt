package hu.bme.aut.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.myapplication.R
import hu.bme.aut.myapplication.data.RecipeItem
import hu.bme.aut.myapplication.ui.list.ListFragment

class RecipeAdapter(private val listener: ListFragment) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    private val items = mutableListOf<RecipeItem>()
    var selectedItemId = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_list, parent, false)
        return RecipeViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val item = items[position]

        holder.nameTextView.text = item.name
        holder.categoryTextView.text = item.category.toString()
        holder.priceTextView.text = item.estimatedPrice.toString()
        holder.totalCookingTime.text = item.cookingTime.toString()

        holder.item = item
        //System.out.println(item?.name)

    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface RecipeItemClickListener {
        fun onItemChanged(item: RecipeItem)
        abstract fun onRecipeClicked(item: RecipeItem)
    }

    fun getItemPosition(item: RecipeItem): Int {
        return items.indexOf(item)
    }

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val iconImageView: ImageView
        val nameTextView: TextView
        val categoryTextView: TextView
        val priceTextView: TextView
        val totalCookingTime: TextView
        val isFavourite: ToggleButton

        var item: RecipeItem? = null

        init {
            iconImageView = itemView.findViewById(R.id.recipe_image)
            nameTextView = itemView.findViewById(R.id.RecipeItemNameTextView)
            categoryTextView = itemView.findViewById(R.id.RecipeItemCategoryTextView)
            priceTextView = itemView.findViewById(R.id.RecipeItemPriceTextView)
            totalCookingTime = itemView.findViewById(R.id.RecipeItemCookingTimeTextView)
            isFavourite = itemView.findViewById(R.id.button_favourite)

            var bundle = bundleOf(
                "name" to "item?.name.toString()",
//                "category" to item?.category.toString(),
//                "price" to item?.estimatedPrice,
//                "cookingTime" to item?.cookingTime,
//                "isFavourite" to item?.isFavourite
            )
            item?.let {
                val newItem = it.copy()
                item = newItem
            }

            itemView.setOnClickListener(

                //System.out.println(item?.name)
                //Navigation.createNavigateOnClickListener(R.id.navigation_detail, bundle)
                Navigation.createNavigateOnClickListener(R.id.action_navigation_recipeList_to_navigation_detail)

            )
        }
    }

    fun addItem(item: RecipeItem) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun update(recipeItems: List<RecipeItem>) {
        items.clear()
        items.addAll(recipeItems)
        notifyDataSetChanged()
    }
}
