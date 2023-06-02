package com.hdd.pakwan.presentation.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.hdd.pakwan.R
import com.hdd.pakwan.data.models.PreparationSchema
import com.hdd.pakwan.data.models.Recipe
import com.hdd.pakwan.data.remoteDataSource.ServiceBuilder
import com.hdd.pakwan.domain.adapter.SetFragmentAdapter
import com.hdd.pakwan.domain.adapter.ViewPager2Adapter
import com.hdd.pakwan.presentation.fragments.AddRecipeDirectionsFragment
import com.hdd.pakwan.presentation.fragments.AddRecipeHashtagsFragment
import com.hdd.pakwan.presentation.fragments.AddRecipeIngredientsFragment
import com.hdd.pakwan.presentation.fragments.AddRecipePreparationFragment
import com.hdd.pakwan.repository.RecipeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class AddRecipeActivity : AppCompatActivity() {
    private lateinit var editRequiredRecipeId: String
    private lateinit var previewRecipeData: Recipe
    private lateinit var getRecipe: Recipe

    private var isEditMode: Boolean=false
    private var isImageUpload: Boolean=false
    //Initialize
    private lateinit var tabTitleList: ArrayList<String>
    private lateinit var fragmentList: ArrayList<Fragment>
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var recipe_profile_image: ImageView
    private lateinit var recipe_upload_image: ImageView
    private lateinit var recipe_profile_name: TextView

    // for recipe add and discard
    private lateinit var recipe_post: ImageView
    private lateinit var recipe_discard: ImageView

    // for upload recipe
    private lateinit var recipe_title: EditText
    private lateinit var recipe_description: EditText
    private lateinit var next_add_recipe_button: Button
    private lateinit var recipeUploadLinearLayout: LinearLayout
    //for preview recipe
    private lateinit var preview_recipe_title: TextView
    private lateinit var preview_recipe_description: TextView
    private lateinit var preview_recipe_upload_image: ImageView
    private lateinit var preview_add_recipe_button: Button
    private lateinit var recipePreviewLinearLayout: LinearLayout
    //add more recipe details layout
    private lateinit var segment2LinearLayout: LinearLayout
    private lateinit var aar_ll_post_recipe: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)
        recipe_profile_image = findViewById(R.id.recipe_profile_image)
        recipe_profile_name = findViewById(R.id.recipe_profile_name)
        // for upload recipe

        //Extract the data…
        val bundle = intent.extras
        val recipeId = bundle?.getString("editRecipeId")
        if (recipeId != null) {
            editRequiredRecipeId=recipeId
            isEditMode=true
            getRecipeById(editRequiredRecipeId)
        } else{
            editRequiredRecipeId= ""
            isEditMode=false
        }

        // for recipe add and discard
        recipe_discard = findViewById(R.id.recipe_discard)
        recipe_post = findViewById(R.id.recipe_post)

        recipe_title = findViewById(R.id.recipe_title)
        recipe_description = findViewById(R.id.recipe_description)
        recipe_upload_image = findViewById(R.id.recipe_upload_image)
        next_add_recipe_button = findViewById(R.id.next_add_recipe_button)
        recipeUploadLinearLayout = findViewById(R.id.recipeUploadLinearLayout)
        //for preview recipe
        preview_recipe_title = findViewById(R.id.preview_recipe_title)
        preview_recipe_description = findViewById(R.id.preview_recipe_description)
        preview_recipe_upload_image = findViewById(R.id.preview_recipe_upload_image)
        preview_add_recipe_button = findViewById(R.id.preview_add_recipe_button)
        recipePreviewLinearLayout = findViewById(R.id.recipePreviewLinearLayout)
        // add more recipe details layout
        segment2LinearLayout = findViewById(R.id.segment2LinearLayout)
        aar_ll_post_recipe = findViewById(R.id.aar_ll_post_recipe)
        // for profile layout
        Glide.with(this@AddRecipeActivity).load(ServiceBuilder.user!!.profile).circleCrop()
            .into(recipe_profile_image)
        recipe_profile_name.text = ServiceBuilder.user!!.fullname

        recipe_discard.setOnClickListener {
            showAlertDialog()
        }

        recipe_post.setOnClickListener {
            if (editRequiredRecipeId.isEmpty()){
                postRecipe()
                super.onBackPressed()
            }
            super.onBackPressed()
        }

        viewPager = findViewById(R.id.rdl_viewPager)
        tabLayout = findViewById(R.id.rdl_tabLayout)
        tabTitleList = arrayListOf<String>("Hashtag", "Prepare", "Item", "Direction")
        fragmentList = arrayListOf<Fragment>(
            AddRecipeHashtagsFragment(),
            AddRecipePreparationFragment(),
            AddRecipeIngredientsFragment(),
            AddRecipeDirectionsFragment(),
        )
        // setting up adapter class for view pager2
        val adapter = ViewPager2Adapter(fragmentList, supportFragmentManager, lifecycle)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitleList[position]
        }.attach()
        recipe_upload_image.setOnClickListener {
            loadPopUpMenu()
        }
        next_add_recipe_button.setOnClickListener {
            when {
                recipe_title.text.isEmpty() -> {recipe_title.error="Recipe Name is required"
                    recipe_title.requestFocus()}
                recipe_description.text.isEmpty() -> { recipe_description.error="Recipe Description is required"
                    recipe_description.requestFocus()}
                isImageUpload == false -> {if (editRequiredRecipeId.isEmpty()){
                    Toast.makeText(this,"Image is required",Toast.LENGTH_SHORT).show()
                }}
                //61fd3c37d9ebcb532d5b5f52
                else -> {
                    addRecipe(ServiceBuilder.recipeId!!,recipe_title.text.toString(), recipe_description.text.toString())
                    recipeUploadLinearLayout.visibility=View.GONE
                    recipePreviewLinearLayout.visibility=View.VISIBLE
                    segment2LinearLayout.visibility=View.VISIBLE
                    aar_ll_post_recipe.visibility=View.VISIBLE
                    next_add_recipe_button.visibility=View.GONE
                    preview_add_recipe_button.visibility=View.VISIBLE
                    hideKeyboard(next_add_recipe_button)
                    showPreviewData(recipe_title.text.toString(), recipe_description.text.toString())
                }
            }
        }
        preview_add_recipe_button.setOnClickListener {
            recipeUploadLinearLayout.visibility=View.VISIBLE
            recipePreviewLinearLayout.visibility=View.GONE
            segment2LinearLayout.visibility=View.GONE
            preview_add_recipe_button.visibility=View.GONE
            next_add_recipe_button.visibility=View.VISIBLE
        }
    }

    override fun onBackPressed() {
        if (ServiceBuilder.recipeId !="") {
            showAlertDialog()
        } else {
            super.onBackPressed()
        }
    }

    private fun discardRecipe() {
        if (ServiceBuilder.recipeId !=""){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val recipeRepository= RecipeRepository()
                val response=recipeRepository.discardRecipe(ServiceBuilder.recipeId!!)
                if(response.success==true){
                    withContext(Dispatchers.Main){
                        super.onBackPressed()
                    }
                }
            }catch (ex:Exception){
                print(ex)
            }
        }
        }
    }
    private fun postRecipe() {
        if (editRequiredRecipeId.isEmpty()){
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val recipeRepository= RecipeRepository()
                    val response=recipeRepository.postRecipe(ServiceBuilder.recipeId!!)
                    ServiceBuilder.recipeId=""
                    if(response.success==true){
                        withContext(Dispatchers.Main){
                            super.onBackPressed()
                        }
                    }
                }catch (ex:Exception){
                    print(ex)
                }
            }
        }
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Discard Recipe")
        builder.setIcon(R.drawable.cross)
        builder.setMessage("Are you sure to discard this recipe?")

        //performing Positive action
        builder.setPositiveButton("Yes") { _, _ ->
            if (editRequiredRecipeId.isEmpty()){
                discardRecipe()
                super.onBackPressed()
            } else {
                Toast.makeText(this, "Changes are saved", Toast.LENGTH_SHORT).show()
                super.onBackPressed()
            }
        }

        //performing cancel action
        builder.setNeutralButton("Cancel") { _, _ ->
        }
        //performing negative action
        builder.setNegativeButton("No") { _, _ ->
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    // Load pop up menu
    private fun loadPopUpMenu() {
        val popupMenu = PopupMenu(this, recipe_upload_image)
        popupMenu.menuInflater.inflate(R.menu.gallery_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menuCamera -> openCamera()
                R.id.menuGallery -> openGallery()
            }
            true
        }
        popupMenu.show()
    }
    private var REQUEST_GALLERY_CODE = 0
    private var REQUEST_CAMERA_CODE = 1
    private var imageUrl: String? = null
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_GALLERY_CODE)
    }
    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CAMERA_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_GALLERY_CODE && data != null) {
                val selectedImage: Uri? = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val contentResolver = contentResolver
                val cursor =
                    contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
                cursor!!.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                imageUrl = cursor.getString(columnIndex)
                recipe_upload_image.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
                preview_recipe_upload_image.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
                isImageUpload=true
                cursor.close()
            } else if (requestCode == REQUEST_CAMERA_CODE && data != null) {
                val imageBitmap = data.extras?.get("data") as Bitmap
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val file = bitmapToFile(imageBitmap, "$timeStamp.jpg")
                imageUrl = file!!.absolutePath
                recipe_upload_image.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
                preview_recipe_upload_image.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
                isImageUpload=true
            }
        }
    }
    private fun getMimeType(url: String?): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }

    private fun addRecipe(prevRecipeId:String,recipeName:String,recipeDescription: String) {
        if (isImageUpload) {
            if (imageUrl!=null){
                val file = File(imageUrl!!)
                val mimeType = getMimeType(file.path);
                val reqFile = RequestBody.create(mimeType!!.toMediaTypeOrNull(), file)
//              val reqFiles = RequestBody.create(mimeType!!.toMediaTypeOrNull(), file)   // fragment
                val reqRecipeTitle = RequestBody.create("multipart/fetch-data".toMediaTypeOrNull(), recipeName)
                val reqRecipeDescription = RequestBody.create("multipart/fetch-data".toMediaTypeOrNull(),recipeDescription)
                val reqPrevRecipeId = RequestBody.create("multipart/fetch-data".toMediaTypeOrNull(), prevRecipeId)
                val body = MultipartBody.Part.createFormData("image",file.name,reqFile)
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val recipeRepository = RecipeRepository()
                        val response = recipeRepository.addRecipe(
                            body,
                            reqRecipeTitle,
                            reqRecipeDescription,
                            reqPrevRecipeId
                        )
                        if (response.success == true) {
                            withContext(Dispatchers.Main) {
                                previewRecipeData = response.data!!
                                ServiceBuilder.recipeId = previewRecipeData._id
                            }
                        }
                    } catch (ex: Exception) {
                        withContext(Dispatchers.Main) {
                            print(ex)
                        }
                    }
                }
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val recipeRepository = RecipeRepository()
                        val response = recipeRepository.updateRecipeWithoutImage(prevRecipeId,Recipe(title =recipeName, description =  recipeDescription))
                        if (response.success == true) {
                            withContext(Dispatchers.Main) {
                                previewRecipeData = response.data!!
                                ServiceBuilder.recipeId = previewRecipeData._id
                            }
                        }
                    } catch (ex: Exception) {
                        withContext(Dispatchers.Main) {
                            print(ex)
                        }
                    }
                }
            }
        }
    }

    private fun showPreviewData(inputTitle: String, inputDescription: String) {
        preview_recipe_title.text = inputTitle
        preview_recipe_description.text = inputDescription
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun getRecipeById(recipeId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val recipeRepository = RecipeRepository()
                val response = recipeRepository.getRecipeById(recipeId!!)
                if (response.success == true) {
                    getRecipe = response.data!!
                    withContext(Dispatchers.Main) {
                        ServiceBuilder.recipeId=getRecipe._id
                        recipe_title.setText(getRecipe.title)
                        recipe_description.setText(getRecipe.description)
                        isImageUpload=true
                        Glide.with(this@AddRecipeActivity).load(getRecipe.image).into(recipe_upload_image)
                        Glide.with(this@AddRecipeActivity).load(getRecipe.image).into(preview_recipe_upload_image)

                        tabTitleList =
                            arrayListOf<String>("Preparation", "Ingredients", "Directions")
                        fragmentList = arrayListOf<Fragment>(
                            AddRecipePreparationFragment(isEditMode,getRecipe.preparation!!),
                            AddRecipeIngredientsFragment(isEditMode, getRecipe.ingredients!!),
                            AddRecipeDirectionsFragment(isEditMode, getRecipe.direction!!)
                        )

                        // setting up adapter class for view pager2 in PDL
                        val adapter =
                            SetFragmentAdapter(fragmentList, supportFragmentManager, lifecycle)
                        viewPager.adapter = adapter
                        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                            tab.text = tabTitleList[position]
                        }.attach()
                    }

                }
            } catch (ex: Exception) {
                print(ex)
            }
        }
    }

    private fun bitmapToFile(bitmap: Bitmap, fileNameToSave: String): File? {
        var file: File? = null
        return try {
            file = File(
                getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    .toString() + File.separator + fileNameToSave
            )
            file.createNewFile()
            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos) // YOU can also save it in JPEG
            val bitMapData = bos.toByteArray()
            //write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitMapData)
            fos.flush()
            fos.close()
            file
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            file // it will return null
        }
    }
}