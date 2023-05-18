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
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tn.esprit.hollyfood.R
import tn.esprit.hollyfood.databinding.FragmentEditPlateBinding
import tn.esprit.hollyfood.databinding.FragmentEditRestaurantBinding
import tn.esprit.hollyfood.model.entities.Plate
import tn.esprit.hollyfood.model.entities.Restaurant
import tn.esprit.hollyfood.util.UploadRequestBody
import tn.esprit.hollyfood.util.Validation
import tn.esprit.hollyfood.viewmodel.PlateViewModel
import tn.esprit.hollyfood.viewmodel.RestaurantViewModel

class EditPlateFragment : Fragment(R.layout.fragment_edit_plate), UploadRequestBody.UploadCallback {
    private lateinit var binding: FragmentEditPlateBinding
    private lateinit var viewModel: PlateViewModel
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
        binding = FragmentEditPlateBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(PlateViewModel::class.java)
        val contentResolver = requireContext().contentResolver
        val plateId = arguments?.getString("plateId") ?: ""
        val sharedPref = requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        var userId : String = sharedPref.getString("id", "") ?: ""
        var restaurantId : String = ""

        viewModel.getPlateById(plateId)

        binding.apply {
            viewModel.plateLiveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    edName.setText(it.name)
                    edCategory.setText(it.category)
                    edPrice.setText(it.price.toString())
                    //selectedImageUri = Uri.parse(it.image)
                    restaurantId = it.restaurantId
                    userId = it.userId
                }
            })

            buttonEdit.setOnClickListener {
                buttonEdit.startAnimation()
                val price = edPrice.text.toString().trim().toFloatOrNull() ?: -1

                /*var parcelFileDescriptor = contentResolver.openFileDescriptor(selectedImageUri!!,"r",null) ?: return@setOnClickListener
                var inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
                val file = File(requireContext().cacheDir, contentResolver.getFileName(selectedImageUri!!))
                val outputStream = FileOutputStream(file)
                inputStream.copyTo(outputStream)
                val uploadRequestFile = UploadRequestBody(file, "image", this@EditRestaurantFragment)
                val image = MultipartBody.Part.createFormData("image", file?.name, uploadRequestFile)*/

                val plate = Plate(
                    plateId,
                    edName.text.toString().trim(),
                    edCategory.text.toString().trim(),
                    price as Float,
                    "",
                    restaurantId,
                    userId,
                )

                viewModel.editPlate(plate)
            }

            viewModel.messageLiveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    buttonEdit.revertAnimation()
                    Toast.makeText(
                        context,
                        it,
                        Toast.LENGTH_LONG
                    ).show()
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
                            buttonEdit.revertAnimation()
                        }
                    }

                    if (validation.price is Validation.Failed) {
                        withContext(Dispatchers.Main) {
                            layoutPrice.error = validation.price.message
                            buttonEdit.revertAnimation()
                        }
                    }


                }
            }

        }


    }

    override fun onProgressUpdate(pecentage: Int) {
        TODO("Not yet implemented")
    }
}