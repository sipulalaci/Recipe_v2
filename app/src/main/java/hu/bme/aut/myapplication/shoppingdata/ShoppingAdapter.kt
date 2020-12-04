package hu.bme.aut.myapplication.shoppingdata

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.myapplication.R

class ShoppingAdapter(private val listener: ShoppingItemClickListener) :
    RecyclerView.Adapter<ShoppingAdapter.ShoppingViewHolder>() {

    private val items = mutableListOf<ShoppingItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
        val itemView: View = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_shoppinglist, parent, false)
        return ShoppingViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
        val item = items[position]
        holder.valueTextView.text = item.value
        holder.isBoughtCheckBox.isChecked = item.isBought

        holder.item = item
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface ShoppingItemClickListener {
        fun onItemChanged(item: ShoppingItem)
        fun onBoughtClicked(item: ShoppingItem)
        fun onItemRemoved(item: ShoppingItem, position: Int)
    }

    fun addItem(item: ShoppingItem) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun update(shoppingItems: List<ShoppingItem>) {
        items.clear()
        items.addAll(shoppingItems)
        notifyDataSetChanged()
    }
    fun removeItem(shoppingItem: ShoppingItem,  position: Int){
        items.remove(shoppingItem)
        notifyItemRemoved(position)
    }

    inner class ShoppingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val valueTextView: TextView
        val isBoughtCheckBox: CheckBox
        val removeButton: ImageButton

        var item: ShoppingItem? = null

        init {

            valueTextView = itemView.findViewById(R.id.ShoppingItemNameTextView)
            isBoughtCheckBox = itemView.findViewById(R.id.ShoppingItemIsBoughtCheckBox)
            removeButton = itemView.findViewById(R.id.ShoppingItemRemoveButton)
            isBoughtCheckBox.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                item?.let {
                    val newItem = it.copy(
                        isBought = isChecked
                    )
                    item = newItem
                    listener.onItemChanged(newItem)
                }
            })
            removeButton.setOnClickListener {
                val removedItem = items.removeAt(adapterPosition)
                listener.onItemRemoved(removedItem, adapterPosition)
            }
        }
    }
}