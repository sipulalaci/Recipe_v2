package hu.bme.aut.myapplication.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.myapplication.R
import hu.bme.aut.myapplication.data.RecipeItem

class RecipeAdapter internal constructor(private val listener: OnRecipeSelectedListener) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    private val items = mutableListOf<RecipeItem>()

    interface OnRecipeSelectedListener {
        fun onRecipeSelected(item: RecipeItem, itemView: View)
    }

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
        holder.priceTextView.text = item.price.toString()
        holder.totalCookingTime.text = item.cookingTime.toString()

        holder.item = item

    }

    override fun getItemCount(): Int {
        return items.size
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

            itemView.setOnClickListener{
                item?.let {it-> listener.onRecipeSelected(it, itemView) }
            }
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
