package tn.esprit.hollyfood.view.fragments.main

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import tn.esprit.hollyfood.R
import tn.esprit.hollyfood.databinding.FragmentEditRestaurantBinding
import tn.esprit.hollyfood.model.entities.Restaurant
import tn.esprit.hollyfood.util.UploadRequestBody
import tn.esprit.hollyfood.util.Validation
import tn.esprit.hollyfood.util.getFileName
import tn.esprit.hollyfood.viewmodel.RestaurantViewModel
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class EditRestaurantFragment : Fragment(R.layout.fragment_edit_restaurant), UploadRequestBody.UploadCallback {
    private lateinit var binding: FragmentEditRestaurantBinding
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
        binding = FragmentEditRestaurantBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(RestaurantViewModel::class.java)
        val contentResolver = requireContext().contentResolver
        val id = arguments?.getString("restaurantId") ?: ""
        val sharedPref = requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        var userId : String = sharedPref.getString("id", "") ?: ""

        viewModel.getRestaurantById(id)

        binding.apply {
            viewModel.restaurantLiveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    edName.setText(it.name)
                    edDescription.setText(it.description)
                    edAddress.setText(it.address)
                    edPhoneNumber.setText(it.phoneNumber.toString())
                    //selectedImageUri = Uri.parse(it.image)
                    userId = it.userId
                }
            })

            buttonSave.setOnClickListener {
                buttonSave.startAnimation()
                val phoneNumber = edPhoneNumber.text.toString().trim().toIntOrNull() ?: -1

                /*var parcelFileDescriptor = contentResolver.openFileDescriptor(selectedImageUri!!,"r",null) ?: return@setOnClickListener
                var inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
                val file = File(requireContext().cacheDir, contentResolver.getFileName(selectedImageUri!!))
                val outputStream = FileOutputStream(file)
                inputStream.copyTo(outputStream)
                val uploadRequestFile = UploadRequestBody(file, "image", this@EditRestaurantFragment)
                val image = MultipartBody.Part.createFormData("image", file?.name, uploadRequestFile)*/

                val restaurant = Restaurant(
                    id,
                    edName.text.toString().trim(),
                    edAddress.text.toString().trim(),
                    phoneNumber,
                    edDescription.text.toString().trim(),
                    "",
                    userId
                )

                viewModel.editRestaurant(restaurant)
            }

            viewModel.messageLiveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    buttonSave.revertAnimation()
                    Toast.makeText(
                        context,
                        it,
                        Toast.LENGTH_LONG
                    ).show()
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
                            buttonSave.revertAnimation()
                        }
                    }

                    if (validation.address is Validation.Failed) {
                        withContext(Dispatchers.Main) {
                            layoutAddress.error = validation.address.message
                            buttonSave.revertAnimation()
                        }
                    }

                    if (validation.phoneNumber is Validation.Failed) {
                        withContext(Dispatchers.Main) {
                            layoutPhoneNumber.error = validation.phoneNumber.message
                            buttonSave.revertAnimation()
                        }
                    }

                    if (validation.description is Validation.Failed) {
                        withContext(Dispatchers.Main) {
                            layoutDescription.error = validation.description.message
                            buttonSave.revertAnimation()
                        }
                    }

                }
            }

        }
    }

    override fun onProgressUpdate(pecentage: Int) {

    }
}