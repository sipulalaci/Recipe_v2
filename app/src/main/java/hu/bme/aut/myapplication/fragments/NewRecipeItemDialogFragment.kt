package hu.bme.aut.myapplication.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hu.bme.aut.myapplication.R
import hu.bme.aut.myapplication.data.RecipeItem
import kotlinx.android.synthetic.main.fragment_details.*

class NewRecipeItemDialogFragment : DialogFragment() {
    interface NewRecipeItemDialogListener {
        fun onRecipeItemCreated(newItem: RecipeItem)
    }

    private lateinit var listener: NewRecipeItemDialogListener
    private lateinit var nameEditText: EditText
    private lateinit var directionsEditText: EditText
    private lateinit var ingredientsEditText: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var priceSpinner: Spinner
    private lateinit var totalTimeEditText: EditText



    override fun onAttach(context: Context) {
        super.onAttach(context)

        listener = context as? NewRecipeItemDialogListener
            ?: throw RuntimeException("Activity must implement the NewRecipeItemDialogListener interface!")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.new_item)
            .setView(getContentView())
            .setPositiveButton(R.string.ok) { dialogInterface, i ->
                if (isValid()) {
                    listener.onRecipeItemCreated(getRecipeItem())
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }

    companion object {
        const val TAG = "NewRecipeItemDialogFragment"
    }


    private fun getContentView(): View {
        val contentView =
            LayoutInflater.from(context).inflate(R.layout.dialog_new_item, null)
        nameEditText = contentView.findViewById(R.id.RecipeItemNameEditText)
        ingredientsEditText = contentView.findViewById(R.id.RecipeItemIngredientsEditText)
        directionsEditText = contentView.findViewById(R.id.RecipeItemDirectionsEditText)
        priceSpinner = contentView.findViewById(R.id.RecipePriceSpinner)
        totalTimeEditText = contentView.findViewById(R.id.RecipeItemTimeEditText)
        categorySpinner = contentView.findViewById(R.id.RecipeItemCategorySpinner)
        categorySpinner.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.category_items)
            )
        )
        priceSpinner = contentView.findViewById(R.id.RecipePriceSpinner)
        priceSpinner.setAdapter(
            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.price_items,
                android.R.layout.simple_spinner_dropdown_item)
        )



        return contentView
    }

    private fun isValid() = nameEditText.text.isNotEmpty()

    private fun getRecipeItem() = RecipeItem(
        id = null,
        //TODO: pictures
        pictureURI = "TODO",
        name = nameEditText.text.toString(),
        ingredients = ingredientsEditText.text.toString(),
        directions = directionsEditText.text.toString(),
        price = priceSpinner.selectedItem.toString(),

        category = RecipeItem.Category.getByOrdinal(categorySpinner.selectedItemPosition)
            ?: RecipeItem.Category.APPETIZER,

        cookingTime = try {
            totalTimeEditText.text.toString().toInt()
        } catch (e: java.lang.NumberFormatException) {
            0
        },
        isFavourite = false
    )
}

