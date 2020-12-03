package hu.bme.aut.myapplication.fragments

import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hu.bme.aut.myapplication.R
import hu.bme.aut.myapplication.data.RecipeItem

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
    private lateinit var addPicture: Button
    private var imageUri : Uri? = null
    private val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var imageView : ImageView

    override fun onAttach(context: Context) {
        super.onAttach(context)

        listener = context as? NewRecipeItemDialogListener
            ?: throw RuntimeException("Activity must implement the NewRecipeItemDialogListener interface!")
    }

    override fun onResume() {
        super.onResume()
        val dialog = dialog as AlertDialog

        val positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE)

        positiveButton.setOnClickListener {
            if (isValid()) {
                listener.onRecipeItemCreated(getRecipeItem())
                dialog.hide()

            } else{
                if (nameEditText.text.isEmpty()) nameEditText.error = "Please add the name of the food!"
                if (ingredientsEditText.text.isEmpty()) ingredientsEditText.error = "Please add the ingredients of the food!"
                if (directionsEditText.text.isEmpty()) directionsEditText.error = "Please add the directions of the food!"
                if (totalTimeEditText.text.isEmpty()) totalTimeEditText.error = "Please add the total cooking time of the food!"
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.new_item)
            .setView(getContentView())
            .setPositiveButton(R.string.ok) { dialogInterface, i -> }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }
    companion object {
        const val TAG = "NewRecipeItemDialogFragment"
    }


    private fun getContentView(): View {
        val contentView = LayoutInflater.from(context).inflate(R.layout.dialog_new_item, null)
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
        addPicture = contentView.findViewById(R.id.RecipeItemPictureButton)
        addPicture.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            try{
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
            }catch(e: ActivityNotFoundException){
                Toast.makeText(requireContext(), "Camera was not found", Toast.LENGTH_SHORT)
            }
        }
        return contentView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
        }
    }

    private fun isValid() :Boolean{
        return (nameEditText.text.isNotEmpty() &&
                ingredientsEditText.text.isNotEmpty() &&
                directionsEditText.text.isNotEmpty() &&
                totalTimeEditText.text.isNotEmpty())

    }

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

