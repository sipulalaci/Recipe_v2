package hu.bme.aut.myapplication.fragments

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Environment.getExternalStorageDirectory
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import hu.bme.aut.myapplication.R
import hu.bme.aut.myapplication.data.RecipeItem
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


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
    private lateinit var takePicture: Button
    private lateinit var addPicture: Button
    private val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var imageView: ImageView
    lateinit var currentPhotoPath: String
    private val cameraRequest = 1888
    lateinit var imagePath: String

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

            } else {
                if (nameEditText.text.isEmpty()) nameEditText.error =
                    "Please add the name of the food!"
                if (ingredientsEditText.text.isEmpty()) ingredientsEditText.error =
                    "Please add the ingredients of the food!"
                if (directionsEditText.text.isEmpty()) directionsEditText.error =
                    "Please add the directions of the food!"
                if (totalTimeEditText.text.isEmpty()) totalTimeEditText.error =
                    "Please add the total cooking time of the food!"
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
        const val TAG = "NewRecipeItemDialog"
        private val IMAGE_PICK_CODE = 1000
        private val PERMISSION_CODE = 1001
    }


    private fun getContentView(): View {
        val contentView = LayoutInflater.from(context).inflate(R.layout.dialog_new_item, null)
        nameEditText = contentView.findViewById(R.id.RecipeItemNameEditText)
        ingredientsEditText = contentView.findViewById(R.id.RecipeItemIngredientsEditText)
        ingredientsEditText.setHint("Add each ingredient in new line separated with enter!")
        directionsEditText = contentView.findViewById(R.id.RecipeItemDirectionsEditText)
        directionsEditText.setHint("Add each direction step separated with enter!")
        priceSpinner = contentView.findViewById(R.id.RecipePriceSpinner)
        totalTimeEditText = contentView.findViewById(R.id.RecipeItemTimeEditText)
        categorySpinner = contentView.findViewById(R.id.RecipeItemCategorySpinner)
        imageView = contentView.findViewById(R.id.RecipeImageView)
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
                android.R.layout.simple_spinner_dropdown_item
            )
        )
        currentPhotoPath = ""

        if (checkSelfPermission(
                requireContext().applicationContext,
                Manifest.permission.CAMERA
            )
            == PackageManager.PERMISSION_DENIED
        )
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                cameraRequest
            )
        takePicture = contentView.findViewById(R.id.RecipeItemPictureButton)
        takePicture.setOnClickListener {
            dispatchTakePictureIntent()
        }

        addPicture = contentView.findViewById(R.id.RecipeItemAddPictureButton)
        addPicture.setOnClickListener {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//                if (checkSelfPermission(requireContext(),Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
//                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
//                    requestPermissions(permissions, PERMISSION_CODE)
//                } else{
//                    galleryAddPic();
//                }
//            }else{
//                galleryAddPic();
//            }
            galleryAddPic()
        }
        return contentView
    }


    //Code for taking new picture for recipe.
    //Attached to takePicture button.
    private fun dispatchTakePictureIntent() {

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(activity?.packageManager!!)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.example.android.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath


            System.out.println(currentPhotoPath)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            System.out.println(currentPhotoPath)

//            var imgFile = File(currentPhotoPath)
//            var myBitMap = BitmapFactory.decodeFile(imgFile.absolutePath)
//            imageView.setImageBitmap(myBitMap)
           // Glide.with(requireContext()).load(currentPhotoPath).into(imageView)
            imageView.setImageURI(currentPhotoPath.toUri())

        }
//        if (requestCode == IMAGE_PICK_CODE) {
//
//            currentPhotoPath = getRealPathFromURI(requireContext(), data?.data!!).toString()
//            //imageView.setImageURI(currentPhotoPath.toUri())
//
//            var imgFile = File(currentPhotoPath)
//            var myBitMap = BitmapFactory.decodeFile(imgFile.absolutePath)
//            imageView.setImageBitmap(myBitMap)
//
//
//        }
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {

            currentPhotoPath = data?.data.toString()
//            System.out.println(currentPhotoPath)
            //var realPath = getPath(requireContext(), data?.data!!)
            var realPath = getRealPathFromURI(currentPhotoPath.toUri())
            //currentPhotoPath = realPath.toString()
//            System.out.println(realPath)
//            imageView.setImageURI(realPath!!.toUri())
            var f = File(currentPhotoPath)
            System.out.println(realPath)
            //imageView.setImageURI(realPath?.toUri())
            //Picasso.get().load(realPath).into(imageView)
            imageView.setImageURI(realPath?.toUri())
            //Glide.with(requireContext()).load(realPath).into(imageView)
        }

    }

    private fun getRealPathFromURI(contentURI: Uri): String? {
        val result: String?
        val cursor: Cursor? = context?.contentResolver?.query(contentURI, null, null, null, null)
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }

    private fun getRealPathFromURI(context: Context, contentUri: Uri): String? {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "getRealPathFromURI Exception : $e")
            ""
        } finally {
            cursor?.close()
        }
    }

    private fun saveImage(finalBitmap: Bitmap) {

        val root = getExternalStorageDirectory().toString()
        val myDir = File(root + "/capture_photo")
        myDir.mkdirs()
        val generator = Random()
        var n = 10000
        n = generator.nextInt(n)
        val OutletFname = "Image-$n.jpg"
        val file = File(myDir, OutletFname)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            imagePath = file.absolutePath
            out.flush()
            out.close()


        } catch (e: Exception) {
            e.printStackTrace()

        }

    }


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            val imageBitmap = data?.extras?.get("data") as Bitmap
//            imageView.setImageBitmap(imageBitmap)
//        }
//    }

    //
    private fun galleryAddPic() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)

    }
//
//    private fun setPic() {
//        // Get the dimensions of the View
//        val targetW: Int = imageView.width
//        val targetH: Int = imageView.height
//        val bmOptions = BitmapFactory.Options()
//        bmOptions.apply {
//            // Get the dimensions of the bitmap
//            inJustDecodeBounds = true
//
//
//            BitmapFactory.decodeFile(currentPhotoPath, bmOptions)
//
//            val photoW: Int = outWidth
//            val photoH: Int = outHeight
//
//            // Determine how much to scale down the image
//            val scaleFactor: Int = Math.max(1, Math.min(photoW / targetW, photoH / targetH))
//
//            // Decode the image file into a Bitmap sized to fill the View
//            inJustDecodeBounds = false
//            inSampleSize = scaleFactor
//            inPurgeable = true
//        }
//        BitmapFactory.decodeFile(currentPhotoPath, bmOptions)?.also { bitmap ->
//            imageView.setImageBitmap(bitmap)
//        }
//    }

    private fun isValid(): Boolean {
        return (nameEditText.text.isNotEmpty() &&
                ingredientsEditText.text.isNotEmpty() &&
                directionsEditText.text.isNotEmpty() &&
                totalTimeEditText.text.isNotEmpty())
    }

    private fun getRecipeItem() = RecipeItem(
        id = null,
        //TODO: pictures
        pictureURI = currentPhotoPath,
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

