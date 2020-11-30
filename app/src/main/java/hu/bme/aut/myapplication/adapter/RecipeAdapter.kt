package hu.bme.aut.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.myapplication.R
import hu.bme.aut.myapplication.data.RecipeItem
import hu.bme.aut.myapplication.ui.list.ListFragment

class RecipeAdapter(private val listener: ListFragment) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    private val items = mutableListOf<RecipeItem>()
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
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface RecipeItemClickListener {
        fun onItemChanged(item: RecipeItem)
    }

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val iconImageView: ImageView
        val nameTextView: TextView
        val categoryTextView: TextView
        val priceTextView: TextView
        val totalCookingTime: TextView

        var item: RecipeItem? = null

        init {
            iconImageView = itemView.findViewById(R.id.recipe_image)
            nameTextView = itemView.findViewById(R.id.RecipeItemNameTextView)
            categoryTextView = itemView.findViewById(R.id.RecipeItemCategoryTextView)
            priceTextView = itemView.findViewById(R.id.RecipeItemPriceTextView)
            totalCookingTime = itemView.findViewById(R.id.RecipeItemCookingTimeTextView)
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
