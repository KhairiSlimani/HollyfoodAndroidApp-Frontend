package tn.esprit.hollyfood.view.fragments.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tn.esprit.hollyfood.R
import tn.esprit.hollyfood.databinding.FragmentEditRestaurantBinding
import tn.esprit.hollyfood.model.entities.Restaurant
import tn.esprit.hollyfood.util.Validation
import tn.esprit.hollyfood.viewmodel.RestaurantViewModel

class EditRestaurantFragment : Fragment(R.layout.fragment_edit_restaurant) {
    private lateinit var binding: FragmentEditRestaurantBinding
    private lateinit var viewModel: RestaurantViewModel

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
                    userId = it.userId
                }
            })

            buttonSave.setOnClickListener {
                buttonSave.startAnimation()
                val phoneNumber = edPhoneNumber.text.toString().trim().toIntOrNull() ?: -1
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
}