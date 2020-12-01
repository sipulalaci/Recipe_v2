package hu.bme.aut.myapplication.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.room.Room
import com.google.android.material.internal.ViewUtils.getContentView
import hu.bme.aut.myapplication.R
import hu.bme.aut.myapplication.adapter.RecipeAdapter
import hu.bme.aut.myapplication.data.RecipeItem
import hu.bme.aut.myapplication.data.RecipeListDatabase
import hu.bme.aut.myapplication.ui.list.ListFragment
import kotlinx.android.synthetic.main.item_list.*
import kotlinx.coroutines.NonCancellable.cancel
import kotlin.concurrent.thread

class NewRecipeItemDialogFragment : DialogFragment() {
    interface NewRecipeItemDialogListener {
        fun onRecipeItemCreated(newItem: RecipeItem)
    }

    private lateinit var listener: NewRecipeItemDialogListener
    private lateinit var nameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var estimatedPriceEditText: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var isFavouriteCheckBox: CheckBox

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
        descriptionEditText = contentView.findViewById(R.id.RecipeItemDescriptionEditText)
        estimatedPriceEditText = contentView.findViewById(R.id.RecipeItemEstimatedPriceEditText)
        categorySpinner = contentView.findViewById(R.id.RecipeItemCategorySpinner)
        categorySpinner.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.category_items)
            )
        )
        isFavouriteCheckBox = contentView.findViewById(R.id.RecipeItemIsPurchasedCheckBox)
        return contentView
    }

    private fun isValid() = nameEditText.text.isNotEmpty()

    private fun getRecipeItem() = RecipeItem(
        id = null,
        name = nameEditText.text.toString(),
        description = descriptionEditText.text.toString(),
        estimatedPrice = try {
            estimatedPriceEditText.text.toString().toInt()
        } catch (e: java.lang.NumberFormatException) {
            0
        },
        category = RecipeItem.Category.getByOrdinal(categorySpinner.selectedItemPosition)
            ?: RecipeItem.Category.APPETIZER,
        cookingTime = null,
        isFavourite = isFavouriteCheckBox.isChecked
    )
}