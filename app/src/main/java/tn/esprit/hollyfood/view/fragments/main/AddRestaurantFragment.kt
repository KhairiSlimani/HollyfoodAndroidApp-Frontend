package tn.esprit.hollyfood.view.fragments.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
import tn.esprit.hollyfood.databinding.FragmentAddRestaurantBinding
import tn.esprit.hollyfood.databinding.FragmentSettingsBinding
import tn.esprit.hollyfood.model.entities.Restaurant
import tn.esprit.hollyfood.model.entities.User
import tn.esprit.hollyfood.util.UploadRequestBody
import tn.esprit.hollyfood.util.Validation
import tn.esprit.hollyfood.util.getFileName
import tn.esprit.hollyfood.viewmodel.RestaurantViewModel
import tn.esprit.hollyfood.viewmodel.UserViewModel
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class AddRestaurantFragment : Fragment(R.layout.fragment_add_restaurant), UploadRequestBody.UploadCallback {
    private lateinit var binding: FragmentAddRestaurantBinding
    private lateinit var viewModel: RestaurantViewModel
    private  var selectedImageUri: Uri?=null
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddRestaurantBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(RestaurantViewModel::class.java)
        val contentResolver = requireContext().contentResolver
        val sharedPref = requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val userId : String = sharedPref.getString("id", "") ?: ""

        binding.apply {
            buttonUpload.setOnClickListener {
                val intent= Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.type="image/*"
                startForResultOpenGallery.launch(intent)
            }

            buttonAdd.setOnClickListener {
                buttonAdd.startAnimation()
                val phoneNumber = edPhoneNumber.text.toString().trim().toIntOrNull() ?: -1

                var parcelFileDescriptor = contentResolver.openFileDescriptor(selectedImageUri!!,"r",null) ?: return@setOnClickListener
                var inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
                val file = File(requireContext().cacheDir, contentResolver.getFileName(selectedImageUri!!))
                val outputStream = FileOutputStream(file)
                inputStream.copyTo(outputStream)
                val uploadRequestFile = UploadRequestBody(file, "image", this@AddRestaurantFragment)
                val image = MultipartBody.Part.createFormData("image", file?.name, uploadRequestFile)

                val restaurant = Restaurant(
                    "0",
                    edName.text.toString().trim(),
                    edAddress.text.toString().trim(),
                    phoneNumber,
                    edDescription.text.toString().trim(),
                    "",
                    userId
                )
                viewModel.addRestaurant(restaurant, image)
            }

            viewModel.restaurantLiveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    buttonAdd.revertAnimation()

                    Toast.makeText(
                        context,
                        "Restaurant added successfully.",
                        Toast.LENGTH_LONG
                    ).show()

                    edName.setText("")
                    edAddress.setText("")
                    edPhoneNumber.setText("")
                    edDescription.setText("")
                }
            })

            viewModel.messageLiveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    buttonAdd.revertAnimation()

                    if(it == "Server error, please try again later."){
                        Toast.makeText(
                            context,
                            it,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })

            lifecycleScope.launch {
                viewModel.restaurantValidation.collect { validation ->
                    edName.error = null
                    edAddress.error = null
                    edPhoneNumber.error = null
                    edDescription.error = null

                    if (validation.name is Validation.Failed) {
                        withContext(Dispatchers.Main) {
                            layoutName.error = validation.name.message
                            buttonAdd.revertAnimation()
                        }
                    }

                    if (validation.address is Validation.Failed) {
                        withContext(Dispatchers.Main) {
                            layoutAddress.error = validation.address.message
                            buttonAdd.revertAnimation()
                        }
                    }

                    if (validation.phoneNumber is Validation.Failed) {
                        withContext(Dispatchers.Main) {
                            layoutPhoneNumber.error = validation.phoneNumber.message
                            buttonAdd.revertAnimation()
                        }
                    }

                    if (validation.description is Validation.Failed) {
                        withContext(Dispatchers.Main) {
                            layoutDescription.error = validation.description.message
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