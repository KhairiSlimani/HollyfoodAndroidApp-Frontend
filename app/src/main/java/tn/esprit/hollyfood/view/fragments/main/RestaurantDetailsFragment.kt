package tn.esprit.hollyfood.view.fragments.main

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import tn.esprit.hollyfood.R
import tn.esprit.hollyfood.databinding.FragmentRestaurantDetailsBinding
import tn.esprit.hollyfood.viewmodel.RestaurantViewModel

class RestaurantDetailsFragment : Fragment(R.layout.fragment_profile) {
    private lateinit var binding: FragmentRestaurantDetailsBinding
    private lateinit var viewModel: RestaurantViewModel
    private lateinit var builder: AlertDialog.Builder

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRestaurantDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(RestaurantViewModel::class.java)
        builder = AlertDialog.Builder(requireContext())

        val id = arguments?.getString("restaurantId") ?: ""

        viewModel = ViewModelProvider(this).get(RestaurantViewModel::class.java)
        viewModel.getRestaurantById(id)

        binding.apply {
            viewModel.restaurantLiveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    Glide.with(requireContext()).load(it.image).into(restaurantImage)

                    restaurantName.setText(it.name)
                    restaurantDescription.setText(it.description)
                    restaurantAddress.setText(it.address)
                    restaurantPhoneNumber.setText(it.phoneNumber.toString())
                }
            })

            tvEditRestaurant.setOnClickListener {
                val action = RestaurantDetailsFragmentDirections.actionRestaurantDetailsFragmentToEditRestaurantFragment(id)
                findNavController().navigate(action)
            }

            tvDeleteRestaurant.setOnClickListener {
                builder.setTitle("Alert!")
                    .setMessage("Do you really want to delete this restaurant ?")
                    .setCancelable(true)
                    .setPositiveButton("Yes"){dialogInterface, it ->
                        viewModel.deleteRestaurant(id)
                        findNavController().navigate(R.id.action_restaurantDetailsFragment_to_myRestaurantsFragment)
                    }
                    .setNegativeButton("No"){dialogInterface, it ->
                        dialogInterface.cancel()
                    }
                    .show()
            }

            seeMenuButton.setOnClickListener {
                val action = RestaurantDetailsFragmentDirections.actionRestaurantDetailsFragmentToRestaurantMenuFragment(id)
                findNavController().navigate(action)
            }
        }
    }
}