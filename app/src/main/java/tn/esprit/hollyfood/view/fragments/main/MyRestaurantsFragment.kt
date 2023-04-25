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
import androidx.navigation.fragment.findNavController
import tn.esprit.hollyfood.R
import tn.esprit.hollyfood.databinding.FragmentMyRestaurantsBinding
import tn.esprit.hollyfood.databinding.FragmentProfileBinding
import tn.esprit.hollyfood.model.entities.Restaurant
import tn.esprit.hollyfood.view.adapters.OnListItemClick
import tn.esprit.hollyfood.view.adapters.RestaurantRecyclerView
import tn.esprit.hollyfood.viewmodel.RestaurantViewModel
import tn.esprit.hollyfood.viewmodel.UserViewModel

class MyRestaurantsFragment : Fragment(R.layout.fragment_my_restaurants), OnListItemClick {
    private lateinit var binding: FragmentMyRestaurantsBinding
    private lateinit var viewModel: RestaurantViewModel
    var restaurantsList: List<Restaurant> = emptyList()
    val restaurantRecyclerView: RestaurantRecyclerView by lazy {
        RestaurantRecyclerView()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyRestaurantsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(RestaurantViewModel::class.java)
        val sharedPref = requireContext().getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val userId : String = sharedPref.getString("id", "") ?: ""

        binding.apply {
            tvAddRestaurant.setOnClickListener {
                findNavController().navigate(R.id.action_myRestaurantsFragment_to_addRestaurantFragment)
            }

            recyclerView.adapter = restaurantRecyclerView
            viewModel.getRestaurantsByUser(userId)
        }

        restaurantRecyclerView.onListItemClick = this

        binding.apply {
            viewModel.restaurantsLiveData.observe(viewLifecycleOwner,
                Observer {
                    if (it != null) {
                        restaurantRecyclerView.setList(it)
                    } else {
                        Toast.makeText(
                            context,
                            "Connection Failed",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            )

            viewModel.messageLiveData.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    if(it == "Server error, please try again later."){
                        Toast.makeText(
                            context,
                            it,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
        }
    }

    override fun onItemClick(restaurant: Restaurant) {
        TODO("Not yet implemented")
    }
}