package tn.esprit.hollyfood.view.fragments.main

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
import tn.esprit.hollyfood.databinding.FragmentRestaurantsBinding
import tn.esprit.hollyfood.model.entities.Order
import tn.esprit.hollyfood.model.entities.Plate
import tn.esprit.hollyfood.model.entities.Restaurant
import tn.esprit.hollyfood.view.adapters.OnListItemClick
import tn.esprit.hollyfood.view.adapters.RestaurantRecyclerView
import tn.esprit.hollyfood.viewmodel.RestaurantViewModel

class RestaurantsFragment : Fragment(R.layout.fragment_restaurants), OnListItemClick {
    private lateinit var binding: FragmentRestaurantsBinding
    private lateinit var viewModel: RestaurantViewModel

    val restaurantRecyclerView: RestaurantRecyclerView by lazy {
        RestaurantRecyclerView()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRestaurantsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(RestaurantViewModel::class.java)

        binding.apply {
            recyclerView.adapter = restaurantRecyclerView
            viewModel.getAllRestaurants()
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
        val action = RestaurantsFragmentDirections.actionRestaurantsFragmentToRestaurantDetailsFragment(restaurant.id)
        findNavController().navigate(action)
    }

    override fun onAddToCartClick(plate: Plate) {
        TODO("Not yet implemented")
    }

    override fun onDeleteOrder(order: Order) {
        TODO("Not yet implemented")
    }
}