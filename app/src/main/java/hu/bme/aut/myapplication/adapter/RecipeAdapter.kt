package hu.bme.aut.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.graphics.drawable.toDrawable
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hu.bme.aut.myapplication.MainActivity
import hu.bme.aut.myapplication.R
import hu.bme.aut.myapplication.data.RecipeItem
import kotlinx.android.synthetic.main.fragment_details.*
import kotlin.coroutines.coroutineContext

class RecipeAdapter internal constructor(private val listener: OnRecipeSelectedListener) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    val items = mutableListOf<RecipeItem>()
    interface OnRecipeSelectedListener {
        fun onRecipeSelected(item: RecipeItem, itemView: View)
        fun onIsFavouriteChecked(item: RecipeItem)
        fun onIsFavouriteLongClicked(itemView: View)
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
        when(item.category){
            RecipeItem.Category.APPETIZER -> holder.categoryTextView.text = "Appetizer"
            RecipeItem.Category.SOUP -> holder.categoryTextView.text = "Soup"
            RecipeItem.Category.MAINCOURSE -> holder.categoryTextView.text = "Main course"
            RecipeItem.Category.DESSERT -> holder.categoryTextView.text = "Dessert"
        }
        holder.priceTextView.text = item.price
        holder.totalCookingTime.text = item.cookingTime.toString()
        
        if (item.pictureURI != "") {
            holder.iconImageView.setImageURI(item.pictureURI.toUri())
        } else{
            when (item.category) {
                RecipeItem.Category.APPETIZER -> holder.iconImageView.setImageResource(R.drawable.appetizer)
                RecipeItem.Category.SOUP -> holder.iconImageView.setImageResource(R.drawable.soup)
                RecipeItem.Category.MAINCOURSE -> holder.iconImageView.setImageResource(R.drawable.maincourse)
                RecipeItem.Category.DESSERT -> holder.iconImageView.setImageResource(R.drawable.dessert)
            }
        }
        holder.isFavourite.isChecked = item.isFavourite
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
                item?.let { it-> listener.onRecipeSelected(it, itemView) }
            }

            isFavourite.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                item?.let {
                    val newItem = it.copy(
                        isFavourite = isChecked
                    )
                    item = newItem
                    listener.onIsFavouriteChecked(newItem)
                }
                System.out.println(item?.name)
            })

            isFavourite.setOnLongClickListener {
                //nameTextView.text = "set"
                item?.let { it -> listener.onIsFavouriteLongClicked(itemView)}
                false
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
