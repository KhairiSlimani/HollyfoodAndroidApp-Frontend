package tn.esprit.hollyfood.view.fragments.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import tn.esprit.hollyfood.R
import tn.esprit.hollyfood.databinding.FragmentAddPlateBinding
import tn.esprit.hollyfood.databinding.FragmentAddRestaurantBinding
import tn.esprit.hollyfood.model.entities.Plate
import tn.esprit.hollyfood.model.entities.Restaurant
import tn.esprit.hollyfood.util.UploadRequestBody
import tn.esprit.hollyfood.util.Validation
import tn.esprit.hollyfood.util.getFileName
import tn.esprit.hollyfood.viewmodel.PlateViewModel
import tn.esprit.hollyfood.viewmodel.RestaurantViewModel
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class AddPlateFragment : Fragment(R.layout.fragment_add_plate), UploadRequestBody.UploadCallback {
    private lateinit var binding: FragmentAddPlateBinding
    private lateinit var viewModel: PlateViewModel
    private var selectedImageUri: Uri?=null
    private val startForResultOpenGallery = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            selectedImageUri = result.data!!.data
            /*cover!!.setImageURI(selectedImageUri)
            cover!!.scaleType = ImageView.ScaleType.FIT_XY
            cover!!.adjustViewBounds = true
            cover!!.setPadding(0, 0, 0, 0)
            cover!!.scaleType = ImageView.ScaleType.CENTER_CROP*/
        }
    }

    private val categories = listOf("Pasta", "Pizza", "Plate", "Sandwich", "Other")
    private lateinit var adapter: ArrayAdapter<String>
    lateinit var selectedCategory: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddPlateBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(PlateViewModel::class.java)
        adapter = ArrayAdapter(requireContext(), R.layout.list_item, categories)
        selectedCategory = ""

        val contentResolver = requireContext().contentResolver
        val sharedPref = requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val userId : String = sharedPref.getString("id", "") ?: ""
        val restaurantId = arguments?.getString("restaurantId") ?: ""

        binding.apply {
            buttonUpload.setOnClickListener {
                val intent= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.type="image/*"
                startForResultOpenGallery.launch(intent)
            }

            edCategory.setAdapter(adapter)
            edCategory.onItemClickListener = AdapterView.OnItemClickListener {
                adapterView, view, i, l ->

                selectedCategory = adapterView.getItemAtPosition(i) as String
            }

            buttonAdd.setOnClickListener {
                if(selectedImageUri != null){
                    buttonAdd.startAnimation()
                    val price = edPrice.text.toString().trim().toFloatOrNull() ?: -1
                    Log.e("price", "$price")

                    var parcelFileDescriptor = contentResolver.openFileDescriptor(selectedImageUri!!,"r",null) ?: return@setOnClickListener
                    var inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
                    val file = File(requireContext().cacheDir, contentResolver.getFileName(selectedImageUri!!))
                    val outputStream = FileOutputStream(file)
                    inputStream.copyTo(outputStream)
                    val uploadRequestFile = UploadRequestBody(file, "image", this@AddPlateFragment)
                    val image = MultipartBody.Part.createFormData("image", file?.name, uploadRequestFile)

                    val plate = Plate(
                        "0",
                        edName.text.toString().trim(),
                        selectedCategory,
                        price as Float,
                        "",
                        restaurantId,
                        userId
                    )

                    viewModel.addPlate(plate, image)

                } else {
                    Toast.makeText(
                        context,
                        "You have to select an image.",
                        Toast.LENGTH_LONG
                    ).show()

                }
            }

            viewModel.plateLiveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    buttonAdd.revertAnimation()

                    Toast.makeText(
                        context,
                        "Plate added successfully.",
                        Toast.LENGTH_LONG
                    ).show()

                    edName.setText("")
                    edPrice.setText("")
                }
            })

            viewModel.messageLiveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    buttonAdd.revertAnimation()

                    if(it == "Server error, please try again later." || it == "Network error, please try again later."){
                        Toast.makeText(
                            context,
                            it,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })

            lifecycleScope.launch {
                viewModel.plateValidation.collect { validation ->
                    edName.error = null
                    edCategory.error = null
                    edPrice.error = null

                    if (validation.name is Validation.Failed) {
                        withContext(Dispatchers.Main) {
                            layoutName.error = validation.name.message
                            buttonAdd.revertAnimation()
                        }
                    }

                    if (selectedCategory == "") {
                        withContext(Dispatchers.Main) {
                            layoutCategory.error = "You must select a category."
                            buttonAdd.revertAnimation()
                        }
                    }

                    if (validation.price is Validation.Failed) {
                        withContext(Dispatchers.Main) {
                            layoutPrice.error = validation.price.message
                            buttonAdd.revertAnimation()
                        }
                    }

                }
            }


        }
    }

    override fun onProgressUpdate(pecentage: Int) {

    }

}